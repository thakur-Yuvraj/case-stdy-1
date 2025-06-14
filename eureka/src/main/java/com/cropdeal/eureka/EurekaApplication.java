package com.cropdeal.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

	private static final Logger log = LoggerFactory.getLogger(EurekaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
		log.info("eureka-server running on port 8761");
	}
}

