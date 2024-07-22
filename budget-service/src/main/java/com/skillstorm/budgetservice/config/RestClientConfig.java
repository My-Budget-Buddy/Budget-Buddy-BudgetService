package com.skillstorm.budgetservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /**
     * Defines a RestClient bean that will be managed by the Spring container.
     * 
     * @return a new instance of RestClient
     */
    @Bean
    public RestClient restClient() {
        // Create and return a new RestClient instance
        return RestClient.create();
    }
}
