package org.example.demo.repository;

import java.util.List;
import org.example.demo.model.entity.QRCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
  List<QRCode> findByBatch_BatchNoContainingIgnoreCaseOrderByIdDesc(String batchNo);
  List<QRCode> findAllByOrderByIdDesc();
  java.util.Optional<QRCode> findByTraceCode(String traceCode);
  Page<QRCode> findByBatch_BatchNoContainingIgnoreCase(String batchNo, Pageable pageable);
  Page<QRCode> findAll(Pageable pageable);
}
