package com.kun.ecommerce_fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kun.ecommerce_fullstack.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p WHERE " +
	           "(:category IS NULL OR p.category.name = :category) AND " +  // Fix: Compare category name
	           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
	           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
	           "(:minDiscount IS NULL OR p.discountPrsent >= :minDiscount) " )
//	+
//	           "ORDER BY " +
//	           "CASE WHEN :sort = 'price_asc' THEN p.price END ASC, " +
//	           "CASE WHEN :sort = 'price_desc' THEN p.price END DESC, " +
//	           "CASE WHEN :sort = 'discount' THEN p.discount END DESC")
	public List<Product> filterProduct(@Param("category") String category,
			@Param("minPrice") Integer minPrice,
			@Param("maxPrice") Integer maxPrice,
			@Param("minDiscount") Integer minDiscount
//			@Param("sort") String sort
			);
	

}
