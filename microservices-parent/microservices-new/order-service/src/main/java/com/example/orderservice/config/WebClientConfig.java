package com.example.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    // Clears confusion on which inventory service to call.
    @LoadBalanced
    // Not originally in pom.xml, manually add WebClient dependency.
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}

