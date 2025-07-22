package com.pfe.backend.Repositories;

import com.pfe.backend.Entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // ✅ CORRECTION : Utiliser le bon nom de propriété
    List<Menu> findByGroupeMenuId(Long groupeMenuId); // ✅ CORRECT (groupeMenu)

    // ⭐ AJOUT NÉCESSAIRE pour le DataLoader
    Optional<Menu> findByCode(String code);

    // Méthodes existantes
    Menu findByMenuOrder(int order);

    List<Menu> findByActifTrue();
}