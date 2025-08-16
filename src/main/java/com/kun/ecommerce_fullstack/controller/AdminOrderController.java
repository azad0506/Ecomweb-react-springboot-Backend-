package com.kun.ecommerce_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.OrderException;
import com.kun.ecommerce_fullstack.model.Order;
import com.kun.ecommerce_fullstack.response.AuthResponse;
import com.kun.ecommerce_fullstack.service.OrderService;

@RestController
@RequestMapping("/api/admin/orders")

public class AdminOrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/")
	public ResponseEntity<List<Order>> getAllOrdersHandler() {

		List<Order> orders = orderService.getAllOrder();
		return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{orderId}/confirmed")
	public ResponseEntity<Order> confirmedOrderHandler(@PathVariable(name = "orderId") Long orderid,
			@RequestHeader("Authorization") String jwt) throws OrderException {
				
		Order order=orderService.confirmedOrder(orderid);
		
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/shipped")
	public ResponseEntity<Order> shippedOrderHandler(@PathVariable(name = "orderId") Long orderid,
			@RequestHeader("Authorization") String jwt) throws OrderException {
				
		Order order=orderService.shippedOrder(orderid);
		
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/deliver")
	public ResponseEntity<Order> deliverOrderHandler(@PathVariable(name = "orderId") Long orderid,
			@RequestHeader("Authorization") String jwt) throws OrderException {
				
		Order order=orderService.deliveredOrder(orderid);
		
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrderHandler(@PathVariable Long orderid,
			@RequestHeader("Authorization") String jwt) throws OrderException {
				
		Order order=orderService.cancellOrder(orderid);
		
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@DeleteMapping("/{orderId}/deleteOrder")
	public ResponseEntity<AuthResponse> deleteOrderHandler(@PathVariable(name ="orderId" ) Long orderid,
			@RequestHeader("Authorization") String jwt) throws OrderException {
		
		orderService.deleteOrderById(orderid);
		
		AuthResponse res=new AuthResponse();
		
		res.setMessage("order deleted successfully");
		
		return new ResponseEntity<AuthResponse>(res,HttpStatus.OK);
	}
	
}
