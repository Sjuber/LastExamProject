package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.CVs_Knowledge;
import com.CVP.cv_project.domain.PractisedLanguage;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.CVP.cv_project.handlers.SearchFeaturesListObject;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchFeaturesDTO implements DTO {

    private List<CVs_KnowledgeDTO> cvsKnowledgeDTOList;
    private PractisedLanguageDTO languageDTO;
    private int notBookedHours ;
    private String companyWorkedFor;

    private List<String> prioritiesNamesList;
    private int amountMostContainCVknowledgeInCV;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapFromCVKnowledgeDTOList(utils));
        mapper.addMappings(mapFromPractisedLanguageDTO(utils));

        return mapper;
    }

    public PropertyMap<SearchFeaturesDTO, SearchFeaturesListObject> mapFromCVKnowledgeDTOList(MappingUtils utils) {
        return new PropertyMap<SearchFeaturesDTO, SearchFeaturesListObject>() {
            @Override
            protected void configure() {
                Converter<SearchFeaturesDTO, List<CVs_Knowledge>> mapCVKnowledgeDTOList = new AbstractConverter<SearchFeaturesDTO, List<CVs_Knowledge>>() {

                    @Override
                    protected List<CVs_Knowledge> convert(SearchFeaturesDTO searchFeaturesDTO) {
                        if(searchFeaturesDTO.cvsKnowledgeDTOList.isEmpty()){
                            return new ArrayList<>();
                        }
                        return utils.mapListFromDTO(searchFeaturesDTO.cvsKnowledgeDTOList, CVs_Knowledge.class);
                    }
                };
                using(mapCVKnowledgeDTOList).map(source, destination.getCvsKnowledgeList());
            }

        };
    }

    public PropertyMap<SearchFeaturesDTO, SearchFeaturesListObject> mapFromPractisedLanguageDTO(MappingUtils utils) {
        return new PropertyMap<SearchFeaturesDTO, SearchFeaturesListObject>() {
            @Override
            protected void configure() {
                Converter<SearchFeaturesDTO, PractisedLanguage> mapLanguageDTO = new AbstractConverter<SearchFeaturesDTO, PractisedLanguage>() {


                    @Override
                    protected PractisedLanguage convert(SearchFeaturesDTO searchFeaturesDTO) {
                        if(searchFeaturesDTO.getLanguageDTO()==null){
                            return null;
                        }
                        return utils.mapFromDTO(searchFeaturesDTO.getLanguageDTO(), PractisedLanguage.class);
                    }
                };
                using(mapLanguageDTO).map(source, destination.getLanguage());
            }



        };
    }
}
