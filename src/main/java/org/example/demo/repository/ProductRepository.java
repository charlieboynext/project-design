package org.example.demo.repository;

import java.util.Optional;
import org.example.demo.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findByName(String name);
  Page<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand, Pageable pageable);
}
