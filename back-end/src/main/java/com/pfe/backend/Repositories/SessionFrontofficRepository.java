package com.pfe.backend.Repositories;

import com.pfe.backend.Entities.SessionFrontoffice;
import com.pfe.backend.Entities.UtilisateurFrontoffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionFrontofficRepository extends JpaRepository<SessionFrontoffice, Long> {

    /**
     * Trouver une session par token et statut actif
     */
    Optional<SessionFrontoffice> findByTokenAndActif(String token, Boolean actif);

    /**
     * Trouver une session par token
     */
    Optional<SessionFrontoffice> findByToken(String token);

    /**
     * Trouver toutes les sessions actives d'un utilisateur
     */
    @Query("SELECT s FROM SessionFrontoffice s WHERE s.utilisateur = :utilisateur AND s.actif = true")
    List<SessionFrontoffice> findActiveSessionsByUser(@Param("utilisateur") UtilisateurFrontoffice utilisateur);

    /**
     * Supprimer les sessions expirées
     */
    @Query("DELETE FROM SessionFrontoffice s WHERE s.dateExpiration < :now")
    void deleteExpiredSessions(@Param("now") LocalDateTime now);

    /**
     * Désactiver toutes les sessions d'un utilisateur
     */
    @Query("UPDATE SessionFrontoffice s SET s.actif = false WHERE s.utilisateur.id = :utilisateurId")
    void deactivateAllUserSessions(@Param("utilisateurId") Long utilisateurId);

    /**
     * Compter les sessions actives d'un utilisateur
     */
    @Query("SELECT COUNT(s) FROM SessionFrontoffice s WHERE s.utilisateur.id = :utilisateurId AND s.actif = true")
    long countActiveSessionsByUserId(@Param("utilisateurId") Long utilisateurId);

    /**
     * Trouver les sessions expirées
     */
    @Query("SELECT s FROM SessionFrontoffice s WHERE s.dateExpiration < :now AND s.actif = true")
    List<SessionFrontoffice> findExpiredSessions(@Param("now") LocalDateTime now);
}