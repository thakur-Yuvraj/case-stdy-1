package com.cropdeal.farmer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FarmerApplication {

	private static final Logger log = LoggerFactory.getLogger(FarmerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FarmerApplication.class, args);
		log.info("Farmer service running on port 8091");
	}

}
