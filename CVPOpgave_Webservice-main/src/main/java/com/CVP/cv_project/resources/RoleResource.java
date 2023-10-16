package com.CVP.cv_project.resources;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.dtos.RoleDTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.CVP.cv_project.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = RoleResource.USERS_V_1_ROLE)
@CrossOrigin(origins = "http://localhost:4200")
public class RoleResource {
    public static final String USERS_V_1_ROLE = "users/v1/roles";

    private static final MappingUtils mappingUtils = new MappingUtils();
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "{roleID}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<RoleDTO> getRoleByID(@PathVariable int roleID){
        try {
             Role role = roleService.findRoleByID(roleID);
             RoleDTO roleDTO = modelMapper.map(role,RoleDTO.class);
             return new ResponseEntity<>(roleDTO,HttpStatus.CREATED);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<RoleDTO>> allRolesInDB(){
        List<RoleDTO> roleDTOS = mappingUtils.mapList(roleService.getAllRolesInDB(),RoleDTO.class);
        return new ResponseEntity<>(roleDTOS,HttpStatus.OK);
    }

    @GetMapping(path = "user/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<RoleDTO>> getRolesForUser(@PathVariable String phoneNumber){
        List <RoleDTO> roleDTOS = new ArrayList<RoleDTO>();
        roleService.getRoleForUser(phoneNumber).forEach(role -> roleDTOS.add(modelMapper.map(role, RoleDTO.class)));
        return new ResponseEntity<>(roleDTOS, HttpStatus.CREATED);
    }

    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO){
        roleService.createRole(new Role(roleDTO.getTitle()));
        RoleDTO roleDTOdb = modelMapper.map(roleService.findRoleByTitle(roleDTO.getTitle()), RoleDTO.class);
        return new ResponseEntity<>(roleDTOdb, HttpStatus.CREATED);
    }

}
