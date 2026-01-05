package org.example.demo.config;

import java.util.Collections;
import org.example.demo.model.entity.Role;
import org.example.demo.model.entity.UserAccount;
import org.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner initDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      if (!userRepository.existsByUsername("admin")) {
        UserAccount admin = new UserAccount();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(Collections.singleton(Role.ADMIN));
        userRepository.save(admin);
      }
    };
  }
}
