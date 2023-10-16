package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.domain.UserRole;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Date;

@Data
public class UserRoleDTO implements DTO {

    private Date startDate;
    private Date endDate;
    private RoleDTO roleDTO;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToRoleDTO(utils));
        mapper.addMappings(mapFromRoleDTO(utils));
        return mapper;
    }

    public PropertyMap<UserRole, UserRoleDTO> mapToRoleDTO(MappingUtils utils) {
        return new PropertyMap<UserRole, UserRoleDTO>() {
            @Override
                protected void configure() {
                Converter<UserRole, RoleDTO> mapRole = new AbstractConverter<UserRole,RoleDTO>(){
                @Override
                protected RoleDTO convert(UserRole ur) {
                    return utils.map(ur.getRole(),RoleDTO.class);
                    }
                };
                using(mapRole).map(source,destination.getRoleDTO());
                }
            };

    }
    public PropertyMap<UserRoleDTO, UserRole> mapFromRoleDTO(MappingUtils utils) {
        return new PropertyMap<UserRoleDTO, UserRole>() {
            @Override
                protected void configure() {
                Converter<UserRoleDTO, Role> mapRole = new AbstractConverter<UserRoleDTO, Role>(){
                @Override
                protected Role convert(UserRoleDTO userRoleDTO) {
                    return utils.mapFromDTO(userRoleDTO.getRoleDTO(),Role.class);
                    }
                };
                using(mapRole).map(source,destination.getRole());
                }
            };

    }


}
