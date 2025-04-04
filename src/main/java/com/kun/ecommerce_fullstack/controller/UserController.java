package com.kun.ecommerce_fullstack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	
	//11->30
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserprofileHandler(@RequestHeader("Authorization") String jwt)
	throws UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
	}
	
}
