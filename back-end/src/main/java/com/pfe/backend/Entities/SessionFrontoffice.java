package com.pfe.backend.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session_frontoffice")
public class SessionFrontoffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private UtilisateurFrontoffice utilisateur;

    @Column(unique = true, nullable = false, length = 500)
    private String token;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(name = "adresse_ip")
    private String adresseIp;

    @Column(name = "user_agent")
    private String userAgent;

    // Constructeurs
    public SessionFrontoffice() {
        this.dateCreation = LocalDateTime.now();
        this.dateExpiration = LocalDateTime.now().plusHours(8); // Session de 8 heures
        this.actif = true;
    }

    public SessionFrontoffice(UtilisateurFrontoffice utilisateur, String token) {
        this();
        this.utilisateur = utilisateur;
        this.token = token;
    }

    // Méthodes utiles
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.dateExpiration);
    }

    public void extendSession(int hours) {
        this.dateExpiration = LocalDateTime.now().plusHours(hours);
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UtilisateurFrontoffice getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurFrontoffice utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}