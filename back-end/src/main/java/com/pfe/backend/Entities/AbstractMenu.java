package com.pfe.backend.Entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.pfe.backend.views.menu_portail_view;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Menu.class, name = "menu"),
        @JsonSubTypes.Type(value = GroupMenu.class, name = "groupMenu") })
public abstract class AbstractMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class,
            menu_portail_view.PortailView.class, menu_portail_view.getAllGroupMenu_to_frontend.class })
    private Long id;



    // ⭐ Utile pour l'interface
    private String icone; // Icon CSS class ou URL
    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    @ManyToOne
    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class,
            menu_portail_view.getAllGroupMenu_to_frontend.class })
    private UtilisateurFrontoffice creePar;

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class,
            menu_portail_view.getAllGroupMenu_to_frontend.class })
    private Integer menuOrder;

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class,
            menu_portail_view.PortailView.class, menu_portail_view.getAllGroupMenu_to_frontend.class })
    private String title;

    public Long getId() {
        return id;
    }

    public UtilisateurFrontoffice getCreePar() {
        return creePar;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreePar(UtilisateurFrontoffice cree_par) {
        this.creePar = cree_par;
    }

    public AbstractMenu setMenuOrder(Integer menu_order) {
        this.menuOrder = menu_order;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
