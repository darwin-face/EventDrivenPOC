package com.adrian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication (exclude = SecurityAutoConfiguration.class, scanBasePackages = { "com.adrian" })
public class SendEmailService {

    public static void main(String[] args) {
        SpringApplication.run(SendEmailService.class, args);
    }
}