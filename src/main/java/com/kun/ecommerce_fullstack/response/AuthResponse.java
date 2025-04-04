package com.kun.ecommerce_fullstack.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {

	private String jwt;
	private String message;
	private boolean status;
	public AuthResponse(String jwt, String message) {
		super();
		this.jwt = jwt;
		this.message = message;
	}
	
	
	
}
