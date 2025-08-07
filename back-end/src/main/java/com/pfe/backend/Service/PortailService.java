package com.pfe.backend.Service;

import com.pfe.backend.Entities.Portail;
import com.pfe.backend.Entities.Role;
import com.pfe.backend.Repositories.PortailRepository;
import com.pfe.backend.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PortailService {

    @Autowired
    private PortailRepository portailRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(PortailService.class);

    /**
     * Sauvegarder un portail (création ou mise à jour)
     */
    public Portail savePortail(Portail portail) {
        try {
            logger.info("Sauvegarde du portail: {}", portail.getNom());
            return portailRepository.save(portail);
        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde du portail: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la sauvegarde du portail", e);
        }
    }

    /**
     * Récupérer un portail par son ID
     */
    public Portail getPortail(Long id) {
        return portailRepository.findById(id).get();
    }

    /**
     * Récupérer tous les portails
     */
    public List<Portail> getAllPortail() {
        try {
            logger.info("Récupération de tous les portails");
            return portailRepository.findAll();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les portails: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des portails", e);
        }
    }

    /**
     * Mettre à jour un portail existant - VERSION CORRIGÉE
     */
    @Transactional
    public Portail updatePortail(Long id, Portail portailDetails) {
        try {
            logger.info("Mise à jour du portail avec l'ID: {}", id);

            Optional<Portail> optionalPortail = portailRepository.findById(id);
            if (optionalPortail.isEmpty()) {
                throw new RuntimeException("Portail non trouvé avec l'ID: " + id);
            }

            Portail existingPortail = optionalPortail.get();
            logger.info("Portail existant trouvé: {}", existingPortail.getNom());

            // Mettre à jour les champs modifiables
            if (portailDetails.getNom() != null && !portailDetails.getNom().trim().isEmpty()) {
                logger.info("Mise à jour du nom: {} -> {}", existingPortail.getNom(), portailDetails.getNom());
                existingPortail.setNom(portailDetails.getNom().trim());
            }

            if (portailDetails.getRole() != null && !portailDetails.getRole().isEmpty()) {
                logger.info("Mise à jour des rôles: {} rôles fournis", portailDetails.getRole().size());

                // Clear existing roles and set new ones
                existingPortail.getRole().clear();
                existingPortail.setRole(portailDetails.getRole());

                // Log les nouveaux rôles
                for (Role role : portailDetails.getRole()) {
                    logger.info("Nouveau rôle ajouté: ID={}, Nom={}", role.getId(), role.getNom());
                }
            }

            // Forcer la sauvegarde avec flush pour s'assurer de la persistance
            Portail updatedPortail = portailRepository.saveAndFlush(existingPortail);
            logger.info("Portail mis à jour avec succès: ID={}, Nom={}, Nombre de rôles={}",
                    updatedPortail.getId(), updatedPortail.getNom(), updatedPortail.getRole().size());

            return updatedPortail;
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du portail avec l'ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la mise à jour du portail: " + e.getMessage(), e);
        }
    }

    /**
     * Supprimer un portail par son ID
     */
    public boolean deletePortail(Long id) {
        try {
            logger.info("Suppression du portail avec l'ID: {}", id);

            if (!portailRepository.existsById(id)) {
                logger.warn("Tentative de suppression d'un portail inexistant: {}", id);
                return false;
            }

            portailRepository.deleteById(id);
            logger.info("Portail supprimé avec succès: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du portail avec l'ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du portail", e);
        }
    }

    /**
     * Vérifier si un portail existe par son ID
     */
    public boolean existsById(Long id) {
        try {
            return portailRepository.existsById(id);
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence du portail avec l'ID {}: {}", id,
                    e.getMessage());
            return false;
        }
    }

    /**
     * Récupérer les portails par utilisateur
     */
    public List<Portail> getPortailsByUser(Long userId) {
        try {
            logger.info("Récupération des portails pour l'utilisateur: {}", userId);
            return portailRepository.findByUtilisateurId(userId);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des portails pour l'utilisateur {}: {}", userId,
                    e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des portails de l'utilisateur", e);
        }
    }

    /**
     * Rechercher des portails par nom (recherche partielle)
     */
    public List<Portail> searchPortailsByName(String name) {
        try {
            logger.info("Recherche de portails avec le nom contenant: {}", name);
            return portailRepository.findByNomContainingIgnoreCase(name);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de portails par nom {}: {}", name, e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche de portails", e);
        }
    }

    /**
     * Compter le nombre total de portails
     */
    public long countPortails() {
        try {
            return portailRepository.count();
        } catch (Exception e) {
            logger.error("Erreur lors du comptage des portails: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Vérifier si l'utilisateur peut modifier/supprimer le portail
     */
    public boolean canUserModifyPortail(Long portailId, Long userId) {
        try {
            Optional<Portail> portailOpt = portailRepository.findById(portailId);
            if (portailOpt.isEmpty()) {
                return false;
            }

            Portail portail = portailOpt.get();
            // Vérifier si l'utilisateur est le créateur du portail
            return portail.getUtilisateur().getId().equals(userId);
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification des permissions: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Vérifier si un nom de portail existe déjà (pour éviter les doublons)
     */
    public boolean existsByNom(String nom) {
        return portailRepository.existsByNom(nom);
    }

    /**
     * Vérifier si un nom de portail existe déjà pour un autre portail
     */
    public boolean existsByNomAndIdNot(String nom, Long id) {
        return portailRepository.existsByNomAndIdNot(nom, id);
    }
}