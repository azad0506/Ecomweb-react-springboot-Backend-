package com.kun.ecommerce_fullstack.service;

import com.kun.ecommerce_fullstack.exception.CartItemException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.CartItem;
import com.kun.ecommerce_fullstack.model.Product;

public interface CartItemService {

	public CartItem createCartItem(CartItem cartItem);

	public CartItem findCartItemById(Long CartItemId) throws CartItemException;

	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

	public void deleteCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

}
