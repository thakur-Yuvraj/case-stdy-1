package com.cropdeal.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuthApplication {

	private static final Logger log = LoggerFactory.getLogger(AuthApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
		log.info("Auth service running on port 8083");
	}

}
