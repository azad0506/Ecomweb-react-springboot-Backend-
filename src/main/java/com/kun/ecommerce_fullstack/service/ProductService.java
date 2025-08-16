package com.kun.ecommerce_fullstack.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.request.CreateProductRequest;

public interface ProductService {

	public Product createProduct(CreateProductRequest req);
	public List<Product> saveMultipleProducts(List<CreateProductRequest> requests);
	
	public String deleteProductById(Long productId) throws ProductException;
	
	public Product updateProductById(Long productId,Product product) throws ProductException;
	public Product findProductById(Long productId) throws ProductException;
	
	public List<Product> findAllProductByCategory(String category);
	
	
	public Page<Product> getAllProduct(String category,String parentCategoryName, List<String> colors,List<String> size,
			Integer minPrice,Integer maxPrice,Integer minDiscount, String sort,String stock,
			Integer pageNumber, Integer pageSize);

	 public List<Product> getProductsByParentCategory(String parentCategoryName) ;

	 
	 
	 public List<Product> findProductsByTopLevelCategory(String topLevelCategoryName);
	 public List<Product> findProductsBySecondLevelCategory(String topLevelCategoryName);
	 public List<Product> findProductsByThirdLevelCategory(String topLevelCategoryName);

	 


}
