package com.mylive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.mylive"})
@MapperScan(basePackages = {"com.mylive.mappers"})
@EnableFeignClients
@EnableScheduling
public class MyliveCloudInteractRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyliveCloudInteractRunApplication.class, args);
    }
}
