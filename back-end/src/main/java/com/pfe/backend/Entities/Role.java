package com.pfe.backend.Entities;

import com.fasterxml.jackson.annotation.JsonView;
import com.pfe.backend.views.menu_portail_view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(menu_portail_view.PortailView.class)
    private Long id;
    @JsonView(menu_portail_view.PortailView.class)
    private String nom_role;
    // ⭐ OBLIGATOIRE pour identifier le type de rôle
    @Column(unique = true, nullable = false)
    private String code; // Ex: "TRANSITAIRE", "IMPORTATEUR"

    // ⭐ OBLIGATOIRE pour activer/désactiver
    private Boolean actif = true;

    private String description;

    // Getters/Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom_Role(String nom_role) {
        this.nom_role = nom_role;
    }

    public Long getId() {
        return id;
    }

    public String getNom_Role() {
        return nom_role;
    }
}
