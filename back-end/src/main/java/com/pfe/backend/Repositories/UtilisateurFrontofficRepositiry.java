package com.pfe.backend.Repositories;

import com.pfe.backend.Entities.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtilisateurFrontofficRepositiry extends JpaRepository<UtilisateurFrontoffice, Long> {

    // ⭐ MÉTHODES EXISTANTES
    Optional<UtilisateurFrontoffice> findByEmail(String email);

    Optional<UtilisateurFrontoffice> findById(Long id);

    // ⭐ NOUVELLES MÉTHODES NÉCESSAIRES POUR L'AUTHENTIFICATION

    /**
     * Trouver un utilisateur par email et statut actif
     */
    @Query("SELECT u FROM UtilisateurFrontoffice u WHERE u.email = :email AND u.actif = :actif")
    Optional<UtilisateurFrontoffice> findByEmailAndActif(@Param("email") String email, @Param("actif") Boolean actif);

    /**
     * Vérifier si un utilisateur existe par email
     */
    boolean existsByEmail(String email);

    /**
     * Trouver tous les utilisateurs actifs
     */
    @Query("SELECT u FROM UtilisateurFrontoffice u WHERE u.actif = true")
    java.util.List<UtilisateurFrontoffice> findAllActive();

    /**
     * Trouver des utilisateurs par rôle
     */
    @Query("SELECT u FROM UtilisateurFrontoffice u WHERE u.role.code = :roleCode AND u.actif = true")
    java.util.List<UtilisateurFrontoffice> findByRoleCodeAndActif(@Param("roleCode") String roleCode);
}