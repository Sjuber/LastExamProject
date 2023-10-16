package com.CVP.cv_project.services;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRolesInDB() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    public Role findRoleByTitle(String title) {
        return roleRepository.findByTitle(title);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findRoleByID(int roleID) throws ChangeSetPersister.NotFoundException {
        return roleRepository.findById(roleID).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public List<Role> getRoleForUser(String phone) {
    return roleRepository.findByUserPhone(phone);
    }
}
