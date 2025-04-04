package com.kun.ecommerce_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kun.ecommerce_fullstack.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	
}
