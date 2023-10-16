package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.Knowledge;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

@Data
public class PractisedLanguageDTO implements DTO {
    private String name;
    private String levelWritting;
    private String levelReading;
}
