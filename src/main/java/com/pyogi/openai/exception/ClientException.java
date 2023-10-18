package com.pyogi.openai.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class ClientException extends WebClientException {
    public ClientException(Integer statusCode, String errorMessage) {
        super(STR."Exception occurred: HTTP error \{statusCode}. Error message \{errorMessage}");
    }
}