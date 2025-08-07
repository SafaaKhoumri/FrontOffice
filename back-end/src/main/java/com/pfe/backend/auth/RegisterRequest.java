package com.pfe.backend.auth;


import com.pfe.backend.Entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private Long id;


  private String email;


  private String password;

  private String nom;
  private String prenom;
  private String telephone;
  private String entreprise;
  private Role role;
  private Boolean actif = true;
  private LocalDateTime dateCreation = LocalDateTime.now();
  private LocalDateTime derniereConnexion;
}
