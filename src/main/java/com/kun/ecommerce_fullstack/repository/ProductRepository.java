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
	           "(:category IS NULL OR p.category.parentCategory.parentCategory.name = :parentCategoryName) AND " + 
	           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
	           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
	           "(:minDiscount IS NULL OR p.discountPrsent >= :minDiscount) " )
//	+
//	           "ORDER BY " +
//	           "CASE WHEN :sort = 'price_asc' THEN p.price END ASC, " +
//	           "CASE WHEN :sort = 'price_desc' THEN p.price END DESC, " +
//	           "CASE WHEN :sort = 'discount' THEN p.discount END DESC")
	public List<Product> filterProduct(@Param("category") String category,
			@Param("parentCategoryName") String parentCategoryName,
			@Param("minPrice") Integer minPrice,
			@Param("maxPrice") Integer maxPrice,
			@Param("minDiscount") Integer minDiscount
//			@Param("sort") String sort
			);
	

	
//	@Query("SELECT p FROM Product p WHERE " +
//		       "LOWER(p.category.name) = LOWER(:parentCategoryName) " +
//		       "OR (p.category.parentCategory IS NOT NULL AND LOWER(p.category.parentCategory.name) = LOWER(:parentCategoryName)) " +
//		       "OR (p.category.parentCategory IS NOT NULL AND p.category.parentCategory.parentCategory IS NOT NULL AND " +
//		       "LOWER(p.category.parentCategory.parentCategory.name) = LOWER(:parentCategoryName))")
//		List<Product> findByParentCategoryName(@Param("parentCategoryName") String parentCategoryName);
	
	@Query("""
		    SELECT p FROM Product p 
		    LEFT JOIN p.category c
		    LEFT JOIN c.parentCategory cp
		    LEFT JOIN cp.parentCategory cpp
		    WHERE LOWER(c.name) = LOWER(:parentCategoryName)
		       OR LOWER(cp.name) = LOWER(:parentCategoryName)
		       OR LOWER(cpp.name) = LOWER(:parentCategoryName)
		""")
		List<Product> findByParentCategoryName(@Param("parentCategoryName") String name);
	
	
	
	
//	practice
	 // 1. Find by top-level category name (category's grandparent's name)
		    List<Product> findByCategory_ParentCategory_ParentCategory_Name(String topLevelCategoryName);
 // Second-level category (parent)
    List<Product> findByCategory_ParentCategory_Name(String name);

    // Third-level category (actual category)
    List<Product> findByCategory_Name(String name);



}
