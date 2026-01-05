package org.example.demo.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.example.demo.model.entity.Role;
import org.example.demo.model.entity.UserAccount;
import org.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AdminUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping
  public List<UserResponse> list() {
    return userRepository.findAll().stream().map(UserResponse::from).collect(Collectors.toList());
  }

  @PostMapping
  public UserResponse create(@Valid @RequestBody UserRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username exists");
    }
    UserAccount u = new UserAccount();
    u.setUsername(request.getUsername());
    u.setPassword(passwordEncoder.encode(request.getPassword()));
    u.setRoles(request.getRoles() == null || request.getRoles().isEmpty() ? Set.of(Role.USER) : request.getRoles());
    return UserResponse.from(userRepository.save(u));
  }

  public static class UserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<Role> roles;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public Set<Role> getRoles() {
      return roles;
    }

    public void setRoles(Set<Role> roles) {
      this.roles = roles;
    }
  }

  public static class UserResponse {
    private Long id;
    private String username;
    private Set<Role> roles;

    public static UserResponse from(UserAccount u) {
      UserResponse r = new UserResponse();
      r.id = u.getId();
      r.username = u.getUsername();
      r.roles = u.getRoles();
      return r;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public Set<Role> getRoles() {
      return roles;
    }

    public void setRoles(Set<Role> roles) {
      this.roles = roles;
    }
  }
}
