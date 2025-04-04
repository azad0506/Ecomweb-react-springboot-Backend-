package com.kun.ecommerce_fullstack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.model.Review;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.ReviewRepository;
import com.kun.ecommerce_fullstack.request.ReviewRequest;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ProductService productService;

	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		// find product for rating
		Product product = productService.findProductById(req.getProductId());
		
		Review review =new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
		//save in db
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReviewByProductId(Long productId) {
		
		return reviewRepository.getAllReviewByProductId(productId);
	}

}
