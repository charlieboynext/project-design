package org.example.demo.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.demo.model.entity.Role;
import org.example.demo.model.entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

  private final Long id;
  private final String username;
  private final String password;
  private final Set<Role> roles;

  public UserPrincipal(Long id, String username, String password, Set<Role> roles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public static UserPrincipal from(UserAccount user) {
    return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public Long getId() {
    return id;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
