package com.kun.ecommerce_fullstack.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewRequest {

	private Long productId;
	private String review;
}
