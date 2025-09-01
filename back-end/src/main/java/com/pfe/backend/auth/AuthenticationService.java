package com.pfe.backend.auth;

import com.pfe.backend.Entities.UtilisateurFrontoffice;
import com.pfe.backend.Repositories.UtilisateurFrontofficRepositiry;
import com.pfe.backend.config.JwtService;
import com.pfe.backend.token.Token;
import com.pfe.backend.token.TokenRepository;
import com.pfe.backend.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  @Autowired
  UtilisateurFrontofficRepositiry userRepository;

  /*public AuthenticationResponse register(RegisterRequest request) {
    var user = UtilisateurFrontoffice.builder()
            .nom(request.getFullname())
            .entreprise(request.)
        .cin(request.getCin())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .build();
  }*/

  public AuthenticationResponse authenticate(Map<Object,Object> request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            String.valueOf(request.get("email")),
            String.valueOf(request.get("password"))) );
    var user = userRepository.findByEmail(String.valueOf(request.get("email")))
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .build();
  }

  private void saveUserToken(UtilisateurFrontoffice user, String jwtToken) {
    var token = new Token();
    token.setUser(user);
    token.setToken(jwtToken);
    token.setTokenType(TokenType.BEARER);
    token.setExpired(false);
    token.setRevoked(false);

    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(UtilisateurFrontoffice user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(Integer.valueOf(String.valueOf(user.getId())));
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public Optional<UtilisateurFrontoffice> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findByEmail(authentication.getName());
  }

}
