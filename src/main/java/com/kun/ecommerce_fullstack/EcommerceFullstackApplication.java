package com.kun.ecommerce_fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EcommerceFullstackApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceFullstackApplication.class, args);
		
		System.out.println(" D:\\Ecommerce-fullstack\\fullstack-ecom");
		
//		System.out.println("admin and user security updated code");
//		
//		
//		        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		        String rawPassword = "Azad4840@";
//		        String hashedPassword = encoder.encode(rawPassword);
//		        System.out.println("BCrypt Hash: " + hashedPassword);
		    
	}

}
