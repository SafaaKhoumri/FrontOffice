package com.pfe.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aut")
@RequiredArgsConstructor
public class authController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authResponse = service.authenticate(request);

        // Création du cookie JWT
        Cookie jwtCookie = new Cookie("jwt", authResponse.getAccessToken());
        // Pas de méthode setSameSite : ajout manuel de l'attribut SameSite
        response.setHeader("Set-Cookie",
                String.format("jwt=%s; HttpOnly; Path=/; Max-Age=%d; SameSite=Lax",
                        authResponse.getAccessToken(), 24 * 60 * 60));

        System.out.println(jwtCookie);

        // Optionnel : ne pas renvoyer le token dans le body si tu veux plus de sécurité
        return ResponseEntity.ok(authResponse);

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out");
    }
    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(service.getCurrentUser());
    }

}
