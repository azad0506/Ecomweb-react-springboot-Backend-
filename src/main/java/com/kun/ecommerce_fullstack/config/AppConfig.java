package com.kun.ecommerce_fullstack.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		  .authorizeHttpRequests(Authorize -> Authorize
				  
//	
				    .requestMatchers(
				            "/api/toplevelcategory/**", // ✅ allow this endpoint publicly
				            "/api/auth/**",                 // ✅ if you use login/signup
				            "/api/public/**",              // ✅ optional: generic public APIs
				            "/api/products/**"             // ✅ optional: public product APIs
				        ).permitAll()
		            
				    // ✅ Role based check — yahi add karo!
			        .requestMatchers("/api/admin/**").hasRole("ADMIN")
			        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
			        
			        // ✅ Baaki /api/** sabko JWT chahiye  
		      .requestMatchers("/api/**").authenticated()
		      .anyRequest().permitAll()
		  )
		  .addFilterBefore(new  JwtValidator(), BasicAuthenticationFilter.class) // Placeholder for a custom filter
		  .csrf().disable()
		  .cors().configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfg=new CorsConfiguration();
				
				cfg.setAllowedOriginPatterns(Arrays.asList(
						"http://localhost:5173",
						"https://ecomweb-react-springboot.vercel.app"
						));
				
				cfg.setAllowedMethods(Collections.singletonList("*"));// Allows all HTTP methods (GET, POST, PUT, DELETE, etc.).
				cfg.setAllowCredentials(true);
				cfg.setAllowedHeaders(Collections.singletonList("*"));
				cfg.setExposedHeaders(Arrays.asList("Authorization"));
				cfg.setMaxAge(3600L);
				
				
				return cfg;
			}
		})
		 .and().httpBasic().and().formLogin();
		  
		  
		 return http.build();
	}
	

	
//	user ke password ko  hash karke save karna hai
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return  new BCryptPasswordEncoder();
	}
}
