package com.kun.ecommerce_fullstack.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.exception.CartItemException;
import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.CartItem;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.CartItemRepository;
import com.kun.ecommerce_fullstack.repository.CartRepository;
import com.kun.ecommerce_fullstack.repository.ProductRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserService userService;

	@Override
	public CartItem createCartItem(CartItem cartItem) {
		// TODO Auto-generated method stub
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
		cartItem.setDiscountPrice(cartItem.getProduct().getDiscountPrice() * cartItem.getQuantity());

		return cartItemRepository.save(cartItem);
	}

	@Override
	public CartItem findCartItemById(Long CartItemId) throws CartItemException {
		Optional<CartItem> cartItem = cartItemRepository.findById(CartItemId);
		if (cartItem.isPresent()) {
			return cartItem.get();
		}
		throw new CartItemException("cartItem with ID " + cartItem + " not found.");
	}

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
		CartItem item = findCartItemById(id);
		User user = userService.findUserById(userId);
		
		if(userId.equals(user.getId())) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity()*item.getProduct().getPrice());
			item.setDiscountPrice(item.getProduct().getDiscountPrice()*item.getQuantity());
			
		}

		return cartItemRepository.save(item);
	}

	@Override
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
		CartItem cartItem=cartItemRepository.findCartItemByCartAndProductAndSizeAndUserId(cart, product, size, userId);
		
		return cartItem;
	}

	@Override
	public void deleteCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		
		CartItem cartItem=findCartItemById(cartItemId);
		
		 // Ensure the cartItem has a cart and user associated with it
	    if (cartItem.getCart() == null || cartItem.getCart().getUser() == null) {
	        throw new CartItemException("Invalid cart item. No associated cart or user found.");
	    }

	    // Get the owner of the cart item
	    User cartOwner = cartItem.getCart().getUser();

	    // Check if the user requesting deletion is the cart owner
	    if (!Objects.equals(cartOwner.getId(), userId)) {
	        throw new UserException("You can't remove another user's cart item.");
	    }

	    // Delete the cart item
	    cartItemRepository.delete(cartItem);

	}
}
