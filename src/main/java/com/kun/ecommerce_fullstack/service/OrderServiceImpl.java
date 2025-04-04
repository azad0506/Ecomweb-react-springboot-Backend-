package com.kun.ecommerce_fullstack.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.exception.OrderException;
import com.kun.ecommerce_fullstack.model.Address;
import com.kun.ecommerce_fullstack.model.Cart;
import com.kun.ecommerce_fullstack.model.CartItem;
import com.kun.ecommerce_fullstack.model.Order;
import com.kun.ecommerce_fullstack.model.OrderItem;
import com.kun.ecommerce_fullstack.model.User;
import com.kun.ecommerce_fullstack.repository.AddressRepository;
import com.kun.ecommerce_fullstack.repository.OrderItemRepository;
import com.kun.ecommerce_fullstack.repository.OrderRepository;
import com.kun.ecommerce_fullstack.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private OrderItemService orderItemService ;
	@Autowired
	private OrderItemRepository orderItemRepository ;
	@Autowired
	private UserRepository userRepository ;
	
	
	@Override
	public Order createdOrder(User user, Address shippAddress) {
		// TODO Auto-generated method stub
		
		shippAddress.setUser(user);
		Address address=addressRepository.save(shippAddress);
		
		user.getAddress().add(address);
		userRepository.save(user);
		
		Cart cart=cartService.findCartByUserId(user.getId());
		
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem cartItem:cart.getCartItem()) {
			
			//orderitem created
			OrderItem orderItem=new OrderItem();
			
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSize(cartItem.getSize());
			orderItem.setUserId(cartItem.getUserId());
			orderItem.setDiscountPrice(cartItem.getDiscountPrice());
			
			//save orderitem in db
			OrderItem createdOrderitem=orderItemRepository.save(orderItem);
			
			orderItems.add(createdOrderitem);
		}
		
		Order createdOrder=new Order();
		createdOrder.setUser(user);
        createdOrder.setOrderItem(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountPrice(cart.getTotalDiscountPrice());
        createdOrder.setDiscounte( cart.getDiscounte());
        createdOrder.setTotalItem(cart.getTotalItem());
        
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreateAt(LocalDateTime.now());
        

        Order savedOrder = orderRepository.save(createdOrder);
        
        for(OrderItem item:orderItems) {
        	item.setOrder(savedOrder);
        	orderItemRepository.save(item);
        }
        return savedOrder;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Optional<Order> opt=orderRepository.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		
	 throw	 new OrderException("order not exist with id:"+orderId);
	}

	@Override
	public void deleteOrderById(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order=findOrderById(orderId);
		
		orderRepository.deleteById(orderId);
		
	}

	@Override
	public List<Order> userOrderHistory(Long orderId) {
		// TODO Auto-generated method stub
		return orderRepository.getUserOrders(orderId);
	}

	@Override
	public List<Order> getAllOrder() {
		// TODO Auto-generated method stub
		return orderRepository.findAll();
	}

	@Override
	public Order placeOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order=findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setStatus("COMPLETE");
		
		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		 Order order = findOrderById(orderId);
	        order.setOrderStatus("CONFIRMED");
	        return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		 Order order = findOrderById(orderId);
	        order.setOrderStatus("SHIPPED");
//	        order.setDeliverDate(LocalDateTime.now().plusDays(3));
	        return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        order.setDeliverDate(LocalDateTime.now());
        return orderRepository.save(order);
	}

	@Override
	public Order cancellOrder(Long orderId) throws OrderException {
		 Order order = findOrderById(orderId);
	        order.setOrderStatus("CANCELLED");
	        order.getPaymentDetails().setStatus("REFUNDED");
	        return orderRepository.save(order);
	}

}
