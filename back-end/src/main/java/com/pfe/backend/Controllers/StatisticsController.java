package com.pfe.backend.Controllers;

import com.pfe.backend.Repositories.MenuRepository;
import com.pfe.backend.Repositories.PortailRepository;
import com.pfe.backend.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    @Autowired
    private PortailRepository portailRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("portailsCount", portailRepository.count());
        stats.put("menusCount", menuRepository.count());
        stats.put("rolesCount", roleRepository.count());
        return stats;
    }
}