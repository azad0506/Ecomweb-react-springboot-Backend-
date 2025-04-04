package com.kun.ecommerce_fullstack.service;




import java.util.List;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Rating;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.RatingRequest;

public interface RatingService {

	public Rating createRating(RatingRequest req, User user) throws ProductException;
	public List<Rating> getAllRatingofProduct(Long productId);
}