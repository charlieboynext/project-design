package org.example.demo.repository;

import org.example.demo.model.entity.Org;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRepository extends JpaRepository<Org, Long> {
}
