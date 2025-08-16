package com.kun.ecommerce_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	
	//baseurl/api/products?category=cate&color=value....
	@GetMapping("/products")
	public ResponseEntity<Page<Product>> findProductByCategory(@RequestParam String category,
			@RequestParam String toplevelcategory,
			@RequestParam List<String> colors, @RequestParam List<String> size, @RequestParam Integer minPrice,
			@RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort,
			@RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

		Page<Product> result = productService.getAllProduct(category,toplevelcategory, colors, size, minPrice, maxPrice, minDiscount,
				sort, stock, pageNumber, pageSize);
		
		
		//return ResponseEntity.ok(result);
		System.out.println("complete product");
		
		return new ResponseEntity<>(result,HttpStatus.ACCEPTED);

	}
	
	@GetMapping("/products/id/{productId}")
	public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException{
		
		Product product=productService.findProductById(productId);
		
		return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
		
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public String deleteProductById(@PathVariable Long ProductId) throws ProductException {
		String s=productService.deleteProductById(ProductId);
		
		if(s!=null) {
			return s;
		}
		else {
			return "product is not deleted";
		}
	}
	
	
	
	 @GetMapping("/top-category/{name}")
	    public ResponseEntity<List<Product>> getProductsByTopCategory(@PathVariable("name") String categoryName) {
	        List<Product> products = productService.getProductsByParentCategory(categoryName);
	        return new ResponseEntity<>(products,HttpStatus.ACCEPTED);
	    }
	 
	 
	 @GetMapping("/toplevelcategory/{name}")
	    public ResponseEntity<List<Product>>gettAllProductsByTopLevelCategory(@PathVariable("name") String name) {
	        List<Product> products= productService.findProductsByTopLevelCategory(name);
	        return new ResponseEntity<>(products,HttpStatus.ACCEPTED);

	    }	 
	 
	 @GetMapping("/secondlevelcategory/{name}")
	 public ResponseEntity<List<Product>> getAllProductsBySecondLevelCategory(@PathVariable String name) {
	     List<Product> products = productService.findProductsBySecondLevelCategory(name);
	     return new ResponseEntity<>(products, HttpStatus.OK);
	 }

	 @GetMapping("/thirdlevelcategory/{name}")
	 public ResponseEntity<List<Product>> getAllProductsByThirdLevelCategory(@PathVariable String name) {
	     List<Product> products = productService.findProductsByThirdLevelCategory(name);
	     return new ResponseEntity<>(products, HttpStatus.OK);
	 }
}
