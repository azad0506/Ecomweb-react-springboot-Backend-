package com.kun.ecommerce_fullstack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.model.OrderItem;
import com.kun.ecommerce_fullstack.repository.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		// TODO Auto-generated method stub
		
		return orderItemRepository.save(orderItem);
	}

}
