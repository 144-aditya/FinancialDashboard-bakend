package com.example.financedashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class FinancedashboardBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancedashboardBackendApplication.class, args);
	}
	@GetMapping("/")
	public String home() {
		return "Finance Dashboard Backend is Running!";
	}

	@GetMapping("/test")
	public String test() {
		return "Application is working with Java 17!";
	}
}
