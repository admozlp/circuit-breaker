package com.ademozalp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicePatternApplication {
    public static void main(String[] argsö) {
        SpringApplication.run(MicroservicePatternApplication.class);
    }
}
