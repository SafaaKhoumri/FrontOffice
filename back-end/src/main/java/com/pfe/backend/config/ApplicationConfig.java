package com.pfe.backend.config;

import com.pfe.backend.Entities.UtilisateurFrontoffice;
import com.pfe.backend.Repositories.UtilisateurFrontofficRepositiry;
import com.pfe.backend.auditing.ApplicationAuditAware;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UtilisateurFrontofficRepositiry repository;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      System.out.println("=== UserDetailsService Debug ===");
      System.out.println("Username reçu: '" + username + "'");
      System.out.println("Type: " + username.getClass().getSimpleName());
      System.out.println("Longueur: " + username.length());
      System.out.println("Est vide: " + username.isEmpty());
      System.out.println("Est null: " + (username == null));

      return repository.findByEmail(username)
              .orElseThrow(() -> {
                System.out.println("❌ Utilisateur non trouvé pour: '" + username + "'");
                return new UsernameNotFoundException("User not found");
              });
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
