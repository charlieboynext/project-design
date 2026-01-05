package org.example.demo.repository;

import org.example.demo.model.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
  Page<Certificate> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
