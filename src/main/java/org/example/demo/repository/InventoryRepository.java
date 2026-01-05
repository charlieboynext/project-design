package org.example.demo.repository;

import java.util.Optional;
import org.example.demo.model.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  Optional<Inventory> findByTraceCode(String traceCode);
  
  Page<Inventory> findByTraceCodeContainingIgnoreCaseOrProductNameContainingIgnoreCase(
      String traceCode, String productName, Pageable pageable);
}

