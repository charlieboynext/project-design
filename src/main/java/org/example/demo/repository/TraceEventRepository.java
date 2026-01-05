package org.example.demo.repository;

import java.util.List;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.TraceEventRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceEventRepository extends JpaRepository<TraceEventRecord, Long> {
  List<TraceEventRecord> findByBatchOrderByEventTimeAsc(Batch batch);
  Page<TraceEventRecord> findByBatch(Batch batch, Pageable pageable);
  Page<TraceEventRecord> findAll(Pageable pageable);
}
