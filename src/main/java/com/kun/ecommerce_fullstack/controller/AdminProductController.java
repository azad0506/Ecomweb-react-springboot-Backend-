package com.kun.ecommerce_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Order;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.repository.ProductRepository;
import com.kun.ecommerce_fullstack.request.CreateProductRequest;
import com.kun.ecommerce_fullstack.response.AuthResponse;
import com.kun.ecommerce_fullstack.service.ProductService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;
	
	
	@PostMapping("/")
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req){
		
		Product product=productService.createProduct(req);
		
		return new ResponseEntity<Product>(product,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{rpductId}/delete")
	public ResponseEntity<AuthResponse> deleteProduct(@PathVariable Long productId) throws ProductException{
		

		productService.deleteProductById(productId);
		
		AuthResponse res=new AuthResponse();
		
		res.setMessage("order deleted successfully");
//		res.setStatus(true);
		return new ResponseEntity<AuthResponse>(res,HttpStatus.OK);
		
	}
	
	@GetMapping("/findAllProduct")
	public ResponseEntity<List<Product>> findAllProductHandler() {

		List<Product> products=productRepository.findAll();
		return new ResponseEntity<List<Product>>(products, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProduct( @RequestBody Product productReq,@PathVariable Long productId)throws ProductException {
		//TODO: process PUT request
		
		Product product=productService.updateProductById(productId, productReq);
		
		return new ResponseEntity<Product>(product,HttpStatus.CREATED);
	}
	
	@PostMapping("/creates")
	public ResponseEntity<AuthResponse> createMultipleProduct(@RequestBody CreateProductRequest[] req) {
		//TODO: process POST request
		//11-> 12minute
		return null;
	}
	
}
