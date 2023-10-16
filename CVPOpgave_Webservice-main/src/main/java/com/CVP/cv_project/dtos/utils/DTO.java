package com.CVP.cv_project.dtos.utils;

import org.modelmapper.ModelMapper;

public interface DTO {

    default ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        return mapper;
    }

}
