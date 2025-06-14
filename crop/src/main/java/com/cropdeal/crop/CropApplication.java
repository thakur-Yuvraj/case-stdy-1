package com.cropdeal.crop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CropApplication {

	private static final Logger log = LoggerFactory.getLogger(CropApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CropApplication.class, args);
		log.info("Crop service running on port 8082");
	}
}
