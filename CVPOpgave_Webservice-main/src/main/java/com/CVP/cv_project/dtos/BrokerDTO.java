package com.CVP.cv_project.dtos;

import com.CVP.cv_project.dtos.utils.DTO;
import lombok.Data;

import java.util.Date;

@Data
public class BrokerDTO implements DTO {

    private Date startDate;
    private Date endDate;
    private String name;

}
