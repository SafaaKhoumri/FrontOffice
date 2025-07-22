package com.pfe.backend.Entities;

import com.fasterxml.jackson.annotation.*;
import com.pfe.backend.views.menu_portail_view;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity

@AllArgsConstructor

@Table(name = "group_menu")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GroupMenu extends AbstractMenu {
    @JsonView({ menu_portail_view.MenuWithoutGroupMenu.class, menu_portail_view.getAllGroupMenu_to_frontend.class })
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "sous_menus" }) // Ignore ces propriétés dans le
                                                                                   // parent
    private GroupMenu parent;

    @JsonView(menu_portail_view.MenuWithoutGroupMenu.class)
    @ManyToMany
    @Column(nullable = true)
    private List<Role> role = new ArrayList<>();

    @JsonView({ menu_portail_view.MenuWithoutGroupMenu.class, menu_portail_view.getAllGroupMenu_to_frontend.class })
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "groupe_menu_sous_menus",

            joinColumns = @JoinColumn(name = "groupe_menu_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)), inverseJoinColumns = @JoinColumn(name = "sous_menu_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))

    )
    private List<AbstractMenu> sous_menus = new ArrayList<>();

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupMenu getParent() {
        return parent;
    }

    public List<Role> getRole() {
        return role;
    }

    public List<AbstractMenu> getSous_menus() {
        return sous_menus;
    }

    public void setParent(GroupMenu parent) {
        this.parent = parent;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public void setSous_menus(List<AbstractMenu> sous_menus) {
        this.sous_menus = sous_menus;
    }

    public GroupMenu() {
    }
}
