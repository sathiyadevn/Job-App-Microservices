package com.dev43.jonappms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class JonAppMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JonAppMsApplication.class, args);
    }

}
