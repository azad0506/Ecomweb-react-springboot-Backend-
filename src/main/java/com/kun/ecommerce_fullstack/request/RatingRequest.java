package com.kun.ecommerce_fullstack.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RatingRequest {

	private Long productId;
	private double rating;
}
