package com.kun.ecommerce_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kun.ecommerce_fullstack.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
