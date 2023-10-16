package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.domain.User;
import com.CVP.cv_project.domain.UserRole;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO implements DTO {
    private String name;
    private String phone;
    private String email;
    private int maxWeeklyHours;


    private List<UserRoleDTO> userRoleDTOs;
    private List<CVDTO> cvDTOs;


    public PropertyMap<User, UserDTO> mapToUserRoleDTO(MappingUtils utils) {
        return new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {

                Converter<User, List<UserRoleDTO>> mapUserRolesToDTO = new AbstractConverter<User, List<UserRoleDTO>>() {
                    @Override
                    protected List<UserRoleDTO> convert(User user) {
                        return utils.mapList(new ArrayList<>(user.getUserRoles()), UserRoleDTO.class);
                    }
                };

                using(mapUserRolesToDTO).map(source, destination.getUserRoleDTOs());


            }
        };
    }

    public PropertyMap<UserDTO, User> mapFromUserRoleDTO(MappingUtils utils){
        return new PropertyMap<UserDTO, User>() {
            @Override
            protected void configure() {
                Converter<UserDTO, List<UserRole>> mapFromUserRolesDTO = new AbstractConverter<UserDTO, List<UserRole>>() {

                    @Override
                    protected List<UserRole> convert(UserDTO userDTO) {
                        return utils.mapListFromDTO(new ArrayList<>(userDTO.getUserRoleDTOs()), UserRole.class);
                    }
                };

                using(mapFromUserRolesDTO).map(source,destination.getUserRoles());

            }
        };
    }

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToUserRoleDTO(utils));
        mapper.addMappings(mapFromUserRoleDTO(utils));

        return mapper;
    }
}


