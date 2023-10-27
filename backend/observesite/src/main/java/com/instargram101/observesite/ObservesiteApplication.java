package com.instargram101.observesite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ObservesiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservesiteApplication.class, args);
	}

}
