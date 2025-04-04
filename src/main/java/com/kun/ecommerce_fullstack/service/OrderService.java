package com.kun.ecommerce_fullstack.service;

import java.util.List;

import com.kun.ecommerce_fullstack.exception.OrderException;
import com.kun.ecommerce_fullstack.model.Address;
import com.kun.ecommerce_fullstack.model.Order;
import com.kun.ecommerce_fullstack.model.User;

public interface OrderService {

	
	public Order createdOrder(User user, Address shippAddress);
	public Order findOrderById(Long orderId) throws OrderException;
	public void deleteOrderById(Long orderId) throws OrderException;
	public List<Order> userOrderHistory(Long orderId) ;
	public List<Order> getAllOrder();
	
	public Order placeOrder(Long orderId) throws OrderException;
	
	public Order confirmedOrder(Long orderId) throws OrderException;
	
	public Order shippedOrder(Long orderId) throws OrderException;
	public Order deliveredOrder(Long orderId) throws OrderException;
	public Order cancellOrder(Long orderId) throws OrderException;



}
