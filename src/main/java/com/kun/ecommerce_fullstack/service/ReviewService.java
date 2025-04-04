package com.kun.ecommerce_fullstack.service;

import java.util.List;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Review;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest req, User user) throws ProductException;
	public List<Review> getAllReviewByProductId(Long productId);
}
