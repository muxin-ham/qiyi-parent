package com.muxin.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//不需要jdbc
public class WebStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebStartApplication.class,args);
    }
}
