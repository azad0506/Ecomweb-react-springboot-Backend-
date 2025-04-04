package com.kun.ecommerce_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kun.ecommerce_fullstack.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String email);
}
