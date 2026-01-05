package org.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long expirationMs;

  public JwtTokenProvider(@Value("${security.jwt.secret}") String secret,
                          @Value("${security.jwt.expiration}") long expirationMs) {
    // 使用字节生成 HMAC Key，避免字符串被当作 Base64 解析导致非法字符错误
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(Authentication authentication) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
        .setSubject(authentication.getName())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public long getExpirationMs() {
    return expirationMs;
  }
}
