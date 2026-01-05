package org.example.demo.service;

import org.example.demo.model.dto.JwtResponse;
import org.example.demo.model.dto.LoginRequest;
import org.example.demo.model.dto.RegisterRequest;
import org.example.demo.model.entity.UserAccount;
import org.example.demo.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserService userService;

  public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.userService = userService;
  }

  public UserAccount register(RegisterRequest request) {
    return userService.register(request.getUsername(), request.getPassword());
  }

  public JwtResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    String token = tokenProvider.generateToken(authentication);
    return new JwtResponse(token, tokenProvider.getExpirationMs());
  }
}
