package com.pyogi.openai.config;

import com.pyogi.openai.exception.ClientException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Configuration
@EnableConfigurationProperties(OpenaiConfigurationProperties.class)
public class WebClientConfiguration {
    private final OpenaiConfigurationProperties properties;

    public WebClientConfiguration(OpenaiConfigurationProperties properties) {
        this.properties = properties;
    }

    public ExchangeFilterFunction errorHandlerExchangeFilterFunction() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(String.class).flatMap(errorMessage -> Mono.error(new ClientException(clientResponse.statusCode().value(), errorMessage)));
            }
            return just(clientResponse);
        });
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(properties.baseUrl()).defaultHeaders(httpHeaders -> {
            httpHeaders.setBearerAuth(properties.apiKey());
        }).filter(errorHandlerExchangeFilterFunction()).build();
    }
}
