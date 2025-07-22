package com.pfe.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pfe.backend.views.menu_portail_view;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Table(name = "menu")
// @JsonIdentityInfo(
// generator = ObjectIdGenerators.PropertyGenerator.class,
// property = "id")
public class Menu extends AbstractMenu {

    public Menu() {
        this.role = new ArrayList<>();
    }

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class,
            menu_portail_view.PortailView.class, menu_portail_view.getAllGroupMenu_to_frontend.class })
    private String url;

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class })
    @ManyToMany
    @JoinTable(name = "menu_role", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> role;

    @JsonView(menu_portail_view.withoutGroupMenu.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private GroupMenu groupeMenu;

    @JsonView({ menu_portail_view.withoutGroupMenu.class, menu_portail_view.MenuWithoutGroupMenu.class })
    @ManyToMany
    private List<Portail> portails;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Portail> getPortails() {
        return portails;
    }

    public void setPortails(List<Portail> portails) {
        this.portails = portails;
    }

    public String getUrl() {
        return url;
    }

    public List<Role> getRole() {
        return role;
    }

    public GroupMenu getGroupeMenu() {
        return groupeMenu;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public void setGroupeMenu(GroupMenu groupeMenu) {
        this.groupeMenu = groupeMenu;
    }
}
