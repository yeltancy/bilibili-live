package com.mylive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.mylive", exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
public class MyliveCloudResourceRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyliveCloudResourceRunApplication.class, args);
    }
}
