package com.muxin.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.muxin.user.mapper")
public class UserStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserStartApplication.class);
    }

}
