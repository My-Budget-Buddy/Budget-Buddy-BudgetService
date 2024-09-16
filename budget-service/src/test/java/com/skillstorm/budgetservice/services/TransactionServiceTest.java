package com.skillstorm.budgetservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.skillstorm.budgetservice.dto.TransactionDTO;

@RestClientTest(TranscationService.class)
public class TransactionServiceTest {

    @Mock
    private LoadBalancerClient loadBalancerClient;

    // @Mock
    // private RestClient restClient;
    @Autowired
    private MockRestServiceServer server;

    @InjectMocks
    private TranscationService transactionSvc;
    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        // restClient = RestClient.builder().build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void getTransactionsExcludingIncomeTest() {
        int userId = 1;
        ServiceInstance serviceInstance = new TestServiceInstance();
        when(loadBalancerClient.choose(any(String.class))).thenReturn(serviceInstance);

        // this.server.expect(requestTo("/transactionsPrivate/budget/" + userId))
        //   .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

        List<TransactionDTO> expected = new ArrayList<>();
        // when(restClient.get().uri(any(String.class)).retrieve().body(new ParameterizedTypeReference<List<TransactionDTO>> () {})).thenReturn(expected);

        List<TransactionDTO> response = transactionSvc.getTransactionsExcludingIncome(userId);
        

        assertEquals(expected, response);
    }

    private class TestServiceInstance implements ServiceInstance {

        @Override
        public String getServiceId() {
            throw new UnsupportedOperationException("Unimplemented method 'getServiceId'");
        }

        @Override
        public String getHost() {
            throw new UnsupportedOperationException("Unimplemented method 'getHost'");
        }

        @Override
        public int getPort() {
            throw new UnsupportedOperationException("Unimplemented method 'getPort'");
        }

        @Override
        public boolean isSecure() {
            throw new UnsupportedOperationException("Unimplemented method 'isSecure'");
        }

        @Override
        public URI getUri() {
            try {
                URI uri = new URI("http://localhost:8083");
                return uri;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Map<String, String> getMetadata() {
            throw new UnsupportedOperationException("Unimplemented method 'getMetadata'");
        }

    }

}
