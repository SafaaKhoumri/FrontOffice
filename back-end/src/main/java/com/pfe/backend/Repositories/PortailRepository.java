package com.pfe.backend.Repositories;

import com.pfe.backend.Entities.Menu;
import com.pfe.backend.Entities.Portail;
import com.pfe.backend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortailRepository extends JpaRepository<Portail, Long> {

    /**
     * Rechercher des portails par nom (insensible à la casse)
     */
    List<Portail> findByNomContainingIgnoreCase(String nom);

    List<Portail> findByRole(List<Role> role);

    /**
     * Rechercher des portails par utilisateur
     */
    List<Portail> findByUtilisateurId(Long utilisateurId);

    /**
     * Rechercher des portails par nom exact
     */
    Optional<Portail> findByNom(String nom);

    /**
     * Rechercher des portails créés après une certaine date
     */
    @Query("SELECT p FROM Portail p WHERE p.date_creation > :date")
    List<Portail> findByDateCreationAfter(@Param("date") LocalDateTime date);

    /**
     * Rechercher des portails créés entre deux dates
     */
    @Query("SELECT p FROM Portail p WHERE p.date_creation BETWEEN :startDate AND :endDate")
    List<Portail> findByDateCreationBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Rechercher des portails par utilisateur et nom
     */
    List<Portail> findByUtilisateurIdAndNomContainingIgnoreCase(Long utilisateurId, String nom);

    /**
     * Compter les portails par utilisateur
     */
    long countByUtilisateurId(Long utilisateurId);

    /**
     * Vérifier si un portail avec ce nom existe déjà
     */
    boolean existsByNom(String nom);

    /**
     * Vérifier si un portail avec ce nom existe déjà pour un autre ID
     */
    boolean existsByNomAndIdNot(String nom, Long id);

    /**
     * Rechercher des portails avec des rôles spécifiques
     */
    @Query("SELECT DISTINCT p FROM Portail p JOIN p.role r WHERE r.id = :roleId")
    List<Portail> findByRoleId(@Param("roleId") Long roleId);

    /**
     * Rechercher des portails avec un nombre minimum de rôles
     */
    @Query("SELECT p FROM Portail p WHERE SIZE(p.role) >= :minRoles")
    List<Portail> findPortailsWithMinimumRoles(@Param("minRoles") int minRoles);

    /**
     * Rechercher des portails avec un nombre minimum de menus
     */
    @Query("SELECT p FROM Portail p WHERE SIZE(p.menus) >= :minMenus")
    List<Portail> findPortailsWithMinimumMenus(@Param("minMenus") int minMenus);

    /**
     * Rechercher des portails ordonnés par date de création
     */
    @Query("SELECT p FROM Portail p ORDER BY p.date_creation DESC")
    List<Portail> findAllByOrderByDateCreationDesc();

    /**
     * Rechercher des portails ordonnés par nom
     */
    List<Portail> findAllByOrderByNomAsc();

    /**
     * Rechercher les portails les plus récents
     */
    @Query("SELECT p FROM Portail p ORDER BY p.date_creation DESC")
    List<Portail> findRecentPortails();

    /**
     * Rechercher des portails par utilisateur ordonnés par date de création
     */
    @Query("SELECT p FROM Portail p WHERE p.utilisateur.id = :utilisateurId ORDER BY p.date_creation DESC")
    List<Portail> findByUtilisateurIdOrderByDateCreationDesc(@Param("utilisateurId") Long utilisateurId);

    /**
     * Supprimer des portails par utilisateur
     */
    void deleteByUtilisateurId(Long utilisateurId);

    /**
     * Rechercher des portails avec un nom exact et un utilisateur spécifique
     */
    Optional<Portail> findByNomAndUtilisateurId(String nom, Long utilisateurId);

    // === MÉTHODES POUR LE FRONTOFFICE ===

    /**
     * Trouver un portail par code
     */
    Optional<Portail> findByCode(String code);

    /**
     * Trouver un portail par code et statut actif
     */
    Optional<Portail> findByCodeAndActif(String code, Boolean actif);


    @Query("SELECT p FROM Portail p JOIN p.role r WHERE r.nom = :nomRole AND p.actif = :actif")
    List<Portail> findByNomRoleAndActif(
            @Param("nomRole") String roleCode,
            @Param("actif") Boolean actif);
}