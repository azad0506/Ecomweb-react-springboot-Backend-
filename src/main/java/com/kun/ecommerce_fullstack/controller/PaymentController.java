package com.kun.ecommerce_fullstack.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.OrderException;
import com.kun.ecommerce_fullstack.model.Order;
import com.kun.ecommerce_fullstack.repository.OrderRepository;
import com.kun.ecommerce_fullstack.request.PaymentLinkResponse;
import com.kun.ecommerce_fullstack.response.AuthResponse;
import com.kun.ecommerce_fullstack.service.OrderService;
import com.kun.ecommerce_fullstack.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api")
public class PaymentController {

	@Value("${razorpay.api.key}")
	String apikey;

	@Value("${razorpay.api.secret}")
	String apisecret;

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPayment(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException, RazorpayException {
		System.out.println("API Key: " + apikey);
		System.out.println("API Secret: " + apisecret);


		Order order = orderService.findOrderById(orderId);
		


		try {
			RazorpayClient razorpay=new RazorpayClient(apikey, apisecret);
			
			JSONObject paymentLinkRequest=new JSONObject();
			paymentLinkRequest.put("amount", order.getTotalPrice()*100);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject customerDetail=new JSONObject();
			customerDetail.put("name", order.getUser().getLastName());
			customerDetail.put("email", order.getUser().getEmail());
			paymentLinkRequest.put("customer", customerDetail);
			
			JSONObject notify=new JSONObject();
			notify.put("sms", true);
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);
			
			paymentLinkRequest.put("callback_url", "http://localhost:5173/payment/"+orderId);
//			paymentLinkRequest.put("callback_url", "https://ecomweb-react-springboot.vercel.app/payment/" + orderId);
//			paymentLinkRequest.put("callback_url", "https://ecomweb-react-springboot.vercel.app" + orderId);

			
			paymentLinkRequest.put("callback_method", "get");
			
			System.out.println(" Sending Payment Link Request: " + paymentLinkRequest.toString());

			PaymentLink paymentLink=razorpay.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId=paymentLink.get("id");
			String paymentLinkUrl=paymentLink.get("short_url");
			
			PaymentLinkResponse response=new PaymentLinkResponse();
			response.setPayment_link_id(paymentLinkId);
			response.setPayment_link_url(paymentLinkUrl);

			return new ResponseEntity<PaymentLinkResponse>(response,HttpStatus.CREATED);

		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RazorpayException(e.getMessage());
		}
			
	}
	
	@GetMapping("/payments")
	public ResponseEntity<AuthResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
			@RequestParam(name = "order_id") Long orderId) throws OrderException, RazorpayException{
		
		Order order = orderService.findOrderById(orderId);
		RazorpayClient razorpay=new RazorpayClient(apikey, apisecret);
		
		System.out.println("=== this API CALLED 2times (so we checked) ===");
		System.out.println("paymentid: " + paymentId + " orderid: " + orderId);

		System.out.println("paymentid: "+ paymentId+"   orderid: "+orderId);
		
		try {
			Payment payment=razorpay.payments.fetch(paymentId);
			
			System.out.println("====payment==="+payment);
			
			if(payment.get("status").equals("captured")) {
				order.getPaymentDetails().setPaymentId(paymentId);
				order.getPaymentDetails().setStatus("COMPLETED");
				order.setOrderStatus("PLACED");
				
				orderRepository.save(order);
			}
			
			AuthResponse res=new AuthResponse();
			res.setMessage("your order is placed");
			res.setStatus(true);

			return new ResponseEntity<AuthResponse>(res,HttpStatus.ACCEPTED);
		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RazorpayException(e.getMessage());
		}
		

	}
	
}
