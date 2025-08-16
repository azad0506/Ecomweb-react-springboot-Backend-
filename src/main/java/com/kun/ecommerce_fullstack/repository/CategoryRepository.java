package com.kun.ecommerce_fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kun.ecommerce_fullstack.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	public Category findByName(String name);
    List<Category> findByNameAndParentCategory_Name(String name, String parentName); // Level 2 & 3

	
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory.name = :parentCategoryName")
	public Category findByNameAndParent(@Param("name") String name,
			@Param("parentCategoryName") String parentCategory);

 // Add this to CategoryRepository
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory.id = :parentId")
    List<Category> findByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

}
