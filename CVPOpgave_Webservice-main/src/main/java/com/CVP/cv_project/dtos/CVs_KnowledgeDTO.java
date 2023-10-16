package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.CVs_Knowledge;
import com.CVP.cv_project.domain.Knowledge;
import com.CVP.cv_project.domain.Enums.LevelOfCVKnowledgeSkill;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

@Data
public class CVs_KnowledgeDTO implements DTO {

    private String levelSkill;
    private int years;
    private String note;
    private KnowledgeDTO knowledge;
    private int levelSkillRated;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToKnowledgeDTO(utils));
        mapper.addMappings(mapFromKnowledgeDTO(utils));
        mapper.addMappings(mapToKnowledgeDTOToRatingSkill(utils));
        return mapper;
    }


    public PropertyMap<CVs_Knowledge, CVs_KnowledgeDTO> mapToKnowledgeDTO(MappingUtils utils) {
        return new PropertyMap<CVs_Knowledge, CVs_KnowledgeDTO>() {
            @Override
            protected void configure() {
                Converter<CVs_Knowledge, KnowledgeDTO> mapCV_Knowledge = new AbstractConverter<CVs_Knowledge, KnowledgeDTO>() {
                    @Override
                    protected KnowledgeDTO convert(CVs_Knowledge cvs_Knowledge) {
                        if(cvs_Knowledge==null || cvs_Knowledge.getKnowledge()==null){
                            return null;
                        }else {
                            return utils.map(cvs_Knowledge.getKnowledge(), KnowledgeDTO.class);
                        }
                    }
                };
                using(mapCV_Knowledge).map(source, destination.knowledge);
            }
        };

    }

    public PropertyMap<CVs_KnowledgeDTO, CVs_Knowledge> mapFromKnowledgeDTO(MappingUtils utils) {
        return new PropertyMap<CVs_KnowledgeDTO, CVs_Knowledge>() {
            @Override
            protected void configure() {
                Converter<CVs_KnowledgeDTO, Knowledge> mapCV_Knowledge = new AbstractConverter<CVs_KnowledgeDTO, Knowledge>() {
                    @Override
                    protected Knowledge convert(CVs_KnowledgeDTO cvs_KnowledgeDto) {
                        if(cvs_KnowledgeDto==null || cvs_KnowledgeDto.getKnowledge()==null){
                            return null;
                        }else {
                            return utils.mapFromDTO(cvs_KnowledgeDto.getKnowledge(), Knowledge.class);
                        }
                    }
                };
                using(mapCV_Knowledge).map(source, destination.getKnowledge());
            }
        };

    }
    public PropertyMap<CVs_Knowledge, CVs_KnowledgeDTO> mapToKnowledgeDTOToRatingSkill(MappingUtils utils) {
        return new PropertyMap<CVs_Knowledge, CVs_KnowledgeDTO>() {
            @Override
            protected void configure() {
                Converter<CVs_Knowledge, Integer> mapCV_Knowledge = new AbstractConverter<CVs_Knowledge, Integer>() {
                    @Override
                    protected Integer convert(CVs_Knowledge cvs_Knowledge) {
                        if(cvs_Knowledge==null || cvs_Knowledge.getKnowledge()==null){
                            return null;
                        }else {
                            return Integer.valueOf(LevelOfCVKnowledgeSkill.valueOf(
                                    cvs_Knowledge.getLevelSkill().replace(" " ,"_")).getState());
                        }
                    }
                };
                using(mapCV_Knowledge).map(source, destination.levelSkillRated);
            }
        };

    }
}
