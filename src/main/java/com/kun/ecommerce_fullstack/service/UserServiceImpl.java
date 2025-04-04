package com.kun.ecommerce_fullstack.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.config.JwtProvider;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.UserRepository;

import jakarta.validation.constraints.Null;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;

	@Override
	public User findUserById(Long userId) throws UserException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);

		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UserException("user is not found" + userId);
		}
	}

//	@Override
//	public User findUserProfileByJwt(String jwt) throws UserException {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {

		// Extract email from JWT token
		String email = jwtProvider.getEmailFromToken(jwt);
		if (email == null || email.isEmpty()) {
			throw new UserException("Invalid JWT token");
		}

		User user = userRepository.findByEmail(email);

		if (user != null) {
			return user;
		} else {
			throw new UserException("user is not found" + email);
		}

	}

}
