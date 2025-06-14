package com.cropdeal.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentApplication {

	private static final Logger log = LoggerFactory.getLogger(PaymentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
		log.info("Payment service running on port 8085");
	}
}
