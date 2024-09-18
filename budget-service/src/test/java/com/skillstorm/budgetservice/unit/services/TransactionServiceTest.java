package com.skillstorm.budgetservice.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.services.TransactionService;

public class TransactionServiceTest {

    @Mock
    private LoadBalancerClient loadBalancerClient;

    @InjectMocks
    private TransactionService transactionService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
    }  

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetTransactionsExcludingIncomeTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        // given
        int userId = 1;
        String transactionServiceString = "transaction-service";
        String uri = "http://localhost:8083";
        List<TransactionDTO> expected = Arrays.asList(new TransactionDTO(), new TransactionDTO());
        
        // mocks service instance and rest client
        ServiceInstance instance = mock(ServiceInstance.class);
        RestClient restClient = mock(RestClient.class);
        RequestHeadersUriSpec requestHeadersUriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestBodySpec = mock(RequestHeadersSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        // uses java reflect to access information
        Field restClientField = TransactionService.class.getDeclaredField("restClient");
        restClientField.setAccessible(true);
        restClientField.set(transactionService, restClient);

        // when
        when(loadBalancerClient.choose(transactionServiceString)).thenReturn(instance);

        when(instance.getUri()).thenReturn(URI.create(uri));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expected);

        // then
        List<TransactionDTO> response = transactionService.getTransactionsExcludingIncome(userId);

        assertEquals(expected, response);
        verify(loadBalancerClient).choose(transactionServiceString);
        verify(instance).getUri();
        verify(restClient).get();
    }

    @Test
    public void testGetTransactionsExcludingIncomeTestIllegalStateException() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        // given
        int userId = 1;
        String transactionServiceString = "transaction-service";
        
        // when
        when(loadBalancerClient.choose(transactionServiceString)).thenReturn(null);

        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.getTransactionsExcludingIncome(userId);
        });

        assertEquals("No instances available for transaction_service", exception.getMessage());
        verify(loadBalancerClient).choose(transactionServiceString);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetTransactionsExcludingIncomeTestHttpClientErrorException() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        // given
        int userId = 1;
        String transactionServiceString = "transaction-service";
        String uri = "http://localhost:8083";
        List<TransactionDTO> expected = Arrays.asList();
        
        // mocks service instance and rest client
        ServiceInstance instance = mock(ServiceInstance.class);
        RestClient restClient = mock(RestClient.class);
        RequestHeadersUriSpec requestHeadersUriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestBodySpec = mock(RequestHeadersSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        // uses java reflect to access information
        Field restClientField = TransactionService.class.getDeclaredField("restClient");
        restClientField.setAccessible(true);
        restClientField.set(transactionService, restClient);

        // when
        when(loadBalancerClient.choose(transactionServiceString)).thenReturn(instance);

        when(instance.getUri()).thenReturn(URI.create(uri));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expected);

        // then
        List<TransactionDTO> response = transactionService.getTransactionsExcludingIncome(userId);

        assertEquals(expected, response);
        verify(loadBalancerClient).choose(transactionServiceString);
        verify(instance).getUri();
        verify(restClient).get();
    }

}