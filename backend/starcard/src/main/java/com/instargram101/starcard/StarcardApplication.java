package com.instargram101.starcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.instargram101")
public class StarcardApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarcardApplication.class, args);
    }
}