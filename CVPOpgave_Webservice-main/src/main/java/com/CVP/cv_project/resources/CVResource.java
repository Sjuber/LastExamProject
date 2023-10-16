package com.CVP.cv_project.resources;

import com.CVP.cv_project.domain.*;
import com.CVP.cv_project.dtos.*;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.CVP.cv_project.exceptions.ExistingEntity;
import com.CVP.cv_project.exceptions.StopLoadingException;
import com.CVP.cv_project.handlers.SearchFeaturesListObject;
import com.CVP.cv_project.services.CVService;
import com.github.dockerjava.api.exception.NotFoundException;
import net.minidev.asm.ex.NoSuchFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = CVResource.USERS_V_1_CVS)
@CrossOrigin(origins = "http://localhost:4200")
public class CVResource {
    public static final String USERS_V_1_CVS = "users/v1/cvs";

    private static final MappingUtils mappingUtils = new MappingUtils();

    @Autowired
    private CVService cvService;

    Logger logger = LoggerFactory.getLogger(CVResource.class);

    @GetMapping(path = "user/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllOriginalCVsForUser(@PathVariable String phone) {
        try {
            List<CVDTO> cvDTOs = mappingUtils.mapList(cvService.getAllOrignalCVsForUserByPhone(phone), CVDTO.class);
            return new ResponseEntity<>(cvDTOs, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "simple_cvs/not_draft/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllNotInDraftSimpleCVsForConsultant(@PathVariable String phone) {
        List<SimpleCVDTO> simpleCVDTOS = mappingUtils.mapList(cvService.getAllNotInDraftSimpleCVsForConsultant(phone), SimpleCVDTO.class);
        return new ResponseEntity<>(simpleCVDTOS, HttpStatus.ACCEPTED);
    }


    @GetMapping(path = "published", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllOriginalNotInDraftmodeCVs() {
        List<CV> CVsFromDB = cvService.getAllOrignalNotInDraftmodeCVs();
        List<SimpleCVDTO> simpleCVDTOs = mappingUtils.mapList(CVsFromDB, SimpleCVDTO.class);
        return new ResponseEntity<>(simpleCVDTOs, HttpStatus.ACCEPTED);

    }

    @GetMapping(path = "knowlegde", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllKnowlegde() {
        List<KnowledgeDTO> knowledgeDTOS = mappingUtils.mapList(cvService.getAllKnowledge(), KnowledgeDTO.class);
        return new ResponseEntity<>(knowledgeDTOS, HttpStatus.ACCEPTED);

    }


    @GetMapping(path = "cvToPrint/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPDFofCV(@PathVariable int id) throws FileNotFoundException, DocumentException {
        CV cV = cvService.getCVByCVID(id);
        Document docOne = new Document();
        PdfWriter.getInstance(docOne, new FileOutputStream("CV_PDF.pdf"));
        docOne.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK);
        String theCV =
                "CV \nName: " + cV.getConsultant().getName() +
                        "\nEmail: " + cV.getConsultant().getEmail() +
                        "\nPhone number: " + cV.getConsultant().getPhone() +
                        "\nMax Hours: " + cV.getMaxHours() +
                        "\nDescription: " + cV.getDescription() +
                        "\nTitle: " + cV.getTitle() +
                        "\nEducation: " + cV.getEducations();
        Chunk chunk = new Chunk(theCV, font);
        chunk.setWordSpacing(5);
        chunk.setLineHeight(10);
        docOne.add(chunk);
        docOne.close();

        return new ResponseEntity<>(mappingUtils.map(cV, CVDTO.class), HttpStatus.ACCEPTED);
    }
    @PostMapping(path = "cv_id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getIDForCV(@RequestBody CVDTO cvDTO) {
        try {
            CV cv = mappingUtils.mapFromDTO(cvDTO, CV.class);
            int cvID = cvService.findCVIDByCVDTO(cv);
            return new ResponseEntity<>(cvID, HttpStatus.ACCEPTED);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(path = "published/paged", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllOriginalCVWhichArePublishedByConsultants(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "12") int size) {
        logger.info("Loading page: {} wih size: {}", page, size);
        List<User> pageConsultantsFromDB;
        List<CV> cvsWhichArePublished;
        Pageable pageable = PageRequest.of(page, size);
        pageConsultantsFromDB = cvService.getPagedConsultantsWhichHasPublishedCVLinkedTo(pageable).getContent();
        logger.info("Size of the list of consultants paged {}", pageConsultantsFromDB.size());
        cvsWhichArePublished = cvService.getFirstPublishedCVsFromConsultants(pageConsultantsFromDB);
        List<SimpleCVDTO> simpleCVDTOs = mappingUtils.mapList(cvsWhichArePublished, SimpleCVDTO.class);

        return new ResponseEntity<>(simpleCVDTOs, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "published/paged/by_search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPublishedSimpleCVsDTOsWithSortScore(@RequestBody SearchFeaturesDTO searchFeaturesDTO,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "12") int size) {
        try {
            long startTime = System.currentTimeMillis();
            Pageable pageable = PageRequest.of(page, size);
            SearchFeaturesListObject searchFeaturesListObject = mappingUtils.mapFromDTO(searchFeaturesDTO, SearchFeaturesListObject.class);
            List<SimpleCVDTO> cvsOrderedBySortScoreList =
                    cvService.getListOfMappedSimpleCvsAndSortingPointsByPublishedCVs(searchFeaturesListObject, pageable);
            long endTime = System.currentTimeMillis();
            logger.info("Loaded sorted consultants paged: " + (endTime - startTime) + " ms");
            return new ResponseEntity<>(cvsOrderedBySortScoreList, HttpStatus.ACCEPTED);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StopLoadingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
        }
    }

    @PostMapping(path = "cv", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOriginalCV(@RequestBody CVDTO cvDTO) {
        try {
            CV cvCreated = cvService.createOriginalCV(mappingUtils.mapFromDTO(cvDTO, CV.class));
            CVDTO cvDTOCreated = mappingUtils.map(cvCreated, CVDTO.class);
            return new ResponseEntity<>(cvDTOCreated, HttpStatus.CREATED);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(path = "cv/user/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAOriginalCVForUser(@PathVariable int phone, @RequestBody SimpleCVDTO simpleCVDTO) {
        try {
            CVDTO cvDTO = mappingUtils.map(cvService.getAOrignalCVForUserByPhoneAndSimpleCV(phone + "", simpleCVDTO), CVDTO.class);
            return new ResponseEntity<>(cvDTO, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>("No such CV in database", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(path = "cv/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getJobsFromCV(@RequestBody CVDTO cvDTO) {
        try {
            CV cv = mappingUtils.mapFromDTO(cvDTO, CV.class);
            List<JobDTO> jobsDTO = mappingUtils.mapList(cvService.getJobsByCV(cv), JobDTO.class);
            return new ResponseEntity<>(jobsDTO, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>("No such CV in database", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(path = "cv/job/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addJobToCV(@PathVariable int cvID, @RequestBody JobDTO jobDTO) {
        try {
            Job job = mappingUtils.mapFromDTO(jobDTO, Job.class);
            List<Job> jobs = cvService.addJobsByCV(cvID, job);
            List<JobDTO> jobsDTO = mappingUtils.mapList(jobs, JobDTO.class);
            return new ResponseEntity<>(jobsDTO, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>("No jobs by that CV in database", HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(path = "cv/cv_knowledge/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCVKnowlegde(@PathVariable int cvID, @RequestBody CVs_KnowledgeDTO cvKnowledgeDTOToAdd) {
        try {
            CVs_Knowledge cVs_knowledgesToAdd = mappingUtils.mapFromDTO(cvKnowledgeDTOToAdd, CVs_Knowledge.class);
            List<CVs_KnowledgeDTO> cvs_knowledgeDTOs = mappingUtils.mapList(cvService.addCVKnowledgeToCV(cvID, cVs_knowledgesToAdd), CVs_KnowledgeDTO.class);
            return new ResponseEntity<>(cvs_knowledgeDTOs, HttpStatus.ACCEPTED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "cv/practisedLanguage/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addPractisedLanguage(@PathVariable int cvID, @RequestBody PractisedLanguageDTO practisedLanguageDTO) {
        try {
            PractisedLanguage practisedLanguage = mappingUtils.mapFromDTO(practisedLanguageDTO, PractisedLanguage.class);
            PractisedLanguageDTO practisedLanguageDBDTO = mappingUtils.map(cvService.addPractisedLanguageToCV(cvID, practisedLanguage), PractisedLanguageDTO.class);
            return new ResponseEntity<>(practisedLanguageDBDTO, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        }
    }

    @PostMapping(path = "cv/course_certification/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCourseCertification(@PathVariable int cvID, @RequestBody CoursesCertificationDTO coursesCertificationDTO) {
        try {
            CoursesCertification coursesCertification = mappingUtils.mapFromDTO(coursesCertificationDTO, CoursesCertification.class);
            List<CoursesCertificationDTO> coursesCertificationDTOS = mappingUtils.mapList(cvService.addCoursesCertificationToCV(cvID, coursesCertification), CoursesCertificationDTO.class);
            return new ResponseEntity<>(coursesCertificationDTOS, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        }
    }

    @PostMapping(path = "cv/education/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addEducation(@PathVariable int cvID, @RequestBody EducationDTO educationDTO) {
        try {
            Education education = mappingUtils.mapFromDTO(educationDTO, Education.class);
            List<EducationDTO> educationDTOS = mappingUtils.mapList(cvService.addEducationToCV(cvID, education), EducationDTO.class);
            return new ResponseEntity<>(educationDTOS, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        }
    }

    @PostMapping(path = "cv/project/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addProject(@PathVariable int cvID, @RequestBody ProjectDTO projectToAddDTO) {
        try {
            Project project = mappingUtils.mapFromDTO(projectToAddDTO, Project.class);
            ProjectDTO projectDTO = mappingUtils.map(cvService.addProjectToCV(cvID, project), ProjectDTO.class);
            return new ResponseEntity<>(projectDTO, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @DeleteMapping(path = "cv", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCV(@RequestBody CVDTO cvdto) {
        try {
            CV cv = mappingUtils.mapFromDTO(cvdto, CV.class);
            CV deletedCV = cvService.deleteCV(cv);
            CVDTO deletedCVDTO = mappingUtils.map(deletedCV, CVDTO.class);
            return new ResponseEntity<>(deletedCVDTO, HttpStatus.ACCEPTED);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("No such CV in database", HttpStatus.NOT_FOUND);
        } catch (CloneNotSupportedException e) {
            return new ResponseEntity<>("Could not clone CV", HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    @DeleteMapping(path = "cv/job", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteJobForCV(@RequestBody JobDTO jobDTO) {
        try {
            Job job = mappingUtils.mapFromDTO(jobDTO, Job.class);
            List<Job> jobs = cvService.deleteJobsByCV(job);
            List<JobDTO> jobsDTO = mappingUtils.mapList(jobs, JobDTO.class);
            return new ResponseEntity<>(jobsDTO, HttpStatus.ACCEPTED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>("No such Job in database", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(path = "cv/cv_knowledge/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCVKnowledgeFromCV(@PathVariable int cvID, @RequestBody CVs_KnowledgeDTO cVs_knowledgeDTO) {
        try {
            CVs_Knowledge cVs_knowledge = mappingUtils.mapFromDTO(cVs_knowledgeDTO, CVs_Knowledge.class);
            cvService.deleteCVKnowledgeForCV(cvID, cVs_knowledge);
            return new ResponseEntity<>(cVs_knowledgeDTO, HttpStatus.CREATED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(path = "cv", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateOriginalCVSimple(@RequestBody CVDTO[] cvDTOs) {
        try {
            CVDTO cvOriginalDTO = cvDTOs[0];
            CVDTO cvEditedToDTO = cvDTOs[1];
            CV cvOriginal = mappingUtils.mapFromDTO(cvOriginalDTO, CV.class);
            CV cvEditedTo = mappingUtils.mapFromDTO(cvEditedToDTO, CV.class);
            CV cvOriginalEdited = cvService.updateCVSimple(cvOriginal, cvEditedTo);
            CVDTO cvDTOUpdated = mappingUtils.map(cvOriginalEdited, CVDTO.class);
            return new ResponseEntity<>(cvDTOUpdated, HttpStatus.CREATED);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("Det oprindelige CV kunne ikke findes", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "cv/cv_knowledges/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCVKnowlegdeListForCV(@PathVariable int cvID, @RequestBody List<CVs_KnowledgeDTO> newCVKnowledgeDTOList) {
        try {
            List<CVs_Knowledge> newCVKnowledgeList = new ArrayList<>();
            if (!newCVKnowledgeDTOList.isEmpty()) {
                newCVKnowledgeList = mappingUtils.mapListFromDTO(newCVKnowledgeDTOList, CVs_Knowledge.class);
            }
            List<CVs_KnowledgeDTO> cvs_knowledgeDTOs = mappingUtils.mapList(cvService.updateCVKnowledgeToCV(cvID, newCVKnowledgeList), CVs_KnowledgeDTO.class);
            return new ResponseEntity<>(cvs_knowledgeDTOs, HttpStatus.ACCEPTED);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoSuchObjectException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExistingEntity e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping(path = "cv/projects/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProjectsForCV(@PathVariable int cvID, @RequestBody List<ProjectDTO> newProjectDTOList) {
        try {
            List<Project> newProjects = new ArrayList<>();
            if (!newProjectDTOList.isEmpty()) {
                newProjects = mappingUtils.mapListFromDTO(newProjectDTOList, Project.class);
            }
            List<ProjectDTO> projetDTOs = mappingUtils.mapList(cvService.updateProjectsForCV(cvID, newProjects), ProjectDTO.class);
            return new ResponseEntity<>(projetDTOs, HttpStatus.ACCEPTED);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(path = "cv/jobs/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateJobsForCV(@PathVariable int cvID, @RequestBody List<JobDTO> jobDTOs) {
        try {
            List<Job> jobs = new ArrayList<>();
            if (!jobDTOs.isEmpty()) {
                jobs = mappingUtils.mapListFromDTO(jobDTOs, Job.class);
            }
            List<JobDTO> jobFromDBDTOs = mappingUtils.mapList(cvService.updateJobsForCV(cvID, jobs), JobDTO.class);
            return new ResponseEntity<>(jobFromDBDTOs, HttpStatus.ACCEPTED);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "cv/practisedLanguage/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePractisedLanguagesForCV(@PathVariable int cvID, @RequestBody List<PractisedLanguageDTO> practisedLanguageDTOS) {
        try {
            List<PractisedLanguage> practisedLanguages = new ArrayList<>();
            if (!practisedLanguageDTOS.isEmpty()) {
                practisedLanguages = mappingUtils.mapListFromDTO(practisedLanguageDTOS, PractisedLanguage.class);
            }
            List<PractisedLanguageDTO> practisedLanguageDBDTOs = mappingUtils.mapList(cvService.updatePractisedLanguagesForCV(cvID, practisedLanguages), PractisedLanguageDTO.class);
            return new ResponseEntity<>(practisedLanguageDBDTOs, HttpStatus.ACCEPTED);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        }

    }

    @PutMapping(path = "cv/courses_certifications/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCoursesCerficationsForCV(@PathVariable int cvID, @RequestBody List<CoursesCertificationDTO> courseCertificationDTOs) {
        try {
            List<CoursesCertification> coursesCertifications = new ArrayList<>();
            if (!courseCertificationDTOs.isEmpty()) {
                coursesCertifications = mappingUtils.mapListFromDTO(courseCertificationDTOs, CoursesCertification.class);
            }
            List<CoursesCertificationDTO> coursesCertificationDBDTOS = mappingUtils.mapList(cvService.updateCoursesCertificationsForCV(cvID, coursesCertifications), CoursesCertificationDTO.class);
            return new ResponseEntity<>(coursesCertificationDBDTOS, HttpStatus.ACCEPTED);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        }
    }

    @PutMapping(path = "cv/educations/{cvID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateEducationsForCV(@PathVariable int cvID, @RequestBody List<EducationDTO> educationDTOs) {
        try {
            List<Education> educations = new ArrayList<>();
            if (!educationDTOs.isEmpty()) {
                educations = mappingUtils.mapListFromDTO(educationDTOs, Education.class);
            }
            List<EducationDTO> educationDBDTOS = mappingUtils.mapList(cvService.updateEducationsForCV(cvID, educations), EducationDTO.class);
            return new ResponseEntity<>(educationDBDTOS, HttpStatus.ACCEPTED);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ExistingEntity e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.IM_USED);
        }
    }

}
