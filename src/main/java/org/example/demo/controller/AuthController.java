package org.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.example.demo.model.CommonResponse;
import org.example.demo.model.dto.LoginRequest;
import org.example.demo.model.dto.RegisterRequest;
import org.example.demo.model.entity.UserAccount;
import org.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public CommonResponse register(@Valid @RequestBody RegisterRequest request) {
    UserAccount user = authService.register(request);
    Map<String, Object> payload = new HashMap<>();
    payload.put("id", user.getId());
    payload.put("username", user.getUsername());
    return CommonResponse.ok(payload);
  }

  @PostMapping("/login")
  public CommonResponse login(@Valid @RequestBody LoginRequest request) {
    return CommonResponse.ok(authService.login(request));
  }
}
