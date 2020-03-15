package com.example.ShoppingList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
//@ComponentScan(basePackages = {"com.example.ShoppingList.assembler",
//"com.example.ShoppingList.controller", "com.example.ShoppingList.exception",
//"com.example.ShoppingList.model", "com.example.ShoppingList.repository"})
public class ShoppingListApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingListApplication.class, args);
	}

}
