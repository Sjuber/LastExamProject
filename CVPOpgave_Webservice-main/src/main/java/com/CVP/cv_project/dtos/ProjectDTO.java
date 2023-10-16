package com.CVP.cv_project.dtos;

import com.CVP.cv_project.domain.Company;
import com.CVP.cv_project.domain.Project;
import com.CVP.cv_project.domain.Project_Knowledge;
import com.CVP.cv_project.dtos.utils.DTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import lombok.Data;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjectDTO implements DTO {
    private String description;
    private String roleForProject;
    private Date dateStart;
    private Date dateEnd;
    private CompanyDTO customer;
    private List<Project_KnowledgeDTO> projectknowledgeList;

    private BrokerDTO broker;

    public PropertyMap<Project, ProjectDTO> mapToBrokerDTO(MappingUtils utils) {
        return new PropertyMap<Project,ProjectDTO>() {
            @Override
            protected void configure() {

                Converter<Project, BrokerDTO> mapKnowledgeToDTO = new AbstractConverter<Project,BrokerDTO>() {
                    @Override
                    protected BrokerDTO convert(Project project) {
                        if (project == null || project.getBroker() == null) {
                            return null;
                        } else {
                            return utils.map(project.getBroker(), BrokerDTO.class);
                        }
                    }
                };

                using(mapKnowledgeToDTO).map(source, destination.broker);


            }
        };
    }

    public PropertyMap<Project, ProjectDTO> mapToCompanyDTO(MappingUtils utils) {
        return new PropertyMap<Project, ProjectDTO>() {
            @Override
            protected void configure() {

                Converter<Project, CompanyDTO> mapProjectCompanyToDTO = new AbstractConverter<Project, CompanyDTO>() {
                    @Override
                    protected CompanyDTO convert(Project project) {
                        if (project == null || project.getCustomer() == null) {
                            return null;
                        } else {
                            return utils.map(project.getCustomer(), CompanyDTO.class);
                        }
                    }
                };

                using(mapProjectCompanyToDTO).map(source, destination.customer);


            }
        };
    }

    public PropertyMap<ProjectDTO, Project> mapToCompany(MappingUtils utils) {
        return new PropertyMap<ProjectDTO, Project>() {
            @Override
            protected void configure() {

                Converter<ProjectDTO, Company> mapProjectCompany = new AbstractConverter<ProjectDTO, Company>() {
                    @Override
                    protected Company convert(ProjectDTO projectDTO) {
                        System.out.println(projectDTO.getDescription() + " for " +projectDTO.getCustomer().getName());
                        if (projectDTO == null || projectDTO.getCustomer() == null) {
                            return null;
                        } else {
                            return utils.mapFromDTO(projectDTO.getCustomer(), Company.class);
                        }
                    }
                };

                using(mapProjectCompany).map(source, destination.getCustomer());


            }
        };
    }
    public PropertyMap<Project, ProjectDTO> mapToProjectKnowledgeDTOs(MappingUtils utils) {
        return new PropertyMap<Project, ProjectDTO>() {
            @Override
            protected void configure() {

                Converter<Project, List<Project_KnowledgeDTO>> mapKnowledgeToDTO = new AbstractConverter<Project,List<Project_KnowledgeDTO>>() {
                    @Override
                    protected List<Project_KnowledgeDTO> convert(Project project) {
                        if (project == null || project.getProjectsKnowledgeList() == null) {
                            return null;
                        } else {
                            return utils.mapList(new ArrayList<>(project.getProjectsKnowledgeList()), Project_KnowledgeDTO.class);
                        }
                    }
                };

                using(mapKnowledgeToDTO).map(source, destination.projectknowledgeList);


            }
        };
    }

    //public PropertyMap<ProjectDTO, Project> mapProjectKnowledgeFromProjectDTO(MappingUtils utils) {
    //    return new PropertyMap<ProjectDTO,Project>() {
    //        @Override
    //        protected void configure() {
//
    //            Converter<ProjectDTO, List<Project_Knowledge>> mapProjectKnowledgeListFromDTO = new AbstractConverter<ProjectDTO,List<Project_Knowledge>>() {
    //                @Override
    //                protected List<Project_Knowledge> convert(ProjectDTO projectDTO) {
    //                    if (projectDTO == null) {
    //                        return null;
    //                    } else {
    //                        List<Project_Knowledge> project_Knowledges = utils.mapListFromDTO(projectDTO.projectknowledgeList, Project_Knowledge.class);
    //                        project_Knowledges.forEach(project_knowledge -> {
    //                            project_knowledge.setProject(utils.mapFromDTO(projectDTO, Project.class));
    //                        });
    //                        return project_Knowledges;
    //                    }
    //                }
    //            };
//
    //            using(mapProjectKnowledgeListFromDTO).map(source, destination.getProjectsKnowledgeList());
//
//
    //        }
    //    };
    //}

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(mapToCompanyDTO(utils));
        mapper.addMappings(mapToProjectKnowledgeDTOs(utils));
        mapper.addMappings(mapToBrokerDTO(utils));
        mapper.addMappings(mapToCompany(utils));
        return mapper;
    }

}
