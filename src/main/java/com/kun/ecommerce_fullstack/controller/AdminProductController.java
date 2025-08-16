package com.kun.ecommerce_fullstack.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		System.out.println("========");
		return new ResponseEntity<Product>(product,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<AuthResponse> deleteProduct(@PathVariable Long productId) throws ProductException{
		

		productService.deleteProductById(productId);
		
		AuthResponse res=new AuthResponse();
		
		res.setMessage("product deleted successfully");
//		res.setStatus(true);
		return new ResponseEntity<AuthResponse>(res,HttpStatus.OK);
		
	}
	
	@GetMapping("/findAllProduct")
	public ResponseEntity<Page<Product>> findAllProductHandler(Pageable pageable ) {

		Page<Product> products=productRepository.findAll(pageable);
		return new ResponseEntity<Page<Product>>(products, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProduct( @RequestBody Product productReq,@PathVariable Long productId)throws ProductException {
		//TODO: process PUT request
		
		Product product=productService.updateProductById(productId, productReq);
		
		return new ResponseEntity<Product>(product,HttpStatus.CREATED);
	}
	
	@PostMapping("/creates")
	public ResponseEntity<List<Product>> createMultipleProduct(@RequestBody List<CreateProductRequest>  multipleRequests) {
		//TODO: process POST request
		//11-> 12minute
//		List<Product> products=new ArrayList<>();
//		for(CreateProductRequest req:multipleRequests) {
//			Product product=productService.createProduct(req);
//			products.add(product);
//		}
		List<Product> products=productService.saveMultipleProducts(multipleRequests);
		return new ResponseEntity<>(products,HttpStatus.CREATED);
	}
	
}
