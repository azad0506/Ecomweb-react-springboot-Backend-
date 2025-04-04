package com.kun.ecommerce_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Rating;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.RatingRequest;
import com.kun.ecommerce_fullstack.service.RatingService;
import com.kun.ecommerce_fullstack.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/rating")
public class RatingController {

	@Autowired
	private RatingService ratingService;
	@Autowired
	private UserService userService;
	//11->26
	
	@PostMapping("/create")
	public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException {
		User user=userService.findUserProfileByJwt(jwt);

		Rating rating=ratingService.createRating(req, user);
		
		return new ResponseEntity<Rating>(rating,HttpStatus.CREATED);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Rating>> getProductsRatingById(@PathVariable(name = "productId") Long productId,
			@RequestHeader("Authorization") String jwt) throws UserException {
		
		User user=userService.findUserProfileByJwt(jwt);

		List<Rating> ratings=ratingService.getAllRatingofProduct(productId);
		return new ResponseEntity<List<Rating>>(ratings,HttpStatus.ACCEPTED);
	}
	
	
}
