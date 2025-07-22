package com.pfe.backend.Repositories;

import com.pfe.backend.Entities.Menu;
import com.pfe.backend.Entities.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // ⭐ AJOUTEZ CETTE MÉTHODE
    Optional<Role> findByCode(String code);
}
