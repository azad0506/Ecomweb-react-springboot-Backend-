package com.kun.ecommerce_fullstack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.config.JwtProvider;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.UserRepository;
import com.kun.ecommerce_fullstack.request.LoginRequest;
import com.kun.ecommerce_fullstack.response.AuthResponse;
import com.kun.ecommerce_fullstack.service.CartService;
import com.kun.ecommerce_fullstack.service.CustomUserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomUserServiceImpl customUserServiceImpl;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CartService cartService;
	//4->1:58
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException
	{
		String email=user.getEmail();
		String password=user.getPassword();
		String firstName=user.getFirstName();
		String lastName=user.getLastName();
		
		User isEmailExist=userRepository.findByEmail(email);
		
		if(isEmailExist!=null) {
			throw new UserException("Email is already used with other account");
		}
		 // Create and save user with encoded password
		User createdUser=new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setFirstName(firstName);
		createdUser.setLastName(lastName);
		
		//save user in db
		User savedUser=userRepository.save(createdUser);
		
		//when user is created then cart also created
		Cart cart=cartService.createCart(savedUser);
		 // Authenticate 
		Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Generate token
		String token=jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(token);   // both are string so conflict
		authResponse.setMessage("signup success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest ){
		
		String username=loginRequest.getEmail();
		String password=loginRequest.getPassword();
		
		
		//authentication
		Authentication authentication=authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Generate token
				String token=jwtProvider.generateToken(authentication);
				
				
				AuthResponse authResponse=new AuthResponse();
				authResponse.setJwt(token);   // both are string so conflict
				authResponse.setMessage("login success");
				
				return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
		
	}



	private Authentication authenticate(String username, String password) {
		UserDetails userDetails=customUserServiceImpl.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("invalid username");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("invalid password");

		}
		return new UsernamePasswordAuthenticationToken( userDetails,null,userDetails.getAuthorities());
	}
}
