package com.example.intereses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InteresesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InteresesServiceApplication.class, args);
    }
}

