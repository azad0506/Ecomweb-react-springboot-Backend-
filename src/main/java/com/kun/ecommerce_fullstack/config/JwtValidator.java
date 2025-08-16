package com.kun.ecommerce_fullstack.config;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*Ye bas user ko verify karta hai ki JWT valid hai
 *  ya nahi aur is user ke paas kya role/authority hai.
  // - HTTP request se JWT token uthata hai
  // - Secret key se verify karta hai
  // - JWT se username + authorities nikaalta hai
  // - Authorities se Authentication banata hai
  // - SecurityContextHolder mein set karta hai

 */

public class JwtValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String path = request.getRequestURI();

		    // ✅ Skip JWT validation for specific public paths
		    if (path.startsWith("/api/toplevelcategory/**") ||
		        path.startsWith("/api/auth") ||
		        path.startsWith("/api/public") ||
		        path.startsWith("/api/products")) {
		        filterChain.doFilter(request, response); // skip JWT
		        return;
		    }
		
		String jwt=request.getHeader(JwtConstant.JWT_HEADER);
		
		System.out.println("jwt validator :"+ jwt);
		if(jwt!=null) {
			//Baerere kjkjkj (is type ka token ayega)
			jwt=jwt.substring(7);
			
			
			try {
				SecretKey key=Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				 // Parse JWT and extract claims
	            Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(key)
	                    .build()
	                    .parseClaimsJws(jwt)
	                    .getBody();

	            Date expiration = claims.getExpiration();
	            if (expiration.before(new Date())) {
	                throw new BadCredentialsException("Token has expired");
	            }
	            String username = claims.getSubject(); // Extract username
				String email=String.valueOf(claims.get("email"));
				String authorities=String.valueOf(claims.get("authorities"));
				
				List<GrantedAuthority> auths=AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				
				Authentication authentication=new UsernamePasswordAuthenticationToken(email,null, auths);
				
				 // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);


			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new BadCredentialsException("invalid token from jwt validator" );
			}
		}
		
		 // Continue filter chain
        filterChain.doFilter(request, response);
	}

}
