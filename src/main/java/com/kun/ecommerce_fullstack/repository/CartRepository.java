package com.kun.ecommerce_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kun.ecommerce_fullstack.model.Cart;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, Long> {

	@Transactional
	@Query("select c from Cart c where c.user.id=:userId")
	public Cart findCartByUserId(@Param("userId") Long userId);
}
