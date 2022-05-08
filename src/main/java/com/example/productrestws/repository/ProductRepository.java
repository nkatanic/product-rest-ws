package com.example.productrestws.repository;

import java.util.List;
import com.example.productrestws.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
   List<Product> findByIsAvailable(Boolean isAvailable);
}
