package com.example.matches;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MatchesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchesServiceApplication.class, args);
    }
}
