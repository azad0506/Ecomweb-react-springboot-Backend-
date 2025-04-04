package com.kun.ecommerce_fullstack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.AddItemRequest;
import com.kun.ecommerce_fullstack.response.AuthResponse;
import com.kun.ecommerce_fullstack.service.CartService;
import com.kun.ecommerce_fullstack.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/cart")
//@Tag
public class CartController {

	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;

	@GetMapping("/")
//	@operation
	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {

	
//		String s1=new String("abcd");
//		String s="response entity learn kar rhe hai";
//		return new ResponseEntity(s, HttpStatus.OK);
		System.out.println("ye findcart method hai");
		User user=userService.findUserProfileByJwt(jwt);
		System.out.println("ye findcart method hai");
		Cart cart= cartService.findCartByUserId(user.getId());
		
		System.out.println("cart: "+cart );
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}

	@PutMapping("/add")
	public ResponseEntity<String> addItemToCart(
	        @RequestHeader("Authorization") String jwt, 
	        @RequestBody AddItemRequest request) 
	        throws UserException, ProductException {

	    User user = userService.findUserProfileByJwt(jwt);
	    String response = cartService.addCart(user.getId(), request);
	    
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}


	

}
