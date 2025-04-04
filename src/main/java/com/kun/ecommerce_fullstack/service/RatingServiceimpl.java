package com.kun.ecommerce_fullstack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.model.Rating;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.RatingRepository;
import com.kun.ecommerce_fullstack.request.RatingRequest;

@Service
public class RatingServiceimpl implements RatingService {

	@Autowired
	private ProductService productService;
	@Autowired
	private RatingRepository ratingRepository;
	@Override
	public Rating createRating(RatingRequest req, User user) throws ProductException {
		
		//find product for rating
		Product product=productService.findProductById(req.getProductId());
		
		Rating rating=new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		
		//save in db
		return ratingRepository.save(rating);
	}

	@Override
	public List<Rating> getAllRatingofProduct(Long productId) {
		
		return ratingRepository.getAllRatingsByProductId(productId);
	}

}
