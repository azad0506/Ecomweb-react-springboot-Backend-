package com.kun.ecommerce_fullstack.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.CartItem;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.CartRepository;
import com.kun.ecommerce_fullstack.request.AddItemRequest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
//5->2:50

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private CartItemService cartItemService;

	@Override
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	
	@Override
	public Cart findCartByUserId(Long userId)  {
		Cart cart = cartRepository.findCartByUserId(userId);
		

		 if (cart == null || cart.getCartItem() == null) {
		        throw new RuntimeException("Cart not found or cart items are null");
		    }
		 
		int totalprice=0;
		int totalDiscountPrice=0;
		int totalItem=0;
		System.out.println("findCartByUserId");
		 //🔥 Yeh copy create karega, taaki modification ka impact na ho
//		    List<CartItem> cartItems = new ArrayList<>(cart.getCartItem());
//		    System.out.println( " findcartByUserId :  "+cartItems);
		for(CartItem cartItem:cart.getCartItem()) {
//		    for(CartItem cartItem:cartItems) {
			totalprice+=cartItem.getPrice();
			totalDiscountPrice+=cartItem.getDiscountPrice();
			totalItem+=cartItem.getQuantity();
		}
//		    Iterator<CartItem> iterator = cart.getCartItem().iterator();
//		    while (iterator.hasNext()) {
//		        CartItem cartItem = iterator.next();
//		        totalprice += cartItem.getPrice();
//		        totalDiscountPrice += cartItem.getDiscountPrice();
//		        totalItem += cartItem.getQuantity();
//		    }
		
		cart.setTotalDiscountPrice(totalDiscountPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalPrice(totalprice);
		cart.setDiscounte(totalprice-totalDiscountPrice);
		
		return cartRepository.save(cart);
	}

	@Override
	public String addCart(Long userId, AddItemRequest req) throws ProductException {
		// Find the cart for the given user
		Cart cart = cartRepository.findCartByUserId(userId);
		Product product = productService.findProductById(req.getProductId());

		// Check if the product is already in the cart
		CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);

		if (isPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);

			int price = req.getQuantity() * product.getDiscountPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());

			 // 🔥 Ensure the list is mutable
	        if (cart.getCartItem() == null) {
	            cart.setCartItem(new HashSet<>());;
	        }
			
			CartItem createdCartitem = cartItemService.createCartItem(cartItem);
			cart.getCartItem().add(createdCartitem);
		}
		return "item Add to Cart";
	}
	
	


}
