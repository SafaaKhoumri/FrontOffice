package com.pfe.backend.Controllers;

import com.pfe.backend.Entities.*;
import com.pfe.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/frontoffice/auth")
public class AuthFrontofficController {

    @Autowired
    private UtilisateurFrontofficRepositiry userRepository;

    @Autowired
    private SessionFrontofficRepository sessionRepository;

    @Autowired
    private PortailRepository portailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            this.roleCode = user.getRole().getCode();
            this.roleName = user.getRole().getNom_Role();
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("=== DÉBUT LOGIN ===");
            System.out.println("Email reçu: " + request.getEmail());

            // Vérifier les champs obligatoires
            if (request.getEmail() == null || request.getPassword() == null) {
                System.out.println("Erreur: champs manquants");
                return ResponseEntity.badRequest()
                        .body(new LoginResponse(false, "Email et mot de passe requis"));
            }

            // Chercher l'utilisateur par email
            System.out.println("Recherche utilisateur pour: " + request.getEmail());
            Optional<UtilisateurFrontoffice> userOpt = userRepository.findByEmailAndActif(
                    request.getEmail(), true);

            if (!userOpt.isPresent()) {
                System.out.println("Utilisateur non trouvé");
                return ResponseEntity.badRequest()
                        .body(new LoginResponse(false, "Email ou mot de passe incorrect"));
            }

            UtilisateurFrontoffice user = userOpt.get();
            System.out.println("Utilisateur trouvé: " + user.getEmail());
            System.out.println("Rôle: " + user.getRole().getCode());

            // Vérifier le mot de passe
            System.out.println("Vérification du mot de passe...");
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println("Mot de passe incorrect");
                return ResponseEntity.badRequest()
                        .body(new LoginResponse(false, "Email ou mot de passe incorrect"));
            }

            System.out.println("Mot de passe correct !");

            // Vérifier que le rôle est actif
            if (!user.getRole().getActif()) {
                System.out.println("Rôle inactif");
                return ResponseEntity.badRequest()
                        .body(new LoginResponse(false, "Votre compte n'est pas autorisé"));
            }

            // ✅ CORRIGÉ : Chercher le portail associé au rôle
            System.out.println("Recherche portail pour le rôle: " + user.getRole().getCode());
            List<Portail> portails = portailRepository.findByRole_CodeAndActif(
                    user.getRole().getCode(), true);

            if (portails.isEmpty()) {
                System.out.println("Aucun portail trouvé pour ce rôle");
                return ResponseEntity.badRequest()
                        .body(new LoginResponse(false, "Aucun portail disponible pour votre rôle"));
            }

            // Prendre le premier portail trouvé
            Portail portail = portails.get(0);
            System.out.println("Portail trouvé: " + portail.getNom() + " - Code: " + portail.getCode());

            // Générer un token de session
            String token = UUID.randomUUID().toString();

            // Créer une nouvelle session
            SessionFrontoffice session = new SessionFrontoffice(user, token);
            sessionRepository.save(session);

            // Mettre à jour la dernière connexion
            user.setDerniereConnexion(LocalDateTime.now());
            userRepository.save(user);

            // Construire la réponse
            LoginResponse response = new LoginResponse(true, "Connexion réussie");
            response.setToken(token);
            response.setPortailCode(portail.getCode());
            response.setRedirectUrl("/portail/" + portail.getCode());
            response.setUser(new UserInfo(user));

            System.out.println("Connexion réussie ! Redirection vers: " + response.getRedirectUrl());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ERREUR DANS LOGIN: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse(false, "Erreur interne du serveur"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Chercher la session par token
            Optional<SessionFrontoffice> sessionOpt = sessionRepository.findByTokenAndActif(token, true);

            if (sessionOpt.isPresent()) {
                SessionFrontoffice session = sessionOpt.get();
                session.setActif(false);
                sessionRepository.save(session);
            }

            response.put("success", true);
            response.put("message", "Déconnexion réussie");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la déconnexion");
            return ResponseEntity.internalServerError().body(response);
        }
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