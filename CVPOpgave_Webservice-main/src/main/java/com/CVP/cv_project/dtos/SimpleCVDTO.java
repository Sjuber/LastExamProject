package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.CVState;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleCVDTO implements DTO {

    private int bookedHours;
    private int maxHours;
    private String consultantName;
    private String consultantPhone;
    private String title;
    private String consultantEmail;
    private List<CVs_KnowledgeDTO> cvKnowledgeList;
    private int sortScore;
    private int cvID;
    private String description;
    private String techBackground;
    private String cvStateString;
    private List<PractisedLanguageDTO> languages;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
         mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.addMappings(mapToConsultantName(utils));
        mapper.addMappings(mapToConsultantPhone(utils));
         mapper.addMappings(mapToConsultantEmail(utils));
        mapper.addMappings(mapToCVKnowledgeDTO(utils));
        mapper.addMappings(mapToSimpleCVDTOID(utils));
         mapper.addMappings(mapStateToSimpleCVTDO(utils));
         mapper.addMappings(mapToLanguageListDTO(utils));
        return mapper;
    }

    public PropertyMap<CV, SimpleCVDTO> mapToLanguageListDTO(MappingUtils utils) {
        return new PropertyMap<CV, SimpleCVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<PractisedLanguageDTO>> languagesListDTOs = new AbstractConverter<CV, List<PractisedLanguageDTO>>() {
                    @Override
                    protected List<PractisedLanguageDTO> convert(CV cv) {
                        return utils.mapList(new ArrayList<>(cv.getPractisedLanguages()), PractisedLanguageDTO.class);
                    }

                };
                using(languagesListDTOs).map(source, destination.languages);
            }
        };

    }

    public PropertyMap<CV, SimpleCVDTO> mapToCVKnowledgeDTO(MappingUtils utils) {
        return new PropertyMap<CV, SimpleCVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<CVs_KnowledgeDTO>> mapCVs_Knowledge = new AbstractConverter<CV, List<CVs_KnowledgeDTO>>() {
                    @Override
                    protected List<CVs_KnowledgeDTO> convert(CV cv) {
                        return utils.mapList(new ArrayList<>(cv.getKnowledgeList()), CVs_KnowledgeDTO.class);
                    }

                };
                using(mapCVs_Knowledge).map(source, destination.cvKnowledgeList);
            }
        };

    }

    public PropertyMap<CV, SimpleCVDTO> mapToSimpleCVDTOID(MappingUtils utils) {
        return new PropertyMap<CV, SimpleCVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, Integer> cvIDConverter = new AbstractConverter<CV, Integer>() {
                    @Override
                    protected Integer convert(CV cv) {
                        return cv.getId();
                    }

                };
                using(cvIDConverter).map(source, destination.cvID);
            }
        };

    }

    public PropertyMap<CV, SimpleCVDTO> mapToConsultantName(MappingUtils utils) {
        return new PropertyMap<CV, SimpleCVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, String> mapConsultantName = new AbstractConverter<CV, String>() {
                    @Override
                    protected String convert(CV cv) {
                        return cv.getConsultant().getName();
                    }
                };
                using(mapConsultantName).map(source, destination.consultantName);
            }
        };

    }

    public PropertyMap<CV, SimpleCVDTO> mapToConsultantPhone(MappingUtils utils) {
        return new PropertyMap<CV, SimpleCVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, String> mapRole = new AbstractConverter<CV, String>() {
                    @Override
                    protected String convert(CV cv) {
                        return cv.getConsultant().getPhone();
                    }
                };
                using(mapRole).map(source, destination.consultantPhone);
            }
        };
    }

    public PropertyMap<CV, SimpleCVDTO> mapToConsultantEmail(MappingUtils utils) {
        return new PropertyMap<CV, SimpleCVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, String> mapRole = new AbstractConverter<CV, String>() {
                    @Override
                    protected String convert(CV cv) {
                        return cv.getConsultant().getEmail();
                    }
                };
                using(mapRole).map(source, destination.consultantEmail);
            }
        };

    }

public PropertyMap<CV, SimpleCVDTO> mapStateToSimpleCVTDO(MappingUtils utils) {
    return new PropertyMap<CV, SimpleCVDTO>() {
        @Override
        protected void configure() {
            Converter<CV, String> mapState = new AbstractConverter<CV, String>() {
                @Override
                protected String convert(CV cv) {
                    if (cv.equals(null)){
                        return null;
                    }else {
                        return cv.getCVState().getState();
                    }
                }
            };
            using(mapState).map(source, destination.cvStateString);
        }
    };

}

}