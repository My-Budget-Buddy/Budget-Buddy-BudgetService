package com.skillstorm.budgetservice.unit.services;

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

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.services.TranscationService;

public class TransactionServiceTest {

    @Mock
    private static LoadBalancerClient loadBalancerClient;

    @InjectMocks
    private TranscationService transactionSvc;

    // @Test
    // public void getTransactionsExcludingIncomeTest() {
    //     int userId = 1;
    //     ServiceInstance serviceInstance = new TestServiceInstance();
    //     when(loadBalancerClient.choose(any(String.class))).thenReturn(serviceInstance);

    //     // verify(loadBalancerClient).choose(any(String.class)).equals(serviceInstance);
    //     // TransactionDTO transaction = new TransactionDTO();

    //     // Optional<TransactionDTO> accountOptional = Optional.of(transaction);
    //     // when(accountRepository.findById(any(int.class))).thenReturn(accountOptional);

    //     // List<TransactionDTO> response = transactionSvc.getTransactionsExcludingIncome(userId);
    //     // List<TransactionDTO> expectedAccounts = new ArrayList<>();

    //     // assertEquals(expectedAccounts, response);
    // }

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
