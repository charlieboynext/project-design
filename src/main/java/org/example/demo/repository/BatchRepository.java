package org.example.demo.repository;

import java.time.LocalDate;
import org.example.demo.model.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BatchRepository extends JpaRepository<Batch, Long> {
  java.util.Optional<Batch> findByBatchNo(String batchNo);
  Page<Batch> findByBatchNoContainingIgnoreCase(String batchNo, Pageable pageable);

  @Modifying
  @Transactional
  @Query("update Batch b set b.status = org.example.demo.model.entity.BatchStatus.EXPIRED "
      + "where b.expireDate is not null and b.expireDate < :today "
      + "and b.status <> org.example.demo.model.entity.BatchStatus.EXPIRED")
  int markExpired(@Param("today") LocalDate today);
}
