package org.example.demo.repository;

import java.util.Optional;
import org.example.demo.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
  Optional<UserAccount> findByUsername(String username);
  boolean existsByUsername(String username);
}
