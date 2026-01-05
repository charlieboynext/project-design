package org.example.demo.model.dto;

public class JwtResponse {

  private final String token;
  private final long expiresInMs;

  public JwtResponse(String token, long expiresInMs) {
    this.token = token;
    this.expiresInMs = expiresInMs;
  }

  public String getToken() {
    return token;
  }

  public long getExpiresInMs() {
    return expiresInMs;
  }
}
