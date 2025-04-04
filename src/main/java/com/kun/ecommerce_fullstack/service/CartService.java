package com.kun.ecommerce_fullstack.service;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.AddItemRequest;

public interface CartService {

	public Cart createCart(User user);
	
	public Cart findCartByUserId(Long userId) ;

	public String addCart(Long userId, AddItemRequest req) throws ProductException;
}
