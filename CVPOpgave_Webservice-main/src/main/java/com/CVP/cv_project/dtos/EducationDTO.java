package com.CVP.cv_project.dtos;

import com.CVP.cv_project.dtos.utils.DTO;
import lombok.Data;

import java.util.Date;

@Data
public class EducationDTO implements DTO {

    private String title;
    private Integer startYear;
    private Integer endYear;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }
}