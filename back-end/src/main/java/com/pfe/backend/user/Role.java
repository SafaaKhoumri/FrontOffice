package com.pfe.backend.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;




@Getter

public enum Role {

  ADMIN("ADMIN"),
  ADJOINT("ADJOINT");
  public final String role;

  Role(String R) {
    this.role = R;
  }

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    // Add permissions if needed
    return authorities;
  }
}
