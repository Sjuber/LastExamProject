package com.CVP.cv_project.dtos;

import com.CVP.cv_project.dtos.utils.DTO;
import lombok.Data;

@Data
public class KnowCategoryDTO implements DTO {
    private String name;
    private String description;
}
