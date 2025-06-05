package com.cropdeal.crop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CropApplication {

	public static void main(String[] args) {
		SpringApplication.run(CropApplication.class, args);
		System.out.println("Crop service is running");
	}
}
