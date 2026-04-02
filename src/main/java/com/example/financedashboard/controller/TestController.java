package com.example.financedashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Finance Dashboard Backend is Running!");
        response.put("status", "UP");
        response.put("swagger", "http://localhost:8080/swagger-ui.html");
        response.put("h2-console", "http://localhost:8080/h2-console");
        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "Application is working with Java 17!";
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        health.put("application", "Finance Dashboard");
        return health;
    }
}