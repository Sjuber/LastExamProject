package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.*;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Date;

@Data
public class JobDTO implements DTO {
    private int id;
    private Date endDate;
    private Date startDate;
    private String title;
    private CompanyDTO company;

    public PropertyMap<Job, JobDTO> mapToJobDTO(MappingUtils utils) {
        return new PropertyMap<Job, JobDTO>() {
            @Override
            protected void configure() {

                Converter<Job, CompanyDTO> mapUserRolesToDTO = new AbstractConverter<Job, CompanyDTO>() {
                    @Override
                    protected CompanyDTO convert(Job job) {
                        if (job == null || job.getCompany() == null) {
                            return null;
                        } else {
                            return utils.map(job.getCompany(), CompanyDTO.class);
                        }
                    }
                };

                using(mapUserRolesToDTO).map(source, destination.company);


            }
        };
    }

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToJobDTO(utils));

        return mapper;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }
}
