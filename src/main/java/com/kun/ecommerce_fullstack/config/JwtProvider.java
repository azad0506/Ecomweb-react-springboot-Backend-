package com.kun.ecommerce_fullstack.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

	SecretKey key=Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	
	public String generateToken(Authentication auth) {
		
		String jwt=Jwts.builder()
                .setSubject(auth.getName()) // Set username as subject
                .claim("email", auth.getName()) // You can store email or any other details
//                .claim("authorities", authorities) // Store user roles
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(new Date().getTime()+846000000)) // Expiry time 24 hour
                .signWith(key) // Sign with secret key
                .compact();
		System.out.println("jwt: "+ jwt);
		return jwt;
	}
	
	public String getEmailFromToken(String jwt) {
		jwt=jwt.substring(7);
		
		// Parse JWT and extract claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

		String email=String.valueOf(claims.get("email"));
		return email;
	}
	
	
//	 public String getEmailFromToken(String jwt) {
//	        if (jwt.startsWith("Bearer ")) {
//	            jwt = jwt.substring(7);
//	        }
//
//	        try {
//	            Claims claims = Jwts.parserBuilder()
//	                    .setSigningKey(key)
//	                    .build()
//	                    .parseClaimsJws(jwt)
//	                    .getBody();
//
//	            return claims.get("email", String.class);
//	        } catch (Exception e) {
//	            System.out.println("Error parsing JWT: " + jwt);
//	            e.printStackTrace();
//	            throw new RuntimeException("Invalid token from jwt validator");
//	        }
//	    }
}
