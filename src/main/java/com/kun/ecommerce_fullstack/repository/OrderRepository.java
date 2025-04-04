package com.kun.ecommerce_fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.kun.ecommerce_fullstack.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.orderStatus='PLACED' OR o.orderStatus='CONFIRMED' OR o.orderStatus='DELIVERED')")
	public List<Order> getUserOrders(@Param("userId") Long userId);
}
