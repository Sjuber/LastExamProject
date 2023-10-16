package com.CVP.cv_project.services;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.domain.User;
import com.CVP.cv_project.domain.UserRole;
import com.CVP.cv_project.exceptions.ExistingEntity;
import com.CVP.cv_project.repos.RoleRepository;
import com.CVP.cv_project.repos.UserRepository;
import com.CVP.cv_project.repos.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsersInDB(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User createUser(User newUser) throws ExistingEntity {
        ArrayList<UserRole> userRolesWithRolesIDAndUser = new ArrayList<>();
        User finalNewUser;
            if(userRepository.findByEmail(newUser.getEmail()).isEmpty() && userRepository.findByPhone(newUser.getPhone()).isEmpty()){
                    newUser.setPassword("123456!"); //TODO Default password to change for the user later
                    newUser.getUserRoles().forEach(userRole -> {
                        Role roleDB = roleRepository.findByTitle(userRole.getRole().getTitle());
                        userRole.setRole(roleDB);
                        userRolesWithRolesIDAndUser.add(userRole);
                        userRole.setUser(newUser);
                    });
                    newUser.setUserRoles(userRolesWithRolesIDAndUser);
                    finalNewUser = userRepository.save(newUser);
                    return userRepository.findById(finalNewUser.getId()).get();
            }
                throw new ExistingEntity("User already exist - try another phone number or e-mail");
    }

    public User findUserByPhone(String phone) {
        User userDB = userRepository.findByPhone(phone).get();
        return userDB;
    }

    public User verifyAndFindUser(String email, String password) throws CredentialException, ChangeSetPersister.NotFoundException {
        User userDB = userRepository.findByEmail(email).orElseThrow(CredentialException::new);
        if(!(userDB.getPassword().equals(password)) ){
           throw new CredentialException("Email or password is not correct");
        }else {
            return userDB;
        }
    }

    public User updateUser(User editedUser, User originalUser) {
        List <UserRole> userRoles = new ArrayList<>();
        List <Integer> userRolesIDs = new ArrayList<>();

        originalUser.setName(editedUser.getName());
        originalUser.setPhone(editedUser.getPhone());

        originalUser.setEmail(editedUser.getEmail());
        originalUser.setMaxWeeklyHours(editedUser.getMaxWeeklyHours());
        editedUser.getUserRoles().forEach(userRole ->{
            Role roleWithID = roleRepository.findByTitle(userRole.getRole().getTitle());
            userRole.setRole(roleWithID);
            userRole.setUser(originalUser);
            userRoles.add(userRole);
        });
        originalUser.getUserRoles().forEach( userRole -> userRolesIDs.add(userRole.getId()));
        originalUser.getUserRoles().clear();
        userRoleRepository.deleteByIdIn(userRolesIDs);
        originalUser.getUserRoles().addAll(userRoles);
        userRepository.save(originalUser);
        return userRepository.findByPhone(originalUser.getPhone()).get();
    }
}
