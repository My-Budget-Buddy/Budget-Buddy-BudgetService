package com.skillstorm.budgetservice.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.services.TranscationService;

@SpringBootTest
public class TransactionServiceIntegrationTest {
    
    @Autowired
    private TranscationService transactionService;          // The class under test

    @MockBean
    private LoadBalancerClient loadBalancerClient;    // The class under test's dependency

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AutoCloseable closeable;

    /**
     * Setup and teardown methods to clear the database and open and close mocks before and after each test.
     */
    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("DELETE FROM transaction");
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() throws Exception {
        jdbcTemplate.execute("DELETE FROM transaction");
        closeable.close();
    }

    /**
     * Test case for getTransactionsExcludingIncome
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws URISyntaxException
     */
    @Test
    public void testGetTransactionsExcludingIncomeTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException {
        // given
        int userId = 1;
        String transactionServiceString = "transaction-service";
        String uri = "http://localhost:8083";
        URI uriObj = new URI(uri);

        List<TransactionDTO> expected = Arrays.asList(new TransactionDTO(), new TransactionDTO());

        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        ResponseSpec responseSpec = mock(ResponseSpec.class);
        RequestHeadersUriSpec requestHeaderUriSpec = mock(RequestHeadersUriSpec.class);
        RequestBodySpec requestBodySpec = mock(RequestBodySpec.class);
        RestClient restClient = mock(RestClient.class);
        ServiceInstance serviceInstance = mock(ServiceInstance.class);

        Field restClientField = TranscationService.class.getDeclaredField("restClient");
        
        restClientField.setAccessible(true);
        restClientField.set(transactionService, restClient);

        when(loadBalancerClient.choose(transactionServiceString)).thenReturn(serviceInstance);
        when(serviceInstance.getUri()).thenReturn(uriObj);

        when(restClient.get()).thenReturn(requestHeaderUriSpec);
        when(requestHeaderUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(  TransactionDTO.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expected);

        List<TransactionDTO> response = transactionService.getTransactionsExcludingIncome(userId);

        assertEquals(expected, response);
        verify(loadBalancerClient).choose(transactionServiceString);
        verify(serviceInstance).getUri();
        verify(restClient).get();
    }
}
