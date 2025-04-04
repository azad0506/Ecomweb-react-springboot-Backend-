package com.kun.ecommerce_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Rating;
import com.kun.ecommerce_fullstack.model.Review;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.ReviewRequest;
import com.kun.ecommerce_fullstack.service.ReviewService;
import com.kun.ecommerce_fullstack.service.UserService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private UserService userService;
	// 11->28

	@PostMapping("/create")
	public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException {
		User user = userService.findUserProfileByJwt(jwt);

		Review review = reviewService.createReview(req, user);

		return new ResponseEntity<Review>(review, HttpStatus.CREATED);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Review>> getProductsRatingById(@PathVariable(name = "productId") Long productId
			) throws UserException {
		

		List<Review> reviews=reviewService.getAllReviewByProductId(productId);
		return new ResponseEntity<List<Review>>(reviews,HttpStatus.ACCEPTED);
	}
}
