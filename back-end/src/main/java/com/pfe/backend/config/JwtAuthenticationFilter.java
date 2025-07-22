package com.pfe.backend.config;

import com.pfe.backend.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.beans.Transient;
import java.io.IOException;
import java.security.Security;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private final JwtService jwtService;
  @Autowired
  private final UserDetailsService userDetailsService;
  @Autowired
  private final TokenRepository tokenRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
      TokenRepository tokenRepository) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.tokenRepository = tokenRepository;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    String servletPath = request.getServletPath();

    System.out.println("🔍 [JWT FILTER] Path: " + servletPath);

    // ✅ SOLUTION : Ignorer COMPLÈTEMENT tous les endpoints frontoffice
    if (servletPath.startsWith("/api/frontoffice")) {
      System.out.println("⏭️ [JWT FILTER] Ignoring frontoffice endpoint: " + servletPath);
      filterChain.doFilter(request, response);
      return;
    }

    // Ignorer aussi les endpoints d'authentification backoffice
    if (servletPath.contains("/auth") || servletPath.equals("/aut/authenticate")) {
      System.out.println("⏭️ [JWT FILTER] Ignoring auth endpoint: " + servletPath);
      filterChain.doFilter(request, response);
      return;
    }

    System.out.println("🔒 [JWT FILTER] Processing backoffice endpoint: " + servletPath);

    String authHeader = request.getHeader("Authorization");
    String jwt = null;
    String userEmail = null;

    // 1. Récupérer le token du header ou du cookie (BACKOFFICE uniquement)
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
    } else if (request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if ("jwt".equals(cookie.getName())) {
          jwt = cookie.getValue();
          break;
        }
      }
    }

    if (jwt == null) {
      System.out.println("⚠️ [JWT FILTER] No JWT token found for backoffice endpoint");
      filterChain.doFilter(request, response);
      return;
    }

    try {
      userEmail = jwtService.extractUsername(jwt);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        var isTokenValid = tokenRepository.findByToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
          authToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
          System.out.println("✅ [JWT FILTER] User authenticated: " + userEmail);
        }
      }
    } catch (Exception e) {
      System.out.println("❌ [JWT FILTER] JWT processing error: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
  }
}