package com.mylive.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.mylive")
@MapperScan(basePackages = {"com.mylive.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class MyliveAdminRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyliveAdminRunApplication.class, args);
    }
}
