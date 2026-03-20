package com.example.OrderingFood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = {
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//})
public class OrderingFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingFoodApplication.class, args);
	}

}
