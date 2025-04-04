package com.kun.ecommerce_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.CartItem;
import com.kun.ecommerce_fullstack.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	

	 @Query("SELECT ci FROM CartItem ci WHERE ci.cart = :cart AND ci.product = :product AND ci.size = :size AND ci.cart.user.id = :userId")
	  public  CartItem findCartItemByCartAndProductAndSizeAndUserId(
	            @Param("cart") Cart cart, 
	            @Param("product") Product product, 
	            @Param("size") String size, 
	            @Param("userId") Long userId
	    );}
