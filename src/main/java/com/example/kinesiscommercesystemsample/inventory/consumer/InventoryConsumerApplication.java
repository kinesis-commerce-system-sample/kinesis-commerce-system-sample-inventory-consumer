package com.example.kinesiscommercesystemsample.inventory.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryConsumerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(InventoryConsumerApplication.class);
		application.run(args);
	}
}
