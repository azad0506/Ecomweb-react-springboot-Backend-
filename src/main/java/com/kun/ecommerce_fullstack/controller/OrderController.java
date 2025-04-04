package com.kun.ecommerce_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.OrderException;
import com.kun.ecommerce_fullstack.exception.UserException;
import com.kun.ecommerce_fullstack.model.Address;
import com.kun.ecommerce_fullstack.model.Order;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.service.OrderService;
import com.kun.ecommerce_fullstack.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	
	
	//11->18
	
	@PostMapping("/")
	public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
			@RequestHeader("Authorization") String jwt) throws UserException {

		User user=userService.findUserProfileByJwt(jwt);
		
		Order order=orderService.createdOrder(user, shippingAddress);
		System.out.println("order"+order);
		
		return new ResponseEntity<Order>(order,HttpStatus.CREATED);
	}
	
	@GetMapping("/user")
	public ResponseEntity< List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException {
		
		User user=userService.findUserProfileByJwt(jwt);

		List<Order> orders=orderService.userOrderHistory(user.getId());
		return new ResponseEntity<List<Order>>(orders,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Order>  findOrderByID(@PathVariable(name = "id") Long orderId,
			@RequestHeader("Authorization") String jwt) throws UserException, OrderException {
		
		User user=userService.findUserProfileByJwt(jwt);

		Order order=orderService.findOrderById(orderId);
		
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}
	
}
