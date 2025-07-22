package com.pfe.backend.Service;

import com.pfe.backend.Entities.UtilisateurFrontoffice;
import com.pfe.backend.Repositories.UtilisateurFrontofficRepositiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurFrontofficRepositiry utilisateurRepo;

    /**
     * Récupérer un utilisateur par son ID
     * ✅ CORRIGÉ : Type de retour correct et gestion de l'Optional
     */
    public Optional<UtilisateurFrontoffice> getUtilisateur(Long id) {
        return utilisateurRepo.findById(id);
    }

    /**
     * Récupérer un utilisateur par son ID (avec exception si non trouvé)
     * ✅ ALTERNATIVE : Si vous voulez vraiment récupérer l'entité directement
     */
    public UtilisateurFrontoffice getUtilisateurById(Long id) {
        return utilisateurRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
    }

    /**
     * Récupérer un utilisateur par email
     */
    public Optional<UtilisateurFrontoffice> getUtilisateurByEmail(String email) {
        return utilisateurRepo.findByEmail(email);
    }

    /**
     * Sauvegarder un utilisateur
     */
    public UtilisateurFrontoffice saveUtilisateur(UtilisateurFrontoffice utilisateur) {
        return utilisateurRepo.save(utilisateur);
    }

    /**
     * Vérifier si un utilisateur existe
     */
    public boolean existsById(Long id) {
        return utilisateurRepo.existsById(id);
    }

    /**
     * Supprimer un utilisateur
     */
    public void deleteUtilisateur(Long id) {
        utilisateurRepo.deleteById(id);
    }
}