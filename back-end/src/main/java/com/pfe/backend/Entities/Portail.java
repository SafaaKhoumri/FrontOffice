package com.pfe.backend.Entities;

import com.fasterxml.jackson.annotation.JsonView;
import com.pfe.backend.views.menu_portail_view;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter

public class Portail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class,
            menu_portail_view.PortailView.class })

    private Long id;

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.PortailView.class,
            menu_portail_view.AllModification.class })
    private String nom;

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.PortailView.class })
    private LocalDateTime date_creation;

    @ManyToOne
    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.PortailView.class })
    private User utilisateur;



    @ManyToMany(fetch = FetchType.EAGER)
    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.PortailView.class })
    @JoinTable(name = "portail_role", joinColumns = @JoinColumn(name = "portail_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> role = new ArrayList<>();

    @JsonView({ menu_portail_view.withGroupMenu.class, menu_portail_view.PortailView.class })
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "portail_group_menu", joinColumns = @JoinColumn(name = "portail_id"), inverseJoinColumns = @JoinColumn(name = "group_menu_id"))
    private List<GroupMenu> groupMenu = new ArrayList<>();

    @JsonView({ menu_portail_view.PortailView.class })
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();
    // ⭐ OBLIGATOIRE pour le routing frontoffice
    @JsonView({ menu_portail_view.PortailView.class })
    @Column(unique = true, nullable = false)
    private String code; // Ex: "TRANS_001", "IMP_001"

    // ⭐ OBLIGATOIRE pour activer/désactiver
    @JsonView({ menu_portail_view.PortailView.class })
    private Boolean actif = true;

    // ⭐ Utile pour l'interface
    private String description;
    private String logo;
    private String couleurTheme;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "is_valid")
    private Short isValid;

    public Short getIsValid() {
        return isValid;
    }

    public void setIsValid(Short isValid) {
        this.isValid = isValid;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Getters/Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCouleurTheme() {
        return couleurTheme;
    }

    public void setCouleurTheme(String couleurTheme) {
        this.couleurTheme = couleurTheme;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation = date_creation;
    }



    public void setRole(List<Role> role) {
        this.role = role;
    }

    public void setGroupMenu(List<GroupMenu> groupMenu) {
        this.groupMenu = groupMenu;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public LocalDateTime getDate_creation() {
        return date_creation;
    }



    public List<Role> getRole() {
        return role;
    }

    public List<GroupMenu> getGroupMenu() {
        return groupMenu;
    }
}
