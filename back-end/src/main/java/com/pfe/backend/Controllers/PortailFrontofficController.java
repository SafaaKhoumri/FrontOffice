package com.pfe.backend.Controllers;

import com.pfe.backend.Entities.*;
import com.pfe.backend.Repositories.*;
import com.pfe.backend.auth.AuthenticationService;
import org.apache.catalina.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.HasControls;
import javax.swing.*;
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

    private final AuthenticationService service;

    public PortailFrontofficController(AuthenticationService service) {
        this.service = service;
    }

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
                        .filter(gm -> gm.getIsActive()) // Seulement les groupes actifs
                        .map(GroupMenuDTO::new)
                        .collect(Collectors.toList());
            }
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
            this.actif = groupMenu.getIsActive();

            if (groupMenu.getSous_menus() != null) {
                this.sous_menus = groupMenu.getSous_menus().stream()
                        .filter(menu -> menu.getIsActive()) // Seulement les menus actifs
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
            this.url = menu.getUrl();
            this.description = menu.getDescription();
            this.actif = menu.getIsActive();
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

        public String getNom() {
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

    private Map<Object, Object> recursiveSousMenu(Long groupMenuId) {
        AbstractMenu item;
        Map<Object, Object> menuMap = new HashMap<>();
        try {
            item = groupMenuRepo.findById(groupMenuId).get();
        }catch (Exception e) {
            item = menuRepo.findById(groupMenuId).get();

        }
        if (item instanceof GroupMenu) {
            GroupMenu groupMenu = (GroupMenu) item;

            menuMap.put("id", item.getId());
            menuMap.put("nom", item.getTitle());
            menuMap.put("title", item.getTitle());
            menuMap.put("icone", item.getIcone());
            menuMap.put("type", "groupMenu");

            // 🔥 CORRECTION : Récupérer correctement les sous-menus
            List<Map<Object, Object>> sousMenusList = new ArrayList<>();

            if (!groupMenu.getSous_menus().isEmpty() && groupMenu.getIsActive()) {
                for (AbstractMenu sousMenu : groupMenu.getSous_menus()) {
                    if (sousMenu.getIsActive()) {
                        // 🔥 IMPORTANT : Récupérer le résultat de la récursion
                        Map<Object, Object> sousMenuMap = recursiveSousMenu(sousMenu.getId());
                        if (!sousMenuMap.isEmpty()) {
                            sousMenusList.add(sousMenuMap);
                        }
                    }
                }
            }

            menuMap.put("sous_menus", sousMenusList);
            return menuMap;

        } else {
            // C'est un Menu simple
            Menu menu = (Menu) item;
            menuMap.put("id", item.getId());
            menuMap.put("nom", item.getTitle());
            menuMap.put("title", item.getTitle());
            menuMap.put("url", menu.getUrl());
            menuMap.put("icone", item.getIcone());
            menuMap.put("type", "menu");
            return menuMap;
        }
    }


    @GetMapping("/{portailCode}")
    public ResponseEntity<?> getPortailByCode(@PathVariable String portailCode) {

        try {
            UtilisateurFrontoffice user = service.getCurrentUser()
                    .orElseThrow(() -> new RuntimeException("Utilisateur non authentifié"));

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
                    .anyMatch(role -> role.getNom().equals(user.getRole().getNom()));

            if (!hasAccess) {
                System.out.println("❌ [FRONTOFFICE] Accès refusé pour le rôle: " + user.getRole().getNom());
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
            List<Map<Object, Object>> groupMenuList = new ArrayList<>();

            List<GroupMenu> groupes = portail.getGroupMenu().stream()
                    .filter(g -> g.getIsActive() && g.getParent() == null)
                    .collect(Collectors.toList());

            for (GroupMenu group : groupes) {

                Map<Object, Object> groupMap = recursiveSousMenu(group.getId());

                groupMenuList.add(groupMap);
            }


            response.put("groupMenu", groupMenuList);



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
            List<Portail> portails = portailRepository.findByNomRoleAndActif(
                    user.getRole().getNom(), true);

            List<PortailResponse> response = portails.stream()
                    .map(PortailResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

   /* @GetMapping("/fix-menu-links")
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
    }*/
}