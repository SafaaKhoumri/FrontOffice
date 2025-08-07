package com.pfe.backend.Repositories;

import com.pfe.backend.Entities.GroupMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMenuRepo extends JpaRepository<GroupMenu, Long> {
    GroupMenu findByMenuOrder(Integer order);

    List<GroupMenu> findByParentId(Long id);


}
