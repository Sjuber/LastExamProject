package com.CVP.cv_project.resources;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.domain.User;
import com.CVP.cv_project.dtos.UserDTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.CVP.cv_project.exceptions.ExistingEntity;
import com.CVP.cv_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@RestController
@RequestMapping(path = UserResource.USERS_V_1)
@CrossOrigin(origins = "http://localhost:4200")
public class UserResource {
    public static final String USERS_V_1 = "users/v1";
    private static final MappingUtils mappingUtils = new MappingUtils();

    @Autowired
    private UserService userService;

    @GetMapping(path = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = mappingUtils.mapList(userService.getAllUsersInDB(), UserDTO.class);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping(path = "user/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity userInDBByPhone(@PathVariable String phone) {
        UserDTO userDTO = mappingUtils.map(userService.findUserByPhone(phone), UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity verifyUser(@RequestBody List<String> userRequestAttr) {
        UserDTO userDTODB = null;
        User userDB;
        try {
            userDB = userService.verifyAndFindUser(userRequestAttr.get(0), userRequestAttr.get(1));
            userDTODB = mappingUtils.map(userDB, UserDTO.class);
        } catch (CredentialException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTODB, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody UserDTO newUserDTO) {
        try {
            if (newUserDTO.equals(null) || newUserDTO.getEmail().equals(null) || newUserDTO.getPhone().equals(null) || newUserDTO.getName().equals(null)) {
                return new ResponseEntity<>("Some of the added users properties are empty", HttpStatus.BAD_REQUEST);
            } else {
                User newUser = mappingUtils.mapFromDTO(newUserDTO, User.class);
                UserDTO userDTODB = mappingUtils.map(userService.createUser(newUser), UserDTO.class);
                return new ResponseEntity<>(userDTODB, HttpStatus.ACCEPTED);
            }
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (EmptyStackException e) {
            return new ResponseEntity<>("Added user must have roles", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(path = "user/{phone}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestBody UserDTO editedUserDTO, @PathVariable String phone) {
        try {
            if (editedUserDTO.equals(null) || editedUserDTO.getEmail().equals(null) || editedUserDTO.getPhone().equals(null) || editedUserDTO.getName().equals(null)) {
                return new ResponseEntity<>("Some of the added users properties are empty", HttpStatus.BAD_REQUEST);
            } else {
                User editedUser = mappingUtils.mapFromDTO(editedUserDTO, User.class);
                User originalUser = userService.findUserByPhone(phone);
                UserDTO userDTODB = mappingUtils.map(userService.updateUser(editedUser, originalUser), UserDTO.class);
                return new ResponseEntity<>(userDTODB, HttpStatus.ACCEPTED);
            }
        } catch (EmptyStackException e) {
            return new ResponseEntity<>("Added user must have roles", HttpStatus.BAD_REQUEST);
        }

    }
}
