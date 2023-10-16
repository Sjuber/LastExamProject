package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.Project;
import com.CVP.cv_project.domain.Project_Knowledge;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

@Data
public class Project_KnowledgeDTO implements DTO {

    private String note;
    private KnowledgeDTO knowledge;

    public PropertyMap<Project_Knowledge, Project_KnowledgeDTO> mapToKnowledgeDTO(MappingUtils utils) {
        return new PropertyMap<Project_Knowledge,Project_KnowledgeDTO>() {
            @Override
            protected void configure() {

                Converter<Project_Knowledge, KnowledgeDTO> mapKnowledgeToDTO = new AbstractConverter<Project_Knowledge,KnowledgeDTO>() {
                    @Override
                    protected KnowledgeDTO convert(Project_Knowledge project_knowledge) {
                        if (project_knowledge == null || project_knowledge.getKnowledge() == null) {
                            return null;
                        } else {
                            return utils.map(project_knowledge.getKnowledge(), KnowledgeDTO.class);
                        }
                    }
                };

                using(mapKnowledgeToDTO).map(source, destination.knowledge);


            }
        };
    }


    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToKnowledgeDTO(utils));
        return mapper;
    }
}
