package com.CVP.cv_project.dtos;

import com.CVP.cv_project.dtos.utils.DTO;
import lombok.Data;

import java.util.Date;

@Data
public class CoursesCertificationDTO implements DTO {

    private String title;
    private Date startDate;
    private Date endDate;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
