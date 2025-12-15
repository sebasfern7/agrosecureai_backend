package com.agrosecure.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "AgroSecureAI API");
        response.put("status", "running");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("graphiql", "/graphiql");
        endpoints.put("graphql", "/graphql");
        endpoints.put("auth", "/auth/login");
        
        response.put("endpoints", endpoints);
        return response;
    }
}

