package com.stockalert.stockalert.repository;

import com.stockalert.stockalert.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}