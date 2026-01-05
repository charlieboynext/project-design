package org.example.demo.service;

import java.util.Collections;
import org.example.demo.model.entity.Role;
import org.example.demo.model.entity.UserAccount;
import org.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserAccount register(String username, String rawPassword) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Username already exists");
    }
    UserAccount account = new UserAccount();
    account.setUsername(username);
    account.setPassword(passwordEncoder.encode(rawPassword));
    account.setRoles(Collections.singleton(Role.USER));
    return userRepository.save(account);
  }
}
