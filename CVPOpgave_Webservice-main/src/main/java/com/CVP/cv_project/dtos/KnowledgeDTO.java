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
public class KnowledgeDTO implements DTO {
    private String name;
    private KnowCategoryDTO knowCategoryDTO;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToCategoryDTO(utils));

        return mapper;
    }

    public PropertyMap<Knowledge, KnowledgeDTO> mapToCategoryDTO(MappingUtils utils) {
        return new PropertyMap<Knowledge, KnowledgeDTO>() {
            @Override
            protected void configure() {

                Converter<Knowledge, KnowCategoryDTO> mapCategoryToDTO = new AbstractConverter<Knowledge, KnowCategoryDTO>() {
                    @Override
                    protected KnowCategoryDTO convert(Knowledge knowledge) {
                        return utils.map(knowledge.getCategory(), KnowCategoryDTO.class);
                    }
                };

                using(mapCategoryToDTO).map(source, destination.knowCategoryDTO);


            }
        };
    }
}
