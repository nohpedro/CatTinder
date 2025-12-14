package com.example.swipeservice;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDiscoveryClient
@SpringBootApplication
public class SwipeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwipeServiceApplication.class, args);
    }

}
