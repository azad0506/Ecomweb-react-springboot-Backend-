package com.kun.ecommerce_fullstack.request;


import java.util.HashSet;
import java.util.Set;

import com.kun.ecommerce_fullstack.model.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

	private	String title; 
	private	String description;
	private	int price; 
	private	int discountPrice;
	
	private	int discountpresent;
	private	int quantity;
	private	String brand;
	private	String color;
	
	private	Set<Size> sizes =new HashSet<>();
	
	private String imageUrl;
	
	private	String toplavelCategory; //men
	private	String secondlavelCategory; //clothing
	private	String thirdelCategory;    //mens_shirt
}
