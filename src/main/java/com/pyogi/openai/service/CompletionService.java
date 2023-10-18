package com.pyogi.openai.service;

import com.pyogi.openai.dto.CompletionRequest;
import com.pyogi.openai.dto.CompletionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class CompletionService {
    private final static String COMPLETION_URL = "/completions";

    private final WebClient webClient;

    private final ReactiveCircuitBreaker completionCircuitBreaker;

    public CompletionService(WebClient client, ReactiveCircuitBreakerFactory circuitBreakerFactory){
        this.webClient = client;
        this.completionCircuitBreaker = circuitBreakerFactory.create(COMPLETION_URL);
    }

    public Mono<CompletionResponse> completion(String query, String model) {

        CompletionRequest request = new CompletionRequest
                .Builder(model)
                .prompt(query)
                .build();

        return completionCircuitBreaker.run(webClient.post()
                .uri(COMPLETION_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CompletionResponse.class));
    }
}
