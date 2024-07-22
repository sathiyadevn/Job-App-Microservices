package com.dev43.jonappms.job;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate;
        return new RestTemplate();   // as same as
    }

}
