package com.pfe.backend.Controllers;

import com.pfe.backend.Entities.*;
import com.pfe.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/frontoffice/portail")
public class PortailFrontofficController {

    @Autowired
    private PortailRepository portailRepository;

    @Autowired
    private SessionFrontofficRepository sessionRepository;

    @Autowired
    private GroupMenuRepo groupMenuRepo;

    @Autowired
    private MenuRepository menuRepo;

    // DTO pour la réponse du portail
    public static class PortailResponse {
        private String code;
        private String nom;
        private String description;
        private String couleurTheme;
        private boolean actif;
        private List<GroupMenuDTO> groupMenu;

        // Constructeurs
        public PortailResponse() {
        }

        public PortailResponse(Portail portail) {
            this.code = portail.getCode();
            this.nom = portail.getNom();
            this.description = portail.getDescription();
            this.couleurTheme = portail.getCouleurTheme();
            this.actif = portail.getActif();

            if (portail.getGroupMenu() != null) {
                this.groupMenu = portail.getGroupMenu().stream()
                        .filter(gm -> gm.getActif()) // Seulement les groupes actifs
                        .map(GroupMenuDTO::new)
                        .collect(Collectors.toList());
            }
        }

        // Getters/Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCouleurTheme() {
            return couleurTheme;
        }

        public void setCouleurTheme(String couleurTheme) {
            this.couleurTheme = couleurTheme;
        }

        public boolean isActif() {
            return actif;
        }

        public void setActif(boolean actif) {
            this.actif = actif;
        }

        public List<GroupMenuDTO> getGroupMenu() {
            return groupMenu;
        }

        public void setGroupMenu(List<GroupMenuDTO> groupMenu) {
            this.groupMenu = groupMenu;
        }
    }

    // DTO pour les groupes de menus
    public static class GroupMenuDTO {
        private Long id;
        private String title;
        private String icone;
        private String description;
        private boolean actif;
        private List<MenuDTO> sous_menus;

        public GroupMenuDTO() {
        }

        public GroupMenuDTO(GroupMenu groupMenu) {
            this.id = groupMenu.getId();
            this.title = groupMenu.getTitle();
            this.icone = groupMenu.getIcone();
            this.description = groupMenu.getDescription();
            this.actif = groupMenu.getActif();

            if (groupMenu.getSous_menus() != null) {
                this.sous_menus = groupMenu.getSous_menus().stream()
                        .filter(menu -> menu.getActif()) // Seulement les menus actifs
                        .map(abstractMenu -> {
                            if (abstractMenu instanceof Menu) {
                                return new MenuDTO((Menu) abstractMenu);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }

        // Getters/Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcone() {
            return icone;
        }

        public void setIcone(String icone) {
            this.icone = icone;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isActif() {
            return actif;
        }

        public void setActif(boolean actif) {
            this.actif = actif;
        }

        public List<MenuDTO> getSous_menus() {
            return sous_menus;
        }

        public void setSous_menus(List<MenuDTO> sous_menus) {
            this.sous_menus = sous_menus;
        }
    }

    // DTO pour les menus
    public static class MenuDTO {
        private Long id;
        private String title;
        private String icone;
        private String code;
        private String url;
        private String description;
        private boolean actif;

        public MenuDTO() {
        }

        public MenuDTO(Menu menu) {
            this.id = menu.getId();
            this.title = menu.getTitle();
            this.icone = menu.getIcone();
            this.code = menu.getCode();
            this.url = menu.getUrl();
            this.description = menu.getDescription();
            this.actif = menu.getActif();
        }

        // Getters/Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcone() {
            return icone;
        }

        public void setIcone(String icone) {
            this.icone = icone;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isActif() {
            return actif;
        }

        public void setActif(boolean actif) {
            this.actif = actif;
        }
    }

    // ✅ REMPLACEZ LA MÉTHODE getPortailByCode DANS VOTRE
    // PortailFrontofficController

    // ✅ REMPLACEZ LA MÉTHODE getPortailByCode DANS VOTRE
    // PortailFrontofficController

    @GetMapping("/{portailCode}")
    public ResponseEntity<?> getPortailByCode(
            @PathVariable String portailCode,
            @RequestHeader("Authorization") String token) {

        try {
            System.out.println("🔍 [FRONTOFFICE] Recherche du portail avec code: " + portailCode);
            System.out.println("🔑 [FRONTOFFICE] Token reçu: " + token);

            // Test 1: Vérifier la session
            Optional<SessionFrontoffice> sessionOpt = sessionRepository.findByTokenAndActif(token, true);
            if (!sessionOpt.isPresent()) {
                System.out.println("❌ [FRONTOFFICE] Session non trouvée");
                return ResponseEntity.status(401).body("Session non trouvée");
            }

            SessionFrontoffice session = sessionOpt.get();
            UtilisateurFrontoffice user = session.getUtilisateur();
            System.out.println(
                    "✅ [FRONTOFFICE] Utilisateur trouvé: " + user.getEmail() + ", Rôle: " + user.getRole().getCode());

            // Test 2: Chercher le portail par code simple
            Optional<Portail> portailOpt = portailRepository.findByCode(portailCode);
            if (!portailOpt.isPresent()) {
                System.out.println("❌ [FRONTOFFICE] Portail non trouvé avec le code: " + portailCode);
                return ResponseEntity.status(404).body("Portail non trouvé avec le code: " + portailCode);
            }

            Portail portail = portailOpt.get();
            System.out.println("✅ [FRONTOFFICE] Portail trouvé: " + portail.getNom());

            // Test 3: Vérifier l'accès
            boolean hasAccess = portail.getRole().stream()
                    .anyMatch(role -> role.getCode().equals(user.getRole().getCode()));

            if (!hasAccess) {
                System.out.println("❌ [FRONTOFFICE] Accès refusé pour le rôle: " + user.getRole().getCode());
                return ResponseEntity.status(403).body("Accès refusé");
            }

            System.out.println("✅ [FRONTOFFICE] Accès autorisé");

            // Construire la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("code", portail.getCode() != null ? portail.getCode() : portailCode);
            response.put("nom", portail.getNom());
            response.put("description",
                    portail.getDescription() != null ? portail.getDescription() : "Description par défaut");
            response.put("couleurTheme", portail.getCouleurTheme() != null ? portail.getCouleurTheme() : "#3b82f6");
            response.put("actif", portail.getActif() != null ? portail.getActif() : true);

            // ⭐ NOUVEAU : Récupérer les groupes avec leurs menus
            List<Map<String, Object>> groupMenuList = new ArrayList<>();

            List<GroupMenu> groupes = groupMenuRepo.findAll().stream()
                    .filter(g -> g.getActif() && g.getParent() == null)
                    .collect(Collectors.toList());

            for (GroupMenu group : groupes) {
                boolean groupBelongsToPortail = portail.getGroupMenu().stream()
                        .anyMatch(pg -> pg.getId().equals(group.getId()));

                if (groupBelongsToPortail) {
                    Map<String, Object> groupMap = new HashMap<>();
                    groupMap.put("id", group.getId());
                    groupMap.put("nom", group.getTitle());
                    groupMap.put("title", group.getTitle());
                    groupMap.put("icone", group.getIcone());
                    groupMap.put("actif", group.getActif());

                    List<Menu> menusInGroup = menuRepo.findByGroupeMenuId(group.getId());

                    List<Map<String, Object>> menusList = new ArrayList<>();
                    for (Menu menu : menusInGroup) {
                        if (menu.getActif()) {
                            Map<String, Object> menuMap = new HashMap<>();
                            menuMap.put("id", menu.getId());
                            menuMap.put("nom", menu.getTitle());
                            menuMap.put("title", menu.getTitle());
                            menuMap.put("code", menu.getCode());
                            menuMap.put("url", menu.getUrl());
                            menuMap.put("icone", menu.getIcone());
                            menuMap.put("actif", menu.getActif());
                            menusList.add(menuMap);
                        }
                    }

                    groupMap.put("menus", menusList);
                    groupMenuList.add(groupMap);

                    System.out.println("✅ Groupe " + group.getTitle() + " : " + menusList.size() + " menus chargés");
                }
            }

            response.put("groupMenu", groupMenuList);

            // ⭐ NOUVEAU : Récupérer les MENUS DIRECTS (sans groupe parent)
            List<Map<String, Object>> menusDirectsList = new ArrayList<>();

            // Récupérer tous les menus sans groupe parent pour ce rôle
            List<Menu> menusDirects = menuRepo.findAll().stream()
                    .filter(menu -> menu.getActif() &&
                            menu.getGroupeMenu() == null && // Pas de groupe parent
                            menu.getRole().stream().anyMatch(role -> role.getCode().equals(user.getRole().getCode())) && // Rôle
                                                                                                                         // correct
                            menu.getPortails().stream().anyMatch(p -> p.getCode().equals(portailCode))) // Portail
                                                                                                        // correct
                    .sorted((m1, m2) -> Integer.compare(m1.getMenuOrder() != null ? m1.getMenuOrder() : 999,
                            m2.getMenuOrder() != null ? m2.getMenuOrder() : 999)) // Tri par ordre
                    .collect(Collectors.toList());

            for (Menu menu : menusDirects) {
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("id", menu.getId());
                menuMap.put("nom", menu.getTitle());
                menuMap.put("title", menu.getTitle());
                menuMap.put("code", menu.getCode());
                menuMap.put("url", menu.getUrl());
                menuMap.put("icone", menu.getIcone());
                menuMap.put("actif", menu.getActif());
                menuMap.put("order", menu.getMenuOrder());
                menusDirectsList.add(menuMap);
            }

            response.put("menus", menusDirectsList); // ⭐ NOUVEAU CHAMP
            response.put("success", true);

            System.out.println("✅ [FRONTOFFICE] Réponse construite avec " + groupMenuList.size() + " groupes et "
                    + menusDirectsList.size() + " menus directs");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ [FRONTOFFICE] Erreur: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/user-portails")
    public ResponseEntity<List<PortailResponse>> getUserPortails(
            @RequestHeader("Authorization") String token) {

        try {
            // Vérifier la session utilisateur
            Optional<SessionFrontoffice> sessionOpt = sessionRepository.findByTokenAndActif(token, true);

            if (!sessionOpt.isPresent()) {
                return ResponseEntity.status(401).build();
            }

            SessionFrontoffice session = sessionOpt.get();
            UtilisateurFrontoffice user = session.getUtilisateur();

            // Récupérer tous les portails accessibles par le rôle de l'utilisateur
            List<Portail> portails = portailRepository.findByRole_CodeAndActif(
                    user.getRole().getCode(), true);

            List<PortailResponse> response = portails.stream()
                    .map(PortailResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/fix-menu-links")
    public ResponseEntity<String> fixMenuLinks() {
        try {
            // Récupérer les groupes
            GroupMenu groupDeclarations = groupMenuRepo.findByCode("GRP_DECL_TRANS").get();
            GroupMenu groupSuivi = groupMenuRepo.findByCode("GRP_SUIVI_TRANS").get();

            // Récupérer les menus
            Menu menuNewDecl = menuRepo.findByCode("MENU_NEW_DECL_TRANS").get();
            Menu menuListDecl = menuRepo.findByCode("MENU_LIST_DECL_TRANS").get();
            Menu menuSuivi = menuRepo.findByCode("MENU_SUIVI_CARGAISONS").get();

            // Vider et recréer les liaisons
            groupDeclarations.getSous_menus().clear();
            groupDeclarations.getSous_menus().add(menuNewDecl);
            groupDeclarations.getSous_menus().add(menuListDecl);
            groupMenuRepo.save(groupDeclarations);

            groupSuivi.getSous_menus().clear();
            groupSuivi.getSous_menus().add(menuSuivi);
            groupMenuRepo.save(groupSuivi);

            return ResponseEntity.ok("Liaisons corrigées !");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }
}