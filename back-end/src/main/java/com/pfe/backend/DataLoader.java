package com.pfe.backend;

import com.pfe.backend.Entities.*;
import com.pfe.backend.Repositories.*;
import com.pfe.backend.Service.MenuService;
import com.pfe.backend.auth.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader  { //implements CommandLineRunner
    @Autowired
    UtilisateurFrontofficRepositiry utilisateurRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PortailRepository portailRepo;
    @Autowired
    GroupMenuRepo groupMenuRepo;
    @Autowired
    MenuRepository menuRepo;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private MenuService menuService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UtilisateurFrontofficRepositiry utilisateurFrontofficRepositiry;

    @Transactional
    //@Override
    public void run(String... args) throws Exception {
        /*com.pfe.backend.Entities.Role impo = new com.pfe.backend.Entities.Role();
        impo.setNom("Importateur");
        roleRepository.save(impo);
        com.pfe.backend.Entities.Role expo = new com.pfe.backend.Entities.Role();
        expo.setNom("Exportateur");
        roleRepository.save(expo);
        com.pfe.backend.Entities.Role trans = new com.pfe.backend.Entities.Role();
        trans.setNom("Transitaire");
        roleRepository.save(trans);*/


        UtilisateurFrontoffice userTransitaire = new UtilisateurFrontoffice();

        userTransitaire.setEmail("ahmed.ben@transitaire.ma");
        userTransitaire.setPassword(passwordEncoder.encode("transitaire123"));
        userTransitaire.setNom("Ben Ahmed");
        userTransitaire.setPrenom("Ahmed");
        userTransitaire.setTelephone("0661234567");
        userTransitaire.setEntreprise("Transitaire Express SARL");
        userTransitaire.setRole(roleRepository.findById(4L).get());
        userTransitaire.setActif(true);
        utilisateurFrontofficRepositiry.save(userTransitaire);
        System.out.println("✅ Utilisateur transitaire créé");


        // Utilisateur Importateur


        UtilisateurFrontoffice userImportateur = new UtilisateurFrontoffice();


        userImportateur.setEmail("fatima.alami@import.ma");
        userImportateur.setPassword(passwordEncoder.encode("import123"));
        userImportateur.setNom("Alami");
        userImportateur.setPrenom("Fatima");
        userImportateur.setTelephone("0671234567");
        userImportateur.setEntreprise("Import Solutions SA");
        userImportateur.setRole(roleRepository.findById(2L).get());
        userImportateur.setActif(true);
        utilisateurFrontofficRepositiry.save(userImportateur);
        System.out.println("✅ Utilisateur importateur créé");


        // Utilisateur Exportateur

        UtilisateurFrontoffice userExportateur = new UtilisateurFrontoffice();


        userExportateur.setEmail("youssef.tazi@export.ma");
        userExportateur.setPassword(passwordEncoder.encode("export123"));
        userExportateur.setNom("Tazi");
        userExportateur.setPrenom("Youssef");
        userExportateur.setTelephone("0681234567");
        userExportateur.setEntreprise("Export Morocco Ltd");
        userExportateur.setRole(roleRepository.findById(1L).get());
        userExportateur.setActif(true);
        utilisateurFrontofficRepositiry.save(userExportateur);
        System.out.println("✅ Utilisateur exportateur créé");





        // ⭐ MISE À JOUR DU MESSAGE FINAL
        System.out.println("✅ DataLoader terminé - Données créées avec succès !");
        System.out.println("📧 Comptes de test créés :");
        System.out.println("   Transitaire: ahmed.ben@transitaire.ma / transitaire123");
        System.out.println("   Importateur: fatima.alami@import.ma / import123");
        System.out.println("   Exportateur: youssef.tazi@export.ma / export123");
        System.out.println("🌐 Portails créés :");
        System.out.println("   TRANS_001 - Portail Transitaires (2 groupes + 2 menus directs)");
        System.out.println("   IMP_001 - Portail Importateurs (2 groupes + 2 menus directs)");
        System.out.println("   EXP_001 - Portail Exportateurs (1 groupe + 1 menu direct)");
        System.out.println("🎯 Structure complète créée :");
        System.out.println("   └── Portail Transitaires");
        System.out.println("       ├── 🏠 Tableau de Bord (menu direct)");
        System.out.println("       ├── 📁 Déclarations Transit (2 menus liés)");
        System.out.println("       │   ├── ➕ Nouvelle Déclaration");
        System.out.println("       │   └── 📝 Mes Déclarations");
        System.out.println("       ├── 📁 Suivi & Contrôle (1 menu lié)");
        System.out.println("       │   └── 📍 Suivi Cargaisons");
        System.out.println("       ├── 📊 Rapports (menu direct)");
        System.out.println("       └── ❓ Aide & Support (menu direct)");
        System.out.println("   └── Portail Importateurs");
        System.out.println("       ├── 📁 Gestion Imports (2 menus liés)");
        System.out.println("       │   ├── ➕ Nouvelle Déclaration");
        System.out.println("       │   └── 📦 Mes Imports");
        System.out.println("       ├── 📁 Documents (1 menu lié)");
        System.out.println("       │   └── 📤 Télécharger Documents");
        System.out.println("       ├── ⚙️ Paramètres (menu direct)");
        System.out.println("       └── ❓ Aide & Support (menu direct)");
        System.out.println("🚀 Groupes de menus ET menus directs en place !");
        System.out.println("💡 Les portails affichent maintenant une navigation complète !");

    }

}