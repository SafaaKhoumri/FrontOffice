package com.pfe.backend.Service;

import com.pfe.backend.Entities.Menu;
import com.pfe.backend.Repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    MenuRepository menuRepository;
    public Menu add(Menu menu){
        return menuRepository.save(menu);
    }
    public List<Menu> getAllMenu(){
        return menuRepository.findAll();
    }
    public Menu getMenuById(Long id){
        return menuRepository.findById(id).get();
    }
    public void deleteMenu(Long id){
        menuRepository.deleteById(id);
    }
    public Menu getMenuByMenu_order(int order){
        return menuRepository.findByMenuOrder(order);
    }
    public List<Menu> findByGroupMenuId(Long id){
        return menuRepository.findByGroupeMenuId(id);
    }
    public void deleteMenuById(Long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu != null) {
            menuRepository.delete(menu);
        }
    }
}
