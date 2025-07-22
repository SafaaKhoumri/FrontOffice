package com.pfe.backend.Service;

import com.pfe.backend.Entities.GroupMenu;
import com.pfe.backend.Repositories.GroupMenuRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupMenuService {
    @Autowired
    GroupMenuRepo groupMenuRepo;

    public GroupMenu addGroupMenu(GroupMenu groupMenu) {
        return groupMenuRepo.save(groupMenu);
    }

    public List<GroupMenu> getAllGroupMenu() {
        return groupMenuRepo.findAll();
    }

    public Optional<GroupMenu> getGroupMenuById(Long id) {
        return groupMenuRepo.findById(id);
    }
    public void deleteGroupMenu(Long id) {
        groupMenuRepo.deleteById(id);

    }
    public GroupMenu getGroupMenuByMenu_order(int order){
        return groupMenuRepo.findByMenuOrder(order);
    }
    public List<GroupMenu> getGroupMenuByParentId(Long id){
        return groupMenuRepo.findByParentId(id);
    }
    public GroupMenu deleteGroupMenuById(Long id) {
        GroupMenu groupMenu = groupMenuRepo.findById(id).orElse(null);
        if (groupMenu != null) {
            groupMenuRepo.delete(groupMenu);
        }
        return groupMenu;
    }
}
