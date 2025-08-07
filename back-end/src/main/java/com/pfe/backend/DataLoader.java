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
public class DataLoader implements CommandLineRunner {
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
    @Override
    public void run(String... args) throws Exception {
        com.pfe.backend.Entities.Role impo = new com.pfe.backend.Entities.Role();
        impo.setNom("Importateur");
        roleRepository.save(impo);
        com.pfe.backend.Entities.Role expo = new com.pfe.backend.Entities.Role();
        expo.setNom("Exportateur");
        roleRepository.save(expo);
        com.pfe.backend.Entities.Role trans = new com.pfe.backend.Entities.Role();
        trans.setNom("Transitaire");
        roleRepository.save(trans);


        UtilisateurFrontoffice userTransitaire = new UtilisateurFrontoffice();

            userTransitaire.setEmail("ahmed.ben@transitaire.ma");
            userTransitaire.setPassword(passwordEncoder.encode("transitaire123"));
            userTransitaire.setNom("Ben Ahmed");
            userTransitaire.setPrenom("Ahmed");
            userTransitaire.setTelephone("0661234567");
            userTransitaire.setEntreprise("Transitaire Express SARL");
            userTransitaire.setRole(roleRepository.findById(3L).get());
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
            userExportateur.setRole(roleRepository.findById(3L).get());
            userExportateur.setActif(true);
            utilisateurFrontofficRepositiry.save(userExportateur);
            System.out.println("✅ Utilisateur exportateur créé");


        Portail portailTransitaire = null;
        Optional<Portail> existingPortailTransitaire = portailRepo.findByCode("TRANS_001");
        if (existingPortailTransitaire.isEmpty()) {
            portailTransitaire = new Portail();
            portailTransitaire.setCode("TRANS_001");
            portailTransitaire.setNom("Portail Transitaires");
            portailTransitaire.setDescription("Portail dédié aux opérateurs de transit");
            portailTransitaire.setLogo("/assets/logos/transitaire.png");
            portailTransitaire.setCouleurTheme("#3B82F6");
            portailTransitaire.setActif(true);
            portailTransitaire.setDate_creation(LocalDateTime.now());
            portailTransitaire.setUtilisateur(userTransitaire);
            portailTransitaire.getRole().add(roleRepository.findById(3L).get());
            portailTransitaire = portailRepo.save(portailTransitaire);
            System.out.println("✅ Portail transitaire créé");
        } else {
            portailTransitaire = existingPortailTransitaire.get();
            System.out.println("ℹ️ Portail transitaire existe déjà");
        }

        // Portail Importateurs
        Portail portailImportateur = null;
        Optional<Portail> existingPortailImportateur = portailRepo.findByCode("IMP_001");
        if (existingPortailImportateur.isEmpty()) {
            portailImportateur = new Portail();
            portailImportateur.setCode("IMP_001");
            portailImportateur.setNom("Portail Importateurs");
            portailImportateur.setDescription("Portail dédié aux importateurs");
            portailImportateur.setLogo("/assets/logos/importateur.png");
            portailImportateur.setCouleurTheme("#10B981");
            portailImportateur.setActif(true);
            portailImportateur.setDate_creation(LocalDateTime.now());
            portailImportateur.setUtilisateur(userTransitaire);
            portailImportateur.getRole().add(roleRepository.findById(1L).get());
            portailImportateur = portailRepo.save(portailImportateur);
            System.out.println("✅ Portail importateur créé");
        } else {
            portailImportateur = existingPortailImportateur.get();
            System.out.println("ℹ️ Portail importateur existe déjà");
        }

        // Portail Exportateurs
        Portail portailExportateur = null;
        Optional<Portail> existingPortailExportateur = portailRepo.findByCode("EXP_001");
        if (existingPortailExportateur.isEmpty()) {
            portailExportateur = new Portail();
            portailExportateur.setCode("EXP_001");
            portailExportateur.setNom("Portail Exportateurs");
            portailExportateur.setDescription("Portail dédié aux exportateurs");
            portailExportateur.setLogo("/assets/logos/exportateur.png");
            portailExportateur.setCouleurTheme("#8B5CF6");
            portailExportateur.setActif(true);
            portailExportateur.setDate_creation(LocalDateTime.now());
            portailExportateur.setUtilisateur(userExportateur);
            portailExportateur.getRole().add(roleRepository.findById(2L).get());
            portailExportateur = portailRepo.save(portailExportateur);
            System.out.println("✅ Portail exportateur créé");
        } else {
            portailExportateur = existingPortailExportateur.get();
            System.out.println("ℹ️ Portail exportateur existe déjà");
        }

        // ⭐ ÉTAPE 5: Créer les groupes de menus pour le portail Transitaire
        GroupMenu groupDeclarations = new GroupMenu();
        groupDeclarations.setTitle("Déclarations Transit");
        groupDeclarations.setIcone("file-text");
        groupDeclarations.setIsActive(true);
        groupDeclarations.setParent(null);
        groupDeclarations.setMenuOrder(1);
        groupDeclarations.getRole().add(roleRepository.findById(3L).get());
        groupDeclarations.setSous_menus(new ArrayList<>());
        groupDeclarations = groupMenuRepo.save(groupDeclarations);
        portailTransitaire.getGroupMenu().add(groupDeclarations);
        portailRepo.save(portailTransitaire);
        System.out.println("✅ Groupe déclarations créé");



        GroupMenu amine = new GroupMenu();
        amine.setTitle("Amine");  // 🔥 Utiliser 'amine' au lieu de 'groupDeclarations'
        amine.setIcone("file-text");
        amine.setIsActive(true);
        amine.setParent(groupDeclarations);  // 🔥 Parent = groupDeclarations
        amine.setMenuOrder(1);
        amine.getRole().add(roleRepository.findById(3L).get());
        amine.setSous_menus(new ArrayList<>());
        amine = groupMenuRepo.save(amine);  // 🔥 Sauver 'amine'

        groupDeclarations.getSous_menus().add(amine);
        groupMenuRepo.save(groupDeclarations);
        System.out.println("✅ Sous-groupe Amine créé");



            GroupMenu groupSuivi = new GroupMenu();

            groupSuivi.setTitle("Suivi & Contrôle");
            groupSuivi.setIcone("truck");
            groupSuivi.setIsActive(true);
            groupSuivi.setParent(null);
            groupSuivi.setMenuOrder(2);
            groupSuivi.getRole().add(roleRepository.findById(3L).get());
            groupSuivi.setSous_menus(new ArrayList<>());
            groupSuivi = groupMenuRepo.save(groupSuivi);
            System.out.println("✅ Groupe suivi créé");
            portailTransitaire.getGroupMenu().add(groupSuivi);
            portailRepo.save(portailTransitaire);


        // ⭐ ÉTAPE 6: Créer les menus pour Transitaires avec LIAISONS BIDIRECTIONNELLES
        // ⭐ ÉTAPE 6: Créer les menus pour Transitaires avec LIAISONS CORRECTES


        // Création des menus
        Menu menuNouvelleDeclTransit = new Menu();
        menuNouvelleDeclTransit.setTitle("Nouvelle Déclaration");
        menuNouvelleDeclTransit.setIcone("plus-circle");
        menuNouvelleDeclTransit.setIsActive(true);
        menuNouvelleDeclTransit.setUrl("https://www.google.com");
        menuNouvelleDeclTransit.setMenuOrder(1);
        menuNouvelleDeclTransit.getRole().add(roleRepository.findById(3L).get());
        menuNouvelleDeclTransit.setGroupeMenu(amine);  // 🔥 Lier à 'amine'
        menuNouvelleDeclTransit = menuRepo.save(menuNouvelleDeclTransit);

        // 🔥 Ajouter le menu à 'amine' (pas à 'groupDeclarations')
        amine.getSous_menus().add(menuNouvelleDeclTransit);
        groupMenuRepo.save(amine);
        portailTransitaire.getMenus().add(menuNouvelleDeclTransit);
        portailRepo.save(portailTransitaire);
        System.out.println("✅ Menu nouvelle déclaration créé et lié au groupe Amine");


            Menu menuListeDeclTransit = new Menu();
            menuListeDeclTransit.setTitle("Mes Déclarations");
            menuListeDeclTransit.setIcone("list");
            menuListeDeclTransit.setIsActive(true);
            menuListeDeclTransit.setUrl("/transitaire/declarations/liste");
            menuListeDeclTransit.setMenuOrder(2);
            menuListeDeclTransit.getRole().add(roleRepository.findById(3L).get());
            menuListeDeclTransit.setGroupeMenu(amine);
            menuListeDeclTransit = menuRepo.save(menuListeDeclTransit);
            amine.getSous_menus().add(menuListeDeclTransit);
            groupMenuRepo.save(amine);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE

            System.out.println("✅ Menu liste déclarations créé et lié");
            portailTransitaire.getMenus().add(menuListeDeclTransit);
            portailRepo.save(portailTransitaire);


            Menu menuSuiviCargaisons = new Menu();
            menuSuiviCargaisons.setTitle("Suivi Cargaisons");
            menuSuiviCargaisons.setIcone("map-pin");
            menuSuiviCargaisons.setIsActive(true);
            menuSuiviCargaisons.setUrl("/transitaire/suivi/cargaisons");
            menuSuiviCargaisons.setMenuOrder(1);
            menuSuiviCargaisons.getRole().add(roleRepository.findById(3L).get());
            menuSuiviCargaisons.setGroupeMenu(groupSuivi);
            menuSuiviCargaisons = menuRepo.save(menuSuiviCargaisons);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupSuivi.getSous_menus().add(menuSuiviCargaisons);
            groupMenuRepo.save(groupSuivi);
            System.out.println("✅ Menu suivi cargaisons créé et lié");
            portailTransitaire.getMenus().add(menuSuiviCargaisons);
            portailRepo.save(portailTransitaire);


        // ⭐ ÉTAPE 5.1: Lier les groupes au portail Transitaire

        if (!portailTransitaire.getGroupMenu().contains(groupSuivi)) {
            portailTransitaire.getGroupMenu().add(groupSuivi);
        }
        portailRepo.save(portailTransitaire);
        System.out.println("✅ Groupes liés au portail transitaire");

        // ⭐ ÉTAPE 7: Créer les groupes de menus pour le portail Importateur

            GroupMenu groupImports = new GroupMenu();
            groupImports.setTitle("Gestion Imports");
            groupImports.setIcone("ship");
            groupImports.setIsActive(true);
            groupImports.setParent(null);
            groupImports.setMenuOrder(1);
            groupImports.getRole().add(roleRepository.findById(1L).get());
            groupImports.setSous_menus(new ArrayList<>());
            groupImports = groupMenuRepo.save(groupImports);
            portailImportateur.getGroupMenu().add(groupImports);
            portailRepo.save(portailImportateur);
            System.out.println("✅ Groupe imports créé");


            GroupMenu groupDocuments = new GroupMenu();
            groupDocuments.setTitle("Documents");
            groupDocuments.setIcone("file-text");
            groupDocuments.setIsActive(true);
            groupDocuments.setParent(null);
            groupDocuments.setMenuOrder(2);
            groupDocuments.getRole().add(roleRepository.findById(1L).get());
            groupDocuments.setSous_menus(new ArrayList<>());
            groupDocuments = groupMenuRepo.save(groupDocuments);
            portailImportateur.getGroupMenu().add(groupDocuments);
            portailRepo.save(portailImportateur);
            System.out.println("✅ Groupe documents créé");

            Menu menuNouvelImport = new Menu();
            menuNouvelImport.setTitle("Nouvelle Déclaration");
            menuNouvelImport.setIcone("plus-circle");
            menuNouvelImport.setIsActive(true);
            menuNouvelImport.setUrl("/importateur/imports/nouveau");
            menuNouvelImport.setMenuOrder(1);
            menuNouvelImport.getRole().add(roleRepository.findById(1L).get());
            menuNouvelImport.setGroupeMenu(groupImports);
            menuNouvelImport = menuRepo.save(menuNouvelImport);
            portailImportateur.getMenus().add(menuNouvelImport);


            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupImports.getSous_menus().add(menuNouvelImport);
            groupMenuRepo.save(groupImports);
            portailImportateur.getGroupMenu().add(groupImports);
            portailRepo.save(portailImportateur);
            System.out.println("✅ Menu nouvel import créé et lié");


            Menu menuListeImports = new Menu();
            menuListeImports.setTitle("Mes Imports");
            menuListeImports.setIcone("package");
            menuListeImports.setIsActive(true);
            menuListeImports.setUrl("/importateur/imports/liste");
            menuListeImports.setMenuOrder(2);
            menuListeImports.getRole().add(roleRepository.findById(1L).get());
            menuListeImports.setGroupeMenu(groupImports);
            menuListeImports = menuRepo.save(menuListeImports);
            portailImportateur.getMenus().add(menuListeImports);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupImports.getSous_menus().add(menuListeImports);
            groupMenuRepo.save(groupImports);
            portailImportateur.getGroupMenu().add(groupImports);
            portailRepo.save(portailImportateur);
            System.out.println("✅ Menu liste imports créé et lié");


            Menu menuUploadDocs = new Menu();
            menuUploadDocs.setTitle("Télécharger Documents");
            menuUploadDocs.setIcone("upload");
            menuUploadDocs.setIsActive(true);
            menuUploadDocs.setUrl("/importateur/documents/upload");
            menuUploadDocs.setMenuOrder(1);
            menuUploadDocs.getRole().add(roleRepository.findById(1L).get());
            menuUploadDocs.setGroupeMenu(groupDocuments);
            menuUploadDocs = menuRepo.save(menuUploadDocs);
            portailImportateur.getMenus().add(menuUploadDocs);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupDocuments.getSous_menus().add(menuUploadDocs);
            groupMenuRepo.save(groupDocuments);
            portailImportateur.getGroupMenu().add(groupDocuments);
            portailRepo.save(portailImportateur);
            System.out.println("✅ Menu upload documents créé et lié");


        // ⭐ ÉTAPE 7.1: Lier les groupes au portail Importateur
        if (!portailImportateur.getGroupMenu().contains(groupImports)) {
            portailImportateur.getGroupMenu().add(groupImports);
        }
        if (!portailImportateur.getGroupMenu().contains(groupDocuments)) {
            portailImportateur.getGroupMenu().add(groupDocuments);
        }
        System.out.println("✅ Groupes liés au portail importateur");

            GroupMenu groupExports = new GroupMenu();
            groupExports.setTitle("Gestion Exports");
            groupExports.setIcone("plane");
            groupExports.setIsActive(true);
            groupExports.setParent(null);
            groupExports.setMenuOrder(1);
            groupExports.getRole().add(roleRepository.findById(2L).get());
            groupExports.setSous_menus(new ArrayList<>());
            groupExports = groupMenuRepo.save(groupExports);
            System.out.println("✅ Groupe exports créé");


            Menu menuNouvelExport = new Menu();
            menuNouvelExport.setTitle("Nouvelle Déclaration Export");
            menuNouvelExport.setIcone("plus-circle");
            menuNouvelExport.setIsActive(true);
            menuNouvelExport.setUrl("/exportateur/exports/nouveau");
            menuNouvelExport.setMenuOrder(1);
            menuNouvelExport.getRole().add(roleRepository.findById(2L).get());
            menuNouvelExport.setGroupeMenu(groupExports);
            menuNouvelExport = menuRepo.save(menuNouvelExport);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupExports.getSous_menus().add(menuNouvelExport);
            groupMenuRepo.save(groupExports);
            System.out.println("✅ Menu nouvel export créé et lié");

        // Lier le groupe au portail Exportateur
        if (!portailExportateur.getGroupMenu().contains(groupExports)) {
            portailExportateur.getGroupMenu().add(groupExports);
        }
        portailRepo.save(portailExportateur);


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