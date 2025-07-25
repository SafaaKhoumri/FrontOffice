package com.pfe.backend;

import com.pfe.backend.Entities.*;
import com.pfe.backend.Repositories.*;
import com.pfe.backend.Service.MenuService;
import com.pfe.backend.auth.AuthenticationService;
import com.pfe.backend.user.Role;
import com.pfe.backend.user.User;
import com.pfe.backend.user.UserRepository;
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
    UserRepository utilisateurRepo;
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

    @Override
    public void run(String... args) throws Exception {

        // ⭐ ÉTAPE 1: Créer un utilisateur admin pour le backoffice
        User utilisateur = null;
        Optional<User> existingAdmin = utilisateurRepo.findByEmail("safaa@portnet.ma");
        if (existingAdmin.isEmpty()) {
            utilisateur = new User();
            utilisateur.setFullname("Safaa Khoumri");
            utilisateur.setCin("BB212365");
            utilisateur.setEmail("safaa@portnet.ma");
            utilisateur.setMatricule("ADMIN002");
            utilisateur.setPassword(passwordEncoder.encode("admin123"));
            utilisateur.setRole(Role.ADMIN);
            utilisateur = utilisateurRepo.save(utilisateur);
            System.out.println("✅ Utilisateur admin créé");
        } else {
            utilisateur = existingAdmin.get();
            System.out.println("ℹ️ Utilisateur admin existe déjà");
        }

        // ⭐ ÉTAPE 2: Créer les rôles métier avec CODES
        com.pfe.backend.Entities.Role roleTransitaire = null;
        Optional<com.pfe.backend.Entities.Role> existingTransitaire = roleRepository.findByCode("TRANSITAIRE");
        if (existingTransitaire.isEmpty()) {
            roleTransitaire = new com.pfe.backend.Entities.Role();
            roleTransitaire.setCode("TRANSITAIRE");
            roleTransitaire.setNom_Role("Transitaire");
            roleTransitaire.setActif(true);
            roleTransitaire.setDescription("Gestionnaire des opérations de transit");
            roleTransitaire = roleRepository.save(roleTransitaire);
            System.out.println("✅ Rôle TRANSITAIRE créé");
        } else {
            roleTransitaire = existingTransitaire.get();
            System.out.println("ℹ️ Rôle TRANSITAIRE existe déjà");
        }

        com.pfe.backend.Entities.Role roleImportateur = null;
        Optional<com.pfe.backend.Entities.Role> existingImportateur = roleRepository.findByCode("IMPORTATEUR");
        if (existingImportateur.isEmpty()) {
            roleImportateur = new com.pfe.backend.Entities.Role();
            roleImportateur.setCode("IMPORTATEUR");
            roleImportateur.setNom_Role("Importateur");
            roleImportateur.setActif(true);
            roleImportateur.setDescription("Gestionnaire des opérations d'import");
            roleImportateur = roleRepository.save(roleImportateur);
            System.out.println("✅ Rôle IMPORTATEUR créé");
        } else {
            roleImportateur = existingImportateur.get();
            System.out.println("ℹ️ Rôle IMPORTATEUR existe déjà");
        }

        com.pfe.backend.Entities.Role roleExportateur = null;
        Optional<com.pfe.backend.Entities.Role> existingExportateur = roleRepository.findByCode("EXPORTATEUR");
        if (existingExportateur.isEmpty()) {
            roleExportateur = new com.pfe.backend.Entities.Role();
            roleExportateur.setCode("EXPORTATEUR");
            roleExportateur.setNom_Role("Exportateur");
            roleExportateur.setActif(true);
            roleExportateur.setDescription("Gestionnaire des opérations d'export");
            roleExportateur = roleRepository.save(roleExportateur);
            System.out.println("✅ Rôle EXPORTATEUR créé");
        } else {
            roleExportateur = existingExportateur.get();
            System.out.println("ℹ️ Rôle EXPORTATEUR existe déjà");
        }

        // ⭐ ÉTAPE 3: Créer des utilisateurs frontoffice pour chaque rôle
        // Utilisateur Transitaire
        Optional<UtilisateurFrontoffice> existingUserTransitaire = utilisateurFrontofficRepositiry
                .findByEmail("ahmed.ben@transitaire.ma");
        if (existingUserTransitaire.isEmpty()) {
            UtilisateurFrontoffice userTransitaire = new UtilisateurFrontoffice();
            userTransitaire.setEmail("ahmed.ben@transitaire.ma");
            userTransitaire.setPassword(passwordEncoder.encode("transitaire123"));
            userTransitaire.setNom("Ben Ahmed");
            userTransitaire.setPrenom("Ahmed");
            userTransitaire.setTelephone("0661234567");
            userTransitaire.setEntreprise("Transitaire Express SARL");
            userTransitaire.setRole(roleTransitaire);
            userTransitaire.setActif(true);
            utilisateurFrontofficRepositiry.save(userTransitaire);
            System.out.println("✅ Utilisateur transitaire créé");
        } else {
            System.out.println("ℹ️ Utilisateur transitaire existe déjà");
        }

        // Utilisateur Importateur
        Optional<UtilisateurFrontoffice> existingUserImportateur = utilisateurFrontofficRepositiry
                .findByEmail("fatima.alami@import.ma");
        if (existingUserImportateur.isEmpty()) {
            UtilisateurFrontoffice userImportateur = new UtilisateurFrontoffice();
            userImportateur.setEmail("fatima.alami@import.ma");
            userImportateur.setPassword(passwordEncoder.encode("import123"));
            userImportateur.setNom("Alami");
            userImportateur.setPrenom("Fatima");
            userImportateur.setTelephone("0671234567");
            userImportateur.setEntreprise("Import Solutions SA");
            userImportateur.setRole(roleImportateur);
            userImportateur.setActif(true);
            utilisateurFrontofficRepositiry.save(userImportateur);
            System.out.println("✅ Utilisateur importateur créé");
        } else {
            System.out.println("ℹ️ Utilisateur importateur existe déjà");
        }

        // Utilisateur Exportateur
        Optional<UtilisateurFrontoffice> existingUserExportateur = utilisateurFrontofficRepositiry
                .findByEmail("youssef.tazi@export.ma");
        if (existingUserExportateur.isEmpty()) {
            UtilisateurFrontoffice userExportateur = new UtilisateurFrontoffice();
            userExportateur.setEmail("youssef.tazi@export.ma");
            userExportateur.setPassword(passwordEncoder.encode("export123"));
            userExportateur.setNom("Tazi");
            userExportateur.setPrenom("Youssef");
            userExportateur.setTelephone("0681234567");
            userExportateur.setEntreprise("Export Morocco Ltd");
            userExportateur.setRole(roleExportateur);
            userExportateur.setActif(true);
            utilisateurFrontofficRepositiry.save(userExportateur);
            System.out.println("✅ Utilisateur exportateur créé");
        } else {
            System.out.println("ℹ️ Utilisateur exportateur existe déjà");
        }

        // ⭐ ÉTAPE 4: Créer les portails avec CODES
        // Portail Transitaires
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
            portailTransitaire.setUtilisateur(utilisateur);
            portailTransitaire.getRole().add(roleTransitaire);
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
            portailImportateur.setUtilisateur(utilisateur);
            portailImportateur.getRole().add(roleImportateur);
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
            portailExportateur.setUtilisateur(utilisateur);
            portailExportateur.getRole().add(roleExportateur);
            portailExportateur = portailRepo.save(portailExportateur);
            System.out.println("✅ Portail exportateur créé");
        } else {
            portailExportateur = existingPortailExportateur.get();
            System.out.println("ℹ️ Portail exportateur existe déjà");
        }

        // ⭐ ÉTAPE 5: Créer les groupes de menus pour le portail Transitaire
        GroupMenu groupDeclarations = null;
        Optional<GroupMenu> existingGroupDeclarations = groupMenuRepo.findByCode("GRP_DECL_TRANS");
        if (existingGroupDeclarations.isEmpty()) {
            groupDeclarations = new GroupMenu();
            groupDeclarations.setCode("GRP_DECL_TRANS");
            groupDeclarations.setTitle("Déclarations Transit");
            groupDeclarations.setIcone("file-text");
            groupDeclarations.setActif(true);
            groupDeclarations.setParent(null);
            groupDeclarations.setCreePar(utilisateur);
            groupDeclarations.setMenuOrder(1);
            groupDeclarations.getRole().add(roleTransitaire);
            groupDeclarations.setSous_menus(new ArrayList<>());
            groupDeclarations = groupMenuRepo.save(groupDeclarations);
            System.out.println("✅ Groupe déclarations créé");
        } else {
            groupDeclarations = existingGroupDeclarations.get();
            System.out.println("ℹ️ Groupe déclarations existe déjà");
        }

        GroupMenu groupSuivi = null;
        Optional<GroupMenu> existingGroupSuivi = groupMenuRepo.findByCode("GRP_SUIVI_TRANS");
        if (existingGroupSuivi.isEmpty()) {
            groupSuivi = new GroupMenu();
            groupSuivi.setCode("GRP_SUIVI_TRANS");
            groupSuivi.setTitle("Suivi & Contrôle");
            groupSuivi.setIcone("truck");
            groupSuivi.setActif(true);
            groupSuivi.setParent(null);
            groupSuivi.setCreePar(utilisateur);
            groupSuivi.setMenuOrder(2);
            groupSuivi.getRole().add(roleTransitaire);
            groupSuivi.setSous_menus(new ArrayList<>());
            groupSuivi = groupMenuRepo.save(groupSuivi);
            System.out.println("✅ Groupe suivi créé");
        } else {
            groupSuivi = existingGroupSuivi.get();
            System.out.println("ℹ️ Groupe suivi existe déjà");
        }

        // ⭐ ÉTAPE 6: Créer les menus pour Transitaires avec LIAISONS BIDIRECTIONNELLES
        // ⭐ ÉTAPE 6: Créer les menus pour Transitaires avec LIAISONS CORRECTES
        Menu menuNouvelleDeclTransit = null;
        Optional<Menu> existingMenuNouvelleDeclTransit = menuRepo.findByCode("MENU_NEW_DECL_TRANS");
        if (existingMenuNouvelleDeclTransit.isEmpty()) {
            menuNouvelleDeclTransit = new Menu();
            menuNouvelleDeclTransit.setCode("MENU_NEW_DECL_TRANS");
            menuNouvelleDeclTransit.setTitle("Nouvelle Déclaration");
            menuNouvelleDeclTransit.setIcone("plus-circle");
            menuNouvelleDeclTransit.setActif(true);
            menuNouvelleDeclTransit.setUrl("https://www.google.com");
            menuNouvelleDeclTransit.setMenuOrder(1);
            menuNouvelleDeclTransit.setCreePar(utilisateur);
            menuNouvelleDeclTransit.getRole().add(roleTransitaire);
            menuNouvelleDeclTransit.setGroupeMenu(groupDeclarations);
            menuNouvelleDeclTransit = menuRepo.save(menuNouvelleDeclTransit);

            // ✅ LIAISON BIDIRECTIONNELLE CRITIQUE
            // RECHARGEZ le groupe depuis la DB pour éviter les conflits
            groupDeclarations = groupMenuRepo.findById(groupDeclarations.getId()).get();
            groupDeclarations.getSous_menus().add(menuNouvelleDeclTransit);
            groupMenuRepo.save(groupDeclarations);
            System.out.println("✅ Menu nouvelle déclaration créé et lié au groupe");
        } else {
            menuNouvelleDeclTransit = existingMenuNouvelleDeclTransit.get();
            // ⭐ AJOUTEZ CETTE LIGNE POUR FORCER LA MISE À JOUR
            menuNouvelleDeclTransit.setUrl("https://www.google.com");
            menuRepo.save(menuNouvelleDeclTransit);
            System.out.println("ℹ️ Menu nouvelle déclaration existe déjà - URL mise à jour");
        }
        Menu menuListeDeclTransit = null;
        Optional<Menu> existingMenuListeDeclTransit = menuRepo.findByCode("MENU_LIST_DECL_TRANS");
        if (existingMenuListeDeclTransit.isEmpty()) {
            menuListeDeclTransit = new Menu();
            menuListeDeclTransit.setCode("MENU_LIST_DECL_TRANS");
            menuListeDeclTransit.setTitle("Mes Déclarations");
            menuListeDeclTransit.setIcone("list");
            menuListeDeclTransit.setActif(true);
            menuListeDeclTransit.setUrl("/transitaire/declarations/liste");
            menuListeDeclTransit.setMenuOrder(2);
            menuListeDeclTransit.setCreePar(utilisateur);
            menuListeDeclTransit.getRole().add(roleTransitaire);
            menuListeDeclTransit.setGroupeMenu(groupDeclarations);
            menuListeDeclTransit = menuRepo.save(menuListeDeclTransit);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupDeclarations.getSous_menus().add(menuListeDeclTransit);
            groupMenuRepo.save(groupDeclarations);
            System.out.println("✅ Menu liste déclarations créé et lié");
        } else {
            menuListeDeclTransit = existingMenuListeDeclTransit.get();
            System.out.println("ℹ️ Menu liste déclarations existe déjà");
        }

        Menu menuSuiviCargaisons = null;
        Optional<Menu> existingMenuSuiviCargaisons = menuRepo.findByCode("MENU_SUIVI_CARGAISONS");
        if (existingMenuSuiviCargaisons.isEmpty()) {
            menuSuiviCargaisons = new Menu();
            menuSuiviCargaisons.setCode("MENU_SUIVI_CARGAISONS");
            menuSuiviCargaisons.setTitle("Suivi Cargaisons");
            menuSuiviCargaisons.setIcone("map-pin");
            menuSuiviCargaisons.setActif(true);
            menuSuiviCargaisons.setUrl("/transitaire/suivi/cargaisons");
            menuSuiviCargaisons.setMenuOrder(1);
            menuSuiviCargaisons.setCreePar(utilisateur);
            menuSuiviCargaisons.getRole().add(roleTransitaire);
            menuSuiviCargaisons.setGroupeMenu(groupSuivi);
            menuSuiviCargaisons = menuRepo.save(menuSuiviCargaisons);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupSuivi.getSous_menus().add(menuSuiviCargaisons);
            groupMenuRepo.save(groupSuivi);
            System.out.println("✅ Menu suivi cargaisons créé et lié");
        } else {
            menuSuiviCargaisons = existingMenuSuiviCargaisons.get();
            System.out.println("ℹ️ Menu suivi cargaisons existe déjà");
        }

        // ⭐ ÉTAPE 5.1: Lier les groupes au portail Transitaire
        if (!portailTransitaire.getGroupMenu().contains(groupDeclarations)) {
            portailTransitaire.getGroupMenu().add(groupDeclarations);
        }
        if (!portailTransitaire.getGroupMenu().contains(groupSuivi)) {
            portailTransitaire.getGroupMenu().add(groupSuivi);
        }
        portailRepo.save(portailTransitaire);
        System.out.println("✅ Groupes liés au portail transitaire");

        // ⭐ ÉTAPE 7: Créer les groupes de menus pour le portail Importateur
        GroupMenu groupImports = null;
        Optional<GroupMenu> existingGroupImports = groupMenuRepo.findByCode("GRP_IMPORTS");
        if (existingGroupImports.isEmpty()) {
            groupImports = new GroupMenu();
            groupImports.setCode("GRP_IMPORTS");
            groupImports.setTitle("Gestion Imports");
            groupImports.setIcone("ship");
            groupImports.setActif(true);
            groupImports.setParent(null);
            groupImports.setCreePar(utilisateur);
            groupImports.setMenuOrder(1);
            groupImports.getRole().add(roleImportateur);
            groupImports.setSous_menus(new ArrayList<>());
            groupImports = groupMenuRepo.save(groupImports);
            System.out.println("✅ Groupe imports créé");
        } else {
            groupImports = existingGroupImports.get();
            System.out.println("ℹ️ Groupe imports existe déjà");
        }

        GroupMenu groupDocuments = null;
        Optional<GroupMenu> existingGroupDocuments = groupMenuRepo.findByCode("GRP_DOCUMENTS");
        if (existingGroupDocuments.isEmpty()) {
            groupDocuments = new GroupMenu();
            groupDocuments.setCode("GRP_DOCUMENTS");
            groupDocuments.setTitle("Documents");
            groupDocuments.setIcone("file-text");
            groupDocuments.setActif(true);
            groupDocuments.setParent(null);
            groupDocuments.setCreePar(utilisateur);
            groupDocuments.setMenuOrder(2);
            groupDocuments.getRole().add(roleImportateur);
            groupDocuments.setSous_menus(new ArrayList<>());
            groupDocuments = groupMenuRepo.save(groupDocuments);
            System.out.println("✅ Groupe documents créé");
        } else {
            groupDocuments = existingGroupDocuments.get();
            System.out.println("ℹ️ Groupe documents existe déjà");
        }

        // ⭐ ÉTAPE 8: Créer les menus pour Importateurs avec LIAISONS BIDIRECTIONNELLES
        Menu menuNouvelImport = null;
        Optional<Menu> existingMenuNouvelImport = menuRepo.findByCode("MENU_NEW_IMPORT");
        if (existingMenuNouvelImport.isEmpty()) {
            menuNouvelImport = new Menu();
            menuNouvelImport.setCode("MENU_NEW_IMPORT");
            menuNouvelImport.setTitle("Nouvelle Déclaration");
            menuNouvelImport.setIcone("plus-circle");
            menuNouvelImport.setActif(true);
            menuNouvelImport.setUrl("/importateur/imports/nouveau");
            menuNouvelImport.setMenuOrder(1);
            menuNouvelImport.setCreePar(utilisateur);
            menuNouvelImport.getRole().add(roleImportateur);
            menuNouvelImport.setGroupeMenu(groupImports);
            menuNouvelImport = menuRepo.save(menuNouvelImport);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupImports.getSous_menus().add(menuNouvelImport);
            groupMenuRepo.save(groupImports);
            System.out.println("✅ Menu nouvel import créé et lié");
        } else {
            menuNouvelImport = existingMenuNouvelImport.get();
            System.out.println("ℹ️ Menu nouvel import existe déjà");
        }

        Menu menuListeImports = null;
        Optional<Menu> existingMenuListeImports = menuRepo.findByCode("MENU_LIST_IMPORTS");
        if (existingMenuListeImports.isEmpty()) {
            menuListeImports = new Menu();
            menuListeImports.setCode("MENU_LIST_IMPORTS");
            menuListeImports.setTitle("Mes Imports");
            menuListeImports.setIcone("package");
            menuListeImports.setActif(true);
            menuListeImports.setUrl("/importateur/imports/liste");
            menuListeImports.setMenuOrder(2);
            menuListeImports.setCreePar(utilisateur);
            menuListeImports.getRole().add(roleImportateur);
            menuListeImports.setGroupeMenu(groupImports);
            menuListeImports = menuRepo.save(menuListeImports);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupImports.getSous_menus().add(menuListeImports);
            groupMenuRepo.save(groupImports);
            System.out.println("✅ Menu liste imports créé et lié");
        } else {
            menuListeImports = existingMenuListeImports.get();
            System.out.println("ℹ️ Menu liste imports existe déjà");
        }

        Menu menuUploadDocs = null;
        Optional<Menu> existingMenuUploadDocs = menuRepo.findByCode("MENU_UPLOAD_DOCS");
        if (existingMenuUploadDocs.isEmpty()) {
            menuUploadDocs = new Menu();
            menuUploadDocs.setCode("MENU_UPLOAD_DOCS");
            menuUploadDocs.setTitle("Télécharger Documents");
            menuUploadDocs.setIcone("upload");
            menuUploadDocs.setActif(true);
            menuUploadDocs.setUrl("/importateur/documents/upload");
            menuUploadDocs.setMenuOrder(1);
            menuUploadDocs.setCreePar(utilisateur);
            menuUploadDocs.getRole().add(roleImportateur);
            menuUploadDocs.setGroupeMenu(groupDocuments);
            menuUploadDocs = menuRepo.save(menuUploadDocs);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupDocuments.getSous_menus().add(menuUploadDocs);
            groupMenuRepo.save(groupDocuments);
            System.out.println("✅ Menu upload documents créé et lié");
        } else {
            menuUploadDocs = existingMenuUploadDocs.get();
            System.out.println("ℹ️ Menu upload documents existe déjà");
        }

        // ⭐ ÉTAPE 7.1: Lier les groupes au portail Importateur
        if (!portailImportateur.getGroupMenu().contains(groupImports)) {
            portailImportateur.getGroupMenu().add(groupImports);
        }
        if (!portailImportateur.getGroupMenu().contains(groupDocuments)) {
            portailImportateur.getGroupMenu().add(groupDocuments);
        }
        portailRepo.save(portailImportateur);
        System.out.println("✅ Groupes liés au portail importateur");

        // ⭐ ÉTAPE 9: Créer quelques groupes pour le portail Exportateur
        GroupMenu groupExports = null;
        Optional<GroupMenu> existingGroupExports = groupMenuRepo.findByCode("GRP_EXPORTS");
        if (existingGroupExports.isEmpty()) {
            groupExports = new GroupMenu();
            groupExports.setCode("GRP_EXPORTS");
            groupExports.setTitle("Gestion Exports");
            groupExports.setIcone("plane");
            groupExports.setActif(true);
            groupExports.setParent(null);
            groupExports.setCreePar(utilisateur);
            groupExports.setMenuOrder(1);
            groupExports.getRole().add(roleExportateur);
            groupExports.setSous_menus(new ArrayList<>());
            groupExports = groupMenuRepo.save(groupExports);
            System.out.println("✅ Groupe exports créé");
        } else {
            groupExports = existingGroupExports.get();
            System.out.println("ℹ️ Groupe exports existe déjà");
        }

        // Menu simple pour Exportateurs avec LIAISONS BIDIRECTIONNELLES
        Menu menuNouvelExport = null;
        Optional<Menu> existingMenuNouvelExport = menuRepo.findByCode("MENU_NEW_EXPORT");
        if (existingMenuNouvelExport.isEmpty()) {
            menuNouvelExport = new Menu();
            menuNouvelExport.setCode("MENU_NEW_EXPORT");
            menuNouvelExport.setTitle("Nouvelle Déclaration Export");
            menuNouvelExport.setIcone("plus-circle");
            menuNouvelExport.setActif(true);
            menuNouvelExport.setUrl("/exportateur/exports/nouveau");
            menuNouvelExport.setMenuOrder(1);
            menuNouvelExport.setCreePar(utilisateur);
            menuNouvelExport.getRole().add(roleExportateur);
            menuNouvelExport.setGroupeMenu(groupExports);
            menuNouvelExport = menuRepo.save(menuNouvelExport);

            // ✅ LIAISON BIDIRECTIONNELLE CRUCIALE
            groupExports.getSous_menus().add(menuNouvelExport);
            groupMenuRepo.save(groupExports);
            System.out.println("✅ Menu nouvel export créé et lié");
        } else {
            menuNouvelExport = existingMenuNouvelExport.get();
            System.out.println("ℹ️ Menu nouvel export existe déjà");
        }

        // Lier le groupe au portail Exportateur
        if (!portailExportateur.getGroupMenu().contains(groupExports)) {
            portailExportateur.getGroupMenu().add(groupExports);
        }
        portailRepo.save(portailExportateur);
        // ⭐ AJOUTEZ CETTE SECTION DANS VOTRE DATALOADER
        // Après l'étape 7.1 (liaison des groupes au portail importateur)

        // ⭐ ÉTAPE 10: Créer des MENUS DIRECTS (sans groupe parent)

        // 📊 Menu direct DASHBOARD pour Transitaires
        Menu menuDashboardTransitaire = null;
        Optional<Menu> existingDashboardTransitaire = menuRepo.findByCode("MENU_DASHBOARD_TRANS");
        if (existingDashboardTransitaire.isEmpty()) {
            menuDashboardTransitaire = new Menu();
            menuDashboardTransitaire.setCode("MENU_DASHBOARD_TRANS");
            menuDashboardTransitaire.setTitle("Tableau de Bord");
            menuDashboardTransitaire.setIcone("home");
            menuDashboardTransitaire.setActif(true);
            menuDashboardTransitaire.setUrl("/transitaire/dashboard");
            menuDashboardTransitaire.setMenuOrder(0); // Premier dans l'ordre
            menuDashboardTransitaire.setCreePar(utilisateur);
            menuDashboardTransitaire.getRole().add(roleTransitaire);
            // ✅ PAS DE GROUPE PARENT = MENU DIRECT
            menuDashboardTransitaire.setGroupeMenu(null);
            menuDashboardTransitaire = menuRepo.save(menuDashboardTransitaire);

            // ✅ Lier le menu DIRECTEMENT au portail
            if (menuDashboardTransitaire.getPortails() == null) {
                menuDashboardTransitaire.setPortails(new ArrayList<>());
            }
            menuDashboardTransitaire.getPortails().add(portailTransitaire);
            menuRepo.save(menuDashboardTransitaire);

            System.out.println("✅ Menu direct Dashboard Transitaire créé");
        } else {
            menuDashboardTransitaire = existingDashboardTransitaire.get();
            System.out.println("ℹ️ Menu direct Dashboard Transitaire existe déjà");
        }

        // 📊 Menu direct RAPPORTS pour Transitaires
        Menu menuRapportsTransitaire = null;
        Optional<Menu> existingRapportsTransitaire = menuRepo.findByCode("MENU_REPORTS_TRANS");
        if (existingRapportsTransitaire.isEmpty()) {
            menuRapportsTransitaire = new Menu();
            menuRapportsTransitaire.setCode("MENU_REPORTS_TRANS");
            menuRapportsTransitaire.setTitle("Rapports");
            menuRapportsTransitaire.setIcone("bar-chart-3");
            menuRapportsTransitaire.setActif(true);
            menuRapportsTransitaire.setUrl("/transitaire/rapports");
            menuRapportsTransitaire.setMenuOrder(99); // Dernier dans l'ordre
            menuRapportsTransitaire.setCreePar(utilisateur);
            menuRapportsTransitaire.getRole().add(roleTransitaire);
            // ✅ PAS DE GROUPE PARENT = MENU DIRECT
            menuRapportsTransitaire.setGroupeMenu(null);
            menuRapportsTransitaire = menuRepo.save(menuRapportsTransitaire);

            // ✅ Lier le menu DIRECTEMENT au portail
            if (menuRapportsTransitaire.getPortails() == null) {
                menuRapportsTransitaire.setPortails(new ArrayList<>());
            }
            menuRapportsTransitaire.getPortails().add(portailTransitaire);
            menuRepo.save(menuRapportsTransitaire);

            System.out.println("✅ Menu direct Rapports Transitaire créé");
        } else {
            menuRapportsTransitaire = existingRapportsTransitaire.get();
            System.out.println("ℹ️ Menu direct Rapports Transitaire existe déjà");
        }

        // 📊 Menu direct PARAMÈTRES pour Importateurs
        Menu menuParametresImportateur = null;
        Optional<Menu> existingParametresImportateur = menuRepo.findByCode("MENU_SETTINGS_IMP");
        if (existingParametresImportateur.isEmpty()) {
            menuParametresImportateur = new Menu();
            menuParametresImportateur.setCode("MENU_SETTINGS_IMP");
            menuParametresImportateur.setTitle("Paramètres");
            menuParametresImportateur.setIcone("settings");
            menuParametresImportateur.setActif(true);
            menuParametresImportateur.setUrl("/importateur/parametres");
            menuParametresImportateur.setMenuOrder(98);
            menuParametresImportateur.setCreePar(utilisateur);
            menuParametresImportateur.getRole().add(roleImportateur);
            // ✅ PAS DE GROUPE PARENT = MENU DIRECT
            menuParametresImportateur.setGroupeMenu(null);
            menuParametresImportateur = menuRepo.save(menuParametresImportateur);

            // ✅ Lier le menu DIRECTEMENT au portail
            if (menuParametresImportateur.getPortails() == null) {
                menuParametresImportateur.setPortails(new ArrayList<>());
            }
            menuParametresImportateur.getPortails().add(portailImportateur);
            menuRepo.save(menuParametresImportateur);

            System.out.println("✅ Menu direct Paramètres Importateur créé");
        } else {
            menuParametresImportateur = existingParametresImportateur.get();
            System.out.println("ℹ️ Menu direct Paramètres Importateur existe déjà");
        }

        // 📊 Menu direct AIDE pour tous les portails
        Menu menuAide = null;
        Optional<Menu> existingAide = menuRepo.findByCode("MENU_HELP_GLOBAL");
        if (existingAide.isEmpty()) {
            menuAide = new Menu();
            menuAide.setCode("MENU_HELP_GLOBAL");
            menuAide.setTitle("Aide & Support");
            menuAide.setIcone("help-circle");
            menuAide.setActif(true);
            menuAide.setUrl("/aide");
            menuAide.setMenuOrder(100); // Tout à la fin
            menuAide.setCreePar(utilisateur);
            // ✅ Accessible par TOUS les rôles
            menuAide.getRole().add(roleTransitaire);
            menuAide.getRole().add(roleImportateur);
            menuAide.getRole().add(roleExportateur);
            // ✅ PAS DE GROUPE PARENT = MENU DIRECT
            menuAide.setGroupeMenu(null);
            menuAide = menuRepo.save(menuAide);

            // ✅ Lier le menu à TOUS les portails
            if (menuAide.getPortails() == null) {
                menuAide.setPortails(new ArrayList<>());
            }
            menuAide.getPortails().add(portailTransitaire);
            menuAide.getPortails().add(portailImportateur);
            menuAide.getPortails().add(portailExportateur);
            menuRepo.save(menuAide);

            System.out.println("✅ Menu direct Aide créé pour tous les portails");
        } else {
            menuAide = existingAide.get();
            System.out.println("ℹ️ Menu direct Aide existe déjà");
        }

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