package com.pfe.backend.Controllers;

import com.pfe.backend.Entities.*;
import com.pfe.backend.Repositories.*;
import com.pfe.backend.auth.AuthenticationRequest;
import com.pfe.backend.auth.AuthenticationResponse;
import com.pfe.backend.auth.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.pfe.backend.auth.AuthenticationService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/frontoffice/auth")
@RequiredArgsConstructor
public class AuthFrontofficController {

    @Autowired
    private UtilisateurFrontofficRepositiry userRepository;

    @Autowired
    private SessionFrontofficRepository sessionRepository;

    @Autowired
    private PortailRepository portailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final AuthenticationService service;


    // DTO pour la requête de login
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters/Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO pour la réponse de login
    public static class LoginResponse {
        private boolean success;
        private String message;
        private String token;
        private String portailCode;
        private String redirectUrl;
        private UserInfo user;

        // Constructeurs
        public LoginResponse() {
        }

        public LoginResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        // Getters/Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPortailCode() {
            return portailCode;
        }

        public void setPortailCode(String portailCode) {
            this.portailCode = portailCode;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public UserInfo getUser() {
            return user;
        }

        public void setUser(UserInfo user) {
            this.user = user;
        }
    }

    public static class UserInfo {
        private Long id;
        private String nom;
        private String prenom;
        private String email;
        private String entreprise;
        private String roleCode;
        private String roleName;

        // Constructeur
        public UserInfo(UtilisateurFrontoffice user) {
            this.id = user.getId();
            this.nom = user.getNom();
            this.prenom = user.getPrenom();
            this.email = user.getEmail();
            this.entreprise = user.getEntreprise();
            this.roleName = user.getRole().getNom();
        }

        // Getters/Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEntreprise() {
            return entreprise;
        }

        public void setEntreprise(String entreprise) {
            this.entreprise = entreprise;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request1,
                                   @RequestBody Map<Object,Object> request,
                                   HttpServletResponse response) {
        String origin = request1.getHeader("Origin");
        String xForwardedProto = request1.getHeader("X-Forwarded-Proto");
        boolean isSecure = request1.isSecure() ||
                "https".equals(xForwardedProto) ||
                (origin != null && origin.startsWith("https://"));

        boolean isLocalhost = origin != null && origin.contains("localhost");

        AuthenticationResponse authResponse = service.authenticate(request);

        // Changez le nom du cookie ici
        String cookieName = "jwt1"; // Ou tout autre nom que vous voulez

        // Création du cookie JWT
        Cookie jwtCookie = new Cookie(cookieName, authResponse.getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        if (isSecure) {
            jwtCookie.setSecure(true);
        }
        response.addCookie(jwtCookie);

        String sameSite = isLocalhost ? "Lax" : "None";
        String cookieHeader = String.format(
                "%s=%s; HttpOnly; Path=/; Max-Age=%d; SameSite=%s%s",
                cookieName,  // Utilisez la variable ici aussi
                authResponse.getAccessToken(),
                24 * 60 * 60,
                sameSite,
                isSecure ? "; Secure" : ""
        );

        response.setHeader("Set-Cookie", cookieHeader);

        // Logs pour debugging
        System.out.println("=== COOKIE CONFIGURATION ===");
        System.out.println("Cookie Name: " + cookieName);
        System.out.println("Origin: " + origin);
        System.out.println("X-Forwarded-Proto: " + xForwardedProto);
        System.out.println("Is Secure: " + isSecure);
        System.out.println("Is Localhost: " + isLocalhost);
        System.out.println("============================");

        UtilisateurFrontoffice user = userRepository.findByEmail(String.valueOf(request.get("email"))).get();
        System.out.println("User found: " + user.getRole().getNom());
        List<Portail> portail = portailRepository.findByRoleId(user.getRole().getId());

        Optional<Portail> p = portailRepository.findByCode(String.valueOf(request.get("code")));
        if(p.isPresent()) {
            if (!p.get().getRole().contains(user.getRole())) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.noContent().build();
        }

        Map<Object,Object> data = new HashMap<>();
        data.put("access_token", authResponse.getAccessToken());
        data.put("redirectUrl","/portail/"+portail.get(0).getCode());
        data.put("user", user);

        return ResponseEntity.ok(data);
    }
    @GetMapping("/a")
    public void a(){
        System.out.println("a");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        System.out.println("=== LOGOUT ===");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<SessionFrontoffice> sessionOpt = sessionRepository.findByTokenAndActif(token, true);

            if (!sessionOpt.isPresent()) {
                response.put("valid", false);
                response.put("message", "Token invalide");
                // ✅ CORRIGÉ : Utiliser status(401) au lieu de unauthorized()
                return ResponseEntity.status(401).body(response);
            }

            SessionFrontoffice session = sessionOpt.get();

            if (session.isExpired()) {
                session.setActif(false);
                sessionRepository.save(session);
                response.put("valid", false);
                response.put("message", "Session expirée");
                // ✅ CORRIGÉ : Utiliser status(401) au lieu de unauthorized()
                return ResponseEntity.status(401).body(response);
            }

            response.put("valid", true);
            response.put("user", new UserInfo(session.getUtilisateur()));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Erreur de vérification");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}