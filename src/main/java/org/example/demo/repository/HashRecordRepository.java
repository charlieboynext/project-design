package org.example.demo.repository;

import java.util.Optional;
import org.example.demo.model.entity.HashRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashRecordRepository extends JpaRepository<HashRecord, Long> {
  Optional<HashRecord> findByDataHash(String dataHash);
}
