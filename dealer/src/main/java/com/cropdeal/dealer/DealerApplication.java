package com.cropdeal.dealer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DealerApplication {

	private static final Logger log = LoggerFactory.getLogger(DealerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DealerApplication.class, args);
		log.info("Dealer service running on port 8092");
	}

}
