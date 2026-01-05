package org.example.demo.repository;

import java.util.Optional;
import org.example.demo.model.entity.Commodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Long> {
  Optional<Commodity> findByTraceCode(String traceCode);
  
  Page<Commodity> findByTraceCodeContainingIgnoreCaseOrProductNameContainingIgnoreCase(
      String traceCode, String productName, Pageable pageable);
}

