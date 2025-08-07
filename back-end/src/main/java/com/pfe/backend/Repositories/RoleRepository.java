package com.pfe.backend.Repositories;

import java.util.Optional;

import com.pfe.backend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    
}
