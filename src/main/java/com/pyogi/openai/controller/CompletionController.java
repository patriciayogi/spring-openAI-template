package com.pyogi.openai.controller;

import com.pyogi.openai.dto.CompletionResponse;
import com.pyogi.openai.service.CompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.io.IOException;

@RestController
public class CompletionController {

    @Autowired
    private CompletionService translateService;

    @GetMapping(value = "/generate")
    public Mono<CompletionResponse> language(@NonNull @RequestParam("query") String query,
                                             @RequestParam(name = "model",defaultValue = "gpt-3.5-turbo-instruct") String model) throws IOException, InterruptedException {
        return translateService.completion(query, model);
    }

}
