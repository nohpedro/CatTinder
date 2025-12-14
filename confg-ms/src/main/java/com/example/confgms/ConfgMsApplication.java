package com.example.confgms;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDiscoveryClient
@SpringBootApplication
public class ConfgMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfgMsApplication.class, args);
    }

}
