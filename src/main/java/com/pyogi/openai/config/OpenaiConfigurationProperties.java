package com.pyogi.openai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public record OpenaiConfigurationProperties(String apiKey,
                                            String organizationId,
                                            String baseUrl) {
    public OpenaiConfigurationProperties {
        if (apiKey == null)
            throw new IllegalArgumentException("Please add openai.apiKey property to the configuration");
        if (baseUrl == null)
            baseUrl = "https://api.openai.com/v1";
    }
}
