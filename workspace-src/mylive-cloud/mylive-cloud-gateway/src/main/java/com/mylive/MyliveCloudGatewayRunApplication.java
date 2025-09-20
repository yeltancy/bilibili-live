package com.mylive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mylive"})
public class MyliveCloudGatewayRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyliveCloudGatewayRunApplication.class, args);
    }
}
