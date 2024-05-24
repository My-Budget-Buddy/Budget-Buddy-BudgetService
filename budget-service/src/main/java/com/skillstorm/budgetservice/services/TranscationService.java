package com.skillstorm.budgetservice.services;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import com.skillstorm.budgetservice.dto.TransactionDTO;

@Service
public class TranscationService {

    private final LoadBalancerClient loadBalancerClient;
    private final RestClient restClient;

    public TranscationService(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
        this.restClient = RestClient.builder()
                .build();
    }

    public List<TransactionDTO> getTransactionsExcludingIncome(int userId) {

        try {
            ServiceInstance instance = loadBalancerClient.choose("transaction-service");
                if (instance != null) {
                    String serviceUrl = instance.getUri().toString();
                    String fullUrl = serviceUrl + "/transactions/budget/" + userId;

                    return restClient.get()
                                    .uri(fullUrl)
                                    .retrieve()
                                    .body(new ParameterizedTypeReference<List<TransactionDTO>>() {});
                } else {
                    throw new IllegalStateException("No instances available for transaction_service");
                }
            } catch (HttpClientErrorException e) {
                return new ArrayList<>(0);
            }
    }

}
