package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.*;
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
public class CVDTO implements DTO {

    private Integer id;
    private int bookedHours;
    private int maxHours;
    private String title;
    private String description;
    private String techBackground;
    private String cvStateString;
    private UserDTO consultantDTO;
    private UserDTO authorDTO;
    private Integer cvOriginalID;
    private List<CVs_KnowledgeDTO> cv_knowledgeList;
    private List<JobDTO> jobs;
    private List<PractisedLanguageDTO> practisedLanguages;
    private List<CoursesCertificationDTO> coursesCertifications;
    private List<EducationDTO> educations;
    private List<ProjectDTO> projects;


    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.addMappings(mapToConsultantDTO(utils));
        mapper.addMappings(mapToAuthorDTO(utils));
        mapper.addMappings(mapFromConsultantDTO(utils));
        mapper.addMappings(mapFromAuthorDTO(utils));
        mapper.addMappings(mapToCVOriginalID(utils));
        mapper.addMappings(mapToCVKnowledgeDTO(utils));
        mapper.addMappings(mapStateFromCVTDO(utils));
        mapper.addMappings(mapStateToCVTDO(utils));
        mapper.addMappings(mapToJobDTOs(utils));
        mapper.addMappings(mapToPractisedLanguageDTOs(utils));
        mapper.addMappings(mapToCoursesCertificationsDTO(utils));
        mapper.addMappings(mapToEducations(utils));
        mapper.addMappings(mapToProjectDTO(utils));
        return mapper;
    }

    public PropertyMap<CV, CVDTO> mapToCVKnowledgeDTO(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<CVs_KnowledgeDTO>> mapCVs_Knowledge = new AbstractConverter<CV, List<CVs_KnowledgeDTO>>() {
                    @Override
                    protected List<CVs_KnowledgeDTO> convert(CV cv) {
                        if (cv == null ||cv.getKnowledgeList() == null){
                            return null;
                        }else{
                                return utils.mapList(new ArrayList<>(cv.getKnowledgeList()), CVs_KnowledgeDTO.class);
                            }
                        }

                };
                using(mapCVs_Knowledge).map(source, destination.cv_knowledgeList);
            }
        };

    }
    public PropertyMap<CV, CVDTO> mapToProjectDTO(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<ProjectDTO>> mapCVs_Knowledge = new AbstractConverter<CV, List<ProjectDTO>>() {
                    @Override
                    protected List<ProjectDTO> convert(CV cv) {
                        if (cv == null ||cv.getProjects() == null){
                            return null;
                        }else{
                                return utils.mapList(new ArrayList<>(cv.getProjects()), ProjectDTO.class);
                            }
                        }

                };
                using(mapCVs_Knowledge).map(source, destination.projects);
            }
        };

    }
    public PropertyMap<CV, CVDTO> mapToCoursesCertificationsDTO(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<CoursesCertificationDTO>> mapCVs_Knowledge = new AbstractConverter<CV, List<CoursesCertificationDTO>>() {
                    @Override
                    protected List<CoursesCertificationDTO> convert(CV cv) {
                        if (cv == null ||cv.getCoursesCertifications() == null){
                            return null;
                        }else{
                            return utils.mapList(new ArrayList<>(cv.getCoursesCertifications()), CoursesCertificationDTO.class);
                        }
                    }

                };
                using(mapCVs_Knowledge).map(source, destination.coursesCertifications);
            }
        };

    }

    public PropertyMap<CV, CVDTO> mapToEducations(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<EducationDTO>> mapEducation = new AbstractConverter<CV, List<EducationDTO>>() {
                    @Override
                    protected List<EducationDTO> convert(CV cv) {
                        if (cv == null ||cv.getEducations() == null){
                            return null;
                        }else{
                            return utils.mapList(new ArrayList<>(cv.getEducations()), EducationDTO.class);
                        }
                    }
                };
                using(mapEducation).map(source, destination.educations);
            }
        };
    }

    public PropertyMap<CV, CVDTO> mapToPractisedLanguageDTOs(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<PractisedLanguageDTO>> mapCVs_Knowledge = new AbstractConverter<CV, List<PractisedLanguageDTO>>() {
                    @Override
                    protected List<PractisedLanguageDTO> convert(CV cv) {
                        if (cv == null ||cv.getPractisedLanguages() == null){
                            return null;
                        }else{
                                return utils.mapList(new ArrayList<>(cv.getPractisedLanguages()), PractisedLanguageDTO.class);
                            }
                        }

                };
                using(mapCVs_Knowledge).map(source, destination.practisedLanguages);
            }
        };

    }

    public PropertyMap<CV, CVDTO> mapToJobDTOs(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, List<JobDTO>> mapCVs_Knowledge = new AbstractConverter<CV, List<JobDTO>>() {
                    @Override
                    protected List<JobDTO> convert(CV cv) {
                        if (cv == null ||cv.getJobs() == null){
                            return null;
                        }else{
                            return utils.mapList(new ArrayList<>(cv.getJobs()), JobDTO.class);
                        }
                    }

                };
                using(mapCVs_Knowledge).map(source, destination.jobs);
            }
        };

    }

    public PropertyMap<CV, CVDTO> mapToConsultantDTO(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, UserDTO> mapRole = new AbstractConverter<CV, UserDTO>() {
                    @Override
                    protected UserDTO convert(CV cv) {
                        return utils.map(cv.getConsultant(), UserDTO.class);
                    }
                };
                using(mapRole).map(source, destination.consultantDTO);
            }
        };

    }

    public PropertyMap<CV, CVDTO> mapToAuthorDTO(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, UserDTO> mapRole = new AbstractConverter<CV, UserDTO>() {
                    @Override
                    protected UserDTO convert(CV cv) {
                        return utils.map(cv.getAuthor(), UserDTO.class);
                    }
                };
                using(mapRole).map(source, destination.authorDTO);
            }
        };

    }

    public PropertyMap<CV, CVDTO> mapToCVOriginalID(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
            @Override
            protected void configure() {
                Converter<CV, Integer> mapRole = new AbstractConverter<CV, Integer>() {
                    @Override
                    protected Integer convert(CV cv) {
                        if (cv.equals(null)){
                           return null;
                        }else {
                            return cv.getId();
                        }
                    }
                };
                using(mapRole).map(source, destination.cvOriginalID);
            }
        };

    }
    public PropertyMap<CVDTO, CV> mapFromConsultantDTO(MappingUtils utils) {
        return new PropertyMap<CVDTO, CV>() {
            @Override
            protected void configure() {
                Converter<CVDTO, User> mapRole = new AbstractConverter<CVDTO, User>() {
                    @Override
                    protected User convert(CVDTO cvDTO) {
                        return utils.mapFromDTO(cvDTO.consultantDTO, User.class);
                    }
                };
                using(mapRole).map(source, destination.getConsultant());
            }
        };

    }

    public PropertyMap<CVDTO, CV> mapFromAuthorDTO(MappingUtils utils) {
        return new PropertyMap<CVDTO, CV>() {
            @Override
            protected void configure() {
                Converter<CVDTO, User> mapRole = new AbstractConverter<CVDTO, User>() {
                    @Override
                    protected User convert(CVDTO cvDTO) {
                        return utils.mapFromDTO(cvDTO.authorDTO, User.class);
                    }
                };
                using(mapRole).map(source, destination.getAuthor());
            }
        };
    }

    public PropertyMap<CV, CVDTO> mapStateToCVTDO(MappingUtils utils) {
        return new PropertyMap<CV, CVDTO>() {
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
    public PropertyMap<CVDTO, CV> mapStateFromCVTDO(MappingUtils utils) {
        return new PropertyMap<CVDTO, CV>() {
            @Override
            protected void configure() {
                Converter<CVDTO, CVState> mapState = new AbstractConverter<CVDTO, CVState>() {
                    @Override
                    protected CVState convert(CVDTO cvDTO) {
                        if (cvDTO.equals(null)){
                            return null;
                        }else {
                            return CVState.valueOfState(cvDTO.cvStateString);
                        }
                    }
                };
                using(mapState).map(source, destination.getCVState());
            }
        };

    }
}
