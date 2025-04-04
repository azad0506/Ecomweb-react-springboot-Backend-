package com.kun.ecommerce_fullstack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.CartItemException;
import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.CartItem;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.request.AddItemRequest;
import com.kun.ecommerce_fullstack.response.AuthResponse;
import com.kun.ecommerce_fullstack.service.CartItemService;
import com.kun.ecommerce_fullstack.service.UserService;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private UserService userService;

	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<AuthResponse> deleteCartItem(@PathVariable Long cartItemId,
			@RequestHeader("Authorization") String jwt) throws UserException, CartItemException {

		User user = userService.findUserProfileByJwt(jwt);
		cartItemService.deleteCartItem(user.getId(), cartItemId);

		AuthResponse res = new AuthResponse();

		res.setMessage(" deleted item from cart successfully");
		res.setStatus(true);
		return new ResponseEntity<AuthResponse>(res, HttpStatus.OK);

	}
	
	  // ✅ Update Cart Item Quantity
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<AuthResponse> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {

        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);

        AuthResponse res = new AuthResponse();
        res.setMessage("Cart item updated successfully");
        res.setStatus(true);
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
