package com.kun.ecommerce_fullstack.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kun.ecommerce_fullstack.exception.ProductException;
import com.kun.ecommerce_fullstack.model.Category;
import com.kun.ecommerce_fullstack.model.Product;
import com.kun.ecommerce_fullstack.repository.CategoryRepository;
import com.kun.ecommerce_fullstack.repository.ProductRepository;
import com.kun.ecommerce_fullstack.request.CreateProductRequest;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product createProduct(CreateProductRequest req) {
		// Find or create top-level category
		Category topLevelCategory = categoryRepository.findByName(req.getToplavelCategory());

		if (topLevelCategory == null) {
			topLevelCategory = new Category();
			topLevelCategory.setName(req.getToplavelCategory());
			topLevelCategory.setLevel(1);

			// save in db
			topLevelCategory = categoryRepository.save(topLevelCategory);
		}

		// Find or create second-level category
		Category secondLevelCategory = categoryRepository.findByNameAndParent(req.getSecondlavelCategory(),
				topLevelCategory.getName());

		if (secondLevelCategory == null) {
			secondLevelCategory = new Category();
			secondLevelCategory.setName(req.getSecondlavelCategory());
			secondLevelCategory.setParentCategory(topLevelCategory);
			secondLevelCategory.setLevel(2);

			secondLevelCategory = categoryRepository.save(secondLevelCategory);
		}

		Category thirdLevelCategory = categoryRepository.findByNameAndParent(req.getThirdelCategory(),
				secondLevelCategory.getName());

		if (thirdLevelCategory == null) {
			thirdLevelCategory = new Category();
			thirdLevelCategory.setName(req.getThirdelCategory());
			thirdLevelCategory.setParentCategory(topLevelCategory);
			thirdLevelCategory.setLevel(3);

			thirdLevelCategory = categoryRepository.save(thirdLevelCategory);
		}

		// Create Product entity
		Product product = new Product();
		product.setTitle(req.getTitle());
		product.setDescription(req.getDescription());
		product.setPrice(req.getPrice());
		product.setDiscountPrice(req.getDiscountPrice());
		product.setDiscountPrsent(req.getDiscountpresent());
		product.setQuantity(req.getQuantity());
		product.setNumRating(0); // Assuming no ratings initially
		product.setBrand(req.getBrand());
		product.setColor(req.getColor());
		product.setSize(req.getSizes()); // Assuming req.getSize() returns a Set<Size>
		product.setImageUrl(req.getImageUrl());
		product.setCategory(thirdLevelCategory);

		// Save product to database
		product = productRepository.save(product);
		return product;
	}

	@Override
	public String deleteProductById(Long productId) throws ProductException {
		Product product = findProductById(productId);

		product.getSize().clear(); // delete size
		productRepository.delete(product);

		return "Product with ID " + productId + " has been deleted successfully.";
	}

	@Override
	public Product updateProductById(Long productId, Product updatedProduct) throws ProductException {
		// Use findProductById to fetch the existing product
		Product existingProduct = findProductById(productId);

		// Update product fields
		existingProduct.setTitle(updatedProduct.getTitle());
		existingProduct.setDescription(updatedProduct.getDescription());
		existingProduct.setPrice(updatedProduct.getPrice());
		existingProduct.setDiscountPrice(updatedProduct.getDiscountPrice());
		existingProduct.setDiscountPrsent(updatedProduct.getDiscountPrsent());
		existingProduct.setQuantity(updatedProduct.getQuantity());
		existingProduct.setBrand(updatedProduct.getBrand());
		existingProduct.setColor(updatedProduct.getColor());
		existingProduct.setSize(updatedProduct.getSize());
		existingProduct.setImageUrl(updatedProduct.getImageUrl());
		existingProduct.setCategory(updatedProduct.getCategory());

		// Save the updated product
		return productRepository.save(existingProduct);
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> product = productRepository.findById(productId);
		if (product.isPresent()) {
			return product.get();
		}
		throw new ProductException("Product with ID " + productId + " not found.");
	}

	@Override
	public List<Product> findAllProductByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> getAllProduct(String category, List<String> colors, List<String> size, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
		
		
		// Create a Pageable object for pagination
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		// Fetch filtered products from the repository
		List<Product> products = productRepository.filterProduct(category, minPrice, maxPrice, minDiscount);

		// Filter products by color
		if (colors != null && !colors.isEmpty()) {
			List<Product> filteredProducts = new ArrayList<>();

			for (Product product : products) {
				if (colors.contains(product.getColor())) {
					filteredProducts.add(product);
				}
			}

			products = filteredProducts;
		}

		// Filter products based on stock availability
		if (stock != null) {
			if ("in_stock".equalsIgnoreCase(stock)) {
				// Keep only products that have quantity greater than 0
				List<Product> filteredProducts = new ArrayList<>();
				for (Product product : products) {
					if (product.getQuantity() > 0) {
						filteredProducts.add(product);
					}
				}
				products = filteredProducts;
			} else if ("out_of_stock".equalsIgnoreCase(stock)) {
				// Keep only products that have quantity equal to 0
				List<Product> filteredProducts = new ArrayList<>();
				for (Product product : products) {
					if (product.getQuantity() == 0) {
						filteredProducts.add(product);
					}
				}
				products = filteredProducts;
			}
		}
		// ✅ Sorting manually
	    if ("price_asc".equals(sort)) {
	        products.sort(Comparator.comparing(Product::getPrice));
	    } else if ("price_desc".equals(sort)) {
	        products.sort(Comparator.comparing(Product::getPrice).reversed());
	    } else if ("discount".equals(sort)) {
	        products.sort(Comparator.comparing(Product::getDiscountPrsent).reversed());
	    }
		// Convert filtered list into a Page object
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min((startIndex + pageable.getPageSize()), products.size());
		
		List<Product> pagedContent = products.subList(startIndex, endIndex);
		Page<Product> filterTotalProducts= new PageImpl<>(pagedContent,pageable,products.size());
		
		return filterTotalProducts;
	}

}
