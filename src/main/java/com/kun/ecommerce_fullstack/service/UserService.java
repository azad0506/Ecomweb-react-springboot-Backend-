package com.kun.ecommerce_fullstack.service;

import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.User;

public interface UserService {

	public User findUserById(Long userId) throws UserException;
	public User findUserProfileByJwt(String jwt) throws UserException; 
	
}
