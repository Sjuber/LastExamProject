package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.Company;
import com.CVP.cv_project.domain.Project;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyDTO implements DTO {

    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
