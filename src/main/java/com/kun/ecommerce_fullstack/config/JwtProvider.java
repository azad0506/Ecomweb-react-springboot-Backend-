package com.kun.ecommerce_fullstack.config;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

	SecretKey key=Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	
	public String generateToken(Authentication auth) {

	    UserDetails userDetails = (UserDetails) auth.getPrincipal();

		 // Multiple roles ka comma separated string
	    String authorities = userDetails.getAuthorities().stream()
	    	    .map(r -> r.getAuthority())
	    	    .collect(Collectors.joining(","));

	
	    System.err.println("authorities==>"+authorities);
	    
		String jwt=Jwts.builder()
                .setSubject(auth.getName()) // Set username as subject
                .claim("email", auth.getName()) // You can store email or any other details
//                .claim("authorities",  ) // Store user roles
                .claim("authorities", authorities)
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(new Date().getTime()+846000000)) // Expiry time 24 hour
                .signWith(key) // Sign with secret key
                .compact();
		System.out.println("jwt: "+ jwt);
		return jwt;
	}
	public String generateTokennew(UserDetails userDetails) {
	    String authorities = userDetails.getAuthorities().stream()
	        .map(r -> r.getAuthority())
	        .collect(Collectors.joining(","));

	    String jwt = Jwts.builder()
	        .setSubject(userDetails.getUsername())
	        .claim("email", userDetails.getUsername())
	        .claim("authorities", authorities)
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(new Date().getTime() + 846000000))
	        .signWith(key)
	        .compact();

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
	


}
