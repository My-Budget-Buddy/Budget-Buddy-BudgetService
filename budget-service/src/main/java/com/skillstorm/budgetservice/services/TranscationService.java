package com.skillstorm.budgetservice.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

import com.skillstorm.budgetservice.dto.TransactionDTO;

@Service
public class TranscationService {

    private RestClient restClient;

    public TranscationService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<TransactionDTO> getTransactionsExcludingIncome(int userId) {
        String url = "http://localhost:8083/transactions/budgets/" + userId;
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TransactionDTO>>() {});
    }

}
