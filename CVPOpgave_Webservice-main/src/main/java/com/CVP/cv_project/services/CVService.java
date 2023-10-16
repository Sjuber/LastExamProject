package com.CVP.cv_project.services;

import com.CVP.cv_project.domain.*;
import com.CVP.cv_project.dtos.SimpleCVDTO;
import com.CVP.cv_project.dtos.utils.MappingUtils;
import com.CVP.cv_project.exceptions.ExistingEntity;
import com.CVP.cv_project.exceptions.StopLoadingException;
import com.CVP.cv_project.handlers.BestMatchOfCVHandler;
import com.CVP.cv_project.handlers.SearchFeaturesListObject;
import com.CVP.cv_project.handlers.SortByHandler;
import com.CVP.cv_project.repos.*;
import com.CVP.cv_project.resources.CVResource;
import com.github.dockerjava.api.exception.NotFoundException;
import net.minidev.asm.ex.NoSuchFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
@Service
@EnableScheduling
@Configuration
public class CVService {

    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private ProjectKnowledgeRepository projectKnowledgeRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CVsKnowledgeRepository cvsKnowledgeRepository;
    @Autowired
    private CoursesCertificationsRepository coursesCertificationsRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private PractisedLanguageRepository practisedLanguageRepository;
    @Autowired
    private BrokerRepository brokerRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private static final MappingUtils mappingUtils = new MappingUtils();

    Logger log = LoggerFactory.getLogger(CVService.class);

    public CV createOriginalCV(CV cvToCreate) throws NoSuchObjectException, NoSuchFieldException {
        if (cvToCreate.equals(null)) {
            throw new NoSuchObjectException("There is no CV to be created");
        } else if (cvToCreate.getConsultant() == null || cvToCreate.getAuthor() == null
                || cvToCreate.getConsultant().getEmail() == null || cvToCreate.getAuthor().getPhone() == null) {
            throw new NoSuchFieldException("CV must have a author and a consultant \n - they cannot be empty");
        } else {
            User authorNConsultant = userRepository.findByPhone(cvToCreate.getConsultant().getPhone()).get();
            cvToCreate.setAuthor(authorNConsultant);
            cvToCreate.setConsultant(authorNConsultant);
            cvToCreate.setTitle(cvToCreate.getTitle());
            return cvRepository.save(cvToCreate);
        }
    }

    public CV getCVByCVID(int cViD){
        return cvRepository.findById(cViD).get();
    }

    public boolean isCVTooOld(int cvId, int maxAgeInMinutes) {
        //We aquire the cv so that we may pull data out of it
        CV cv = getCVByCVID(cvId);
        if (cv == null){
            return false;
        }
        //This aquires the timestamp of lastUpdated
        Timestamp timeOfLastUpdate = cv.getLastUpdated();
        //We calculate the two against eachother
        long millisecondsAboveLimit = Timestamp.from(Instant.now()).getTime() - timeOfLastUpdate.getTime();
        //  log.info("millisecondsAboveLimit: " + millisecondsAboveLimit +" is the time from 1970: " + Timestamp.from(Instant.now()).getTime() + " minus the amount of time up until the last update: " + timeOfLastUpdate.getTime());
        long minutesAboveLimit = millisecondsAboveLimit / 60000;
        //log.info("The result of " + millisecondsAboveLimit + " turned into minutes is " + minutesAboveLimit);
        //Here the function tests if the minutes above the limit is greater than the maxAgeInMinutes
        log.info("Here we test if " + minutesAboveLimit + " is passed by " + maxAgeInMinutes);

        if (minutesAboveLimit >= maxAgeInMinutes) {
            log.info("CVID " + cvId + " MUST BE UPDATED SOON >:O \n\n");
        } else {
            log.info("CVID " + cvId + " checks out, and doesent need updating right now + :>) \n\n");
        }

        return minutesAboveLimit >= maxAgeInMinutes;

        //If true, then the cv is too old, if false, the cv is young enough
    }

    @Scheduled(fixedRate = 10000)
    public void checkIfTime() {
        //log.info(cvRepository.getAllCVs().toString());
        cvRepository.getAllCVs().forEach((n) -> isCVTooOld(n.getId(),60));
        //boolean checkAge = isCVTooOld(8, 60);
    }


    public List<CV> getAllOrignalCVsForUserByPhone(String phone) throws NoSuchFieldException {
        List<CV> originalCVs = new ArrayList<>();
        List<CV> cvsByAuthorPhone = cvRepository.findAllCVsByAuthorPhone(phone).orElseThrow(NoSuchFieldException::new);
        cvsByAuthorPhone.forEach(cv -> {
            if (cv.getConsultant().getId() == cv.getAuthor().getId()) {
                originalCVs.add(cv);
            }
        });
        return originalCVs;
    }

    public Page<User> getPagedConsultantsWhichHasPublishedCVLinkedTo(Pageable pageable) {
        return userRepository.findAllPagedConsultantsWhoHasPublishedCVs(pageable);
    }

    public List<CV> getAllOrignalNotInDraftmodeCVs() {
        return cvRepository.findAllPublished();
    }

    public CV getAOrignalCVForUserByPhoneAndSimpleCV(String authorPhone, SimpleCVDTO simpleCVDTO) throws NoSuchFieldException {
        return cvRepository.findByAllAttributes(simpleCVDTO, authorPhone).orElseThrow(NoSuchFieldException::new);
    }

    public List<Job> getJobsByCV(CV cv) throws NoSuchFieldException {
        return jobRepository.findAllByCV(cv).orElseThrow(NoSuchFieldException::new);
    }

    public List<Job> addJobsByCV(int CV_ID, Job job) throws NoSuchFieldException {
        if (job == null) {
            throw new NoSuchFieldException("Job is empty");
        } else {
            CV cvDB = cvRepository.findById(CV_ID).get();
            Company company = companyRepository.findByName(job.getCompany().getName()).orElse(null);
            if (company == null) {
                company = new Company(job.getCompany().getName());
                company = companyRepository.save(company);
            }
            job.setCompany(company);
            job.setCv(cvDB);
            jobRepository.save(job);
            return jobRepository.findAllByCV(job.getCv()).get();
        }
    }

    public List<Job> deleteJobsByCV(Job job) throws NoSuchFieldException {
        List<Job> jobs = new ArrayList<>();
        if (job != null) {
            Job jobDB = jobRepository.findJobWithIDByJobAttributes(job).orElseThrow(NoSuchFieldException::new);
            jobDB.setCompany(null);
            jobRepository.delete(jobDB);
            return jobRepository.findAllByCV(jobDB.getCv()).get();
        }
        return jobs;
    }

    public CV updateCVSimple(CV cvOriginal, CV cvEditedTo) throws ChangeSetPersister.NotFoundException {
        CV cvDB = cvRepository.findByAllCVAttributes(cvOriginal, cvOriginal.getCVState()).orElseThrow(ChangeSetPersister.NotFoundException::new);
        cvDB.setCVState(cvEditedTo.getCVState());
        cvDB.setBookedHours(cvEditedTo.getBookedHours());
        cvDB.setMaxHours(cvEditedTo.getMaxHours());
        cvDB.setDescription(cvEditedTo.getDescription());
        cvDB.setTechBackground(cvEditedTo.getTechBackground());
        return cvRepository.save(cvDB);
    }

    public CV deleteCV(CV cv) throws ChangeSetPersister.NotFoundException, CloneNotSupportedException, IllegalArgumentException {
        if (cvRepository.findCVByCVOriginal(cv).size() == 1) {
            CV cvDB = cvRepository.findByAllCVAttributes(cv, cv.getCVState()).orElseThrow(ChangeSetPersister.NotFoundException::new);
            CV cvToDelete = (CV) cvDB.clone();
            cvDB.setConsultant(null);
            cvDB.setAuthor(null);
            cvDB.getJobs().forEach(job -> {
                job.setCompany(null);
            });
            cvDB.getKnowledgeList().forEach(cv_knowledge -> {
                cv_knowledge.setCv(null);
                cv_knowledge.setKnowledge(null);
                cvsKnowledgeRepository.delete(cv_knowledge);
            });
            cvDB.getCoursesCertifications().forEach(coursesCertification -> {
                coursesCertification.setCv(null);
                coursesCertificationsRepository.delete(coursesCertification);
            });
            cvDB.getPractisedLanguages().forEach(practisedLanguage -> {
                practisedLanguage.setCv(null);
                practisedLanguageRepository.delete(practisedLanguage);
            });
            cvDB.getEducations().forEach(education -> {
                education.setCv(null);
                educationRepository.delete(education);
            });
            cvDB.getProjects().forEach(project -> {
                project.setCv(null);
                project.setCustomer(null);
                project.getProjectsKnowledgeList().forEach(project_knowledge -> {
                    project_knowledge.setKnowledge(null);
                });
                projectRepository.delete(project);
            });
            cvRepository.deleteById(cvDB.getId());
            return cvToDelete;
        } else {
            throw new IllegalArgumentException("CV is edited by another user or is not found - cannot delete");
        }
    }

    public List<Knowledge> getAllKnowledge() {
        return knowledgeRepository.findAllKnowledge();
    }

    public List<CVs_Knowledge> addCVKnowledgeToCV(int cvID, CVs_Knowledge cvknowledgeToAdd) throws NotFoundException, NoSuchObjectException, ExistingEntity {
        try {
            Knowledge knowledge = null;
            CV cvDB = cvRepository.findById(cvID).get();
            cvknowledgeToAdd.setCv(cvDB);
            if ((!cvknowledgeToAdd.getKnowledge().getName().equals(""))) {
                CVs_Knowledge existing_cVs_knowledge = cvsKnowledgeRepository.findByKnowledgeNameAndCV(cvknowledgeToAdd).orElse(null);
                if (existing_cVs_knowledge != null) {
                    throw new ExistingEntity("Knowledge for CV already exist");
                } else {
                    knowledge = knowledgeRepository.findByName(cvknowledgeToAdd.getKnowledge().getName());
                }
            } else if (cvknowledgeToAdd.getNote() != "" && cvknowledgeToAdd.getNote() != null) {
                if (cvsKnowledgeRepository.findByNoteAndCV(cvknowledgeToAdd.getNote(), cvDB).orElse(null) == null) {
                    KnowCategory categoryDB = categoryRepository.findByName(cvknowledgeToAdd.getKnowledge().getCategory().getName()).get();
                    //Because Category "Andet" has only one Knowlegde (which has "name" as null)
                    knowledge = categoryDB.getKnowledgeList().get(0);
                }
            }
            if (knowledge != null) {
                cvknowledgeToAdd.setKnowledge(knowledge);
                cvsKnowledgeRepository.save(cvknowledgeToAdd);
                return cvsKnowledgeRepository.findAllByCV(cvDB);
            } else {
                throw new NoSuchObjectException("Knowledge for CV must contain either note or knowledge name");
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("No such category with name");
        }
    }

    public int findCVIDByCVDTO(CV cv) throws ChangeSetPersister.NotFoundException {
        return cvRepository.findByAllCVAttributes(cv, cv.getCVState()).orElseThrow(ChangeSetPersister.NotFoundException::new).getId();
    }

    public void deleteCVKnowledgeForCV(int cvIDToChange, CVs_Knowledge cVs_knowledge) throws NoSuchFieldException {
        CV cvDBToChange = cvRepository.findById(cvIDToChange).orElse(null);
        cVs_knowledge.setCv(cvDBToChange);
        CVs_Knowledge cvsKnowledgeDB = null;
        if (cVs_knowledge.getLevelSkill() == null || cVs_knowledge.getLevelSkill() == "" ||
                cVs_knowledge.getKnowledge().getCategory().getName() == "" || cVs_knowledge.getKnowledge().getCategory() == null ||
                cVs_knowledge.getCv() == null) {
            throw new NoSuchFieldException("Knowledge for CV needs level of skill, a hosting cv and name for category");
        } else if (cVs_knowledge.getKnowledge() == null || cVs_knowledge.getKnowledge().getName() == null) {
            cvsKnowledgeDB = cvsKnowledgeRepository.findByNoteAndCV(cVs_knowledge.getNote(), cvDBToChange).get();
        } else {
            cvsKnowledgeDB = cvsKnowledgeRepository.findByKnowledgeNameAndCV(cVs_knowledge).get();
        }
        if (cvsKnowledgeDB != null) {
            cvsKnowledgeDB.setKnowledge(null);
            cvsKnowledgeDB.setCv(null);
            cvsKnowledgeRepository.deleteCVKnowledge(cvsKnowledgeDB);
        }
    }

    public List<CVs_Knowledge> updateCVKnowledgeToCV(int cvID, List<CVs_Knowledge> newCVKnowledgeList) throws NoSuchObjectException, ExistingEntity {
        if (newCVKnowledgeList == null) {
            throw new NoSuchObjectException("List of CVKnowledge is null");
        } else {
            cvsKnowledgeRepository.deleteAllByCV(cvID);
            for (CVs_Knowledge cvKnowlegde : newCVKnowledgeList) {
                addCVKnowledgeToCV(cvID, cvKnowlegde);
            }
        }
        return cvsKnowledgeRepository.findAllByCV(cvRepository.findById(cvID).get());
    }

    public List<Job> updateJobsForCV(int cvID, List<Job> newJobs) throws NoSuchObjectException {
        if (newJobs == null) {
            throw new NoSuchObjectException("List of Jobs is null");
        } else {
            jobRepository.deleteAllByCV(cvID);
            for (Job job : newJobs) {
                addJobsByCV(cvID, job);
            }
            return jobRepository.findAllByCV(cvRepository.findById(cvID).get()).get();
        }
    }

    public PractisedLanguage addPractisedLanguageToCV(int cvID, PractisedLanguage practisedLanguage) throws NoSuchFieldException, ExistingEntity {
        if (practisedLanguage != null && practisedLanguage.getName() != null && (!practisedLanguage.getName().equals("")) &&
                practisedLanguage.getLevelWritting() != null && (!practisedLanguage.getLevelWritting().equals("")) &&
                practisedLanguage.getLevelReading() != null && (!practisedLanguage.getLevelReading().equals(""))) {
            CV cvDB = cvRepository.findById(cvID).get();
            PractisedLanguage practisedLanguageExist = practisedLanguageRepository.findByNameAndCV(practisedLanguage.getName(), cvDB).orElse(null);
            if (practisedLanguageExist == null) {
                practisedLanguage.setCv(cvDB);
                cvDB.getPractisedLanguages().add(practisedLanguage);
                return practisedLanguageRepository.save(practisedLanguage);
            } else {
                throw new ExistingEntity("The language already exist i database");
            }
        } else {
            throw new NoSuchFieldException("The fields in practised language must nok be empty");
        }
    }

    public List<PractisedLanguage> updatePractisedLanguagesForCV(int cvID, List<PractisedLanguage> newPractisedLanguages) throws NoSuchObjectException, ExistingEntity {
        if (newPractisedLanguages == null) {
            throw new NoSuchObjectException("List of Practised Languages is null");
        } else {
            practisedLanguageRepository.deleteAllByCV(cvID);
            for (PractisedLanguage language : newPractisedLanguages) {
                addPractisedLanguageToCV(cvID, language);
            }
            return practisedLanguageRepository.findAllByCV(cvRepository.findById(cvID).get());
        }
    }

    public List<CoursesCertification> addCoursesCertificationToCV(int cvID, CoursesCertification coursesCertification) throws ExistingEntity, NoSuchFieldException {
        //Input validation
        if (coursesCertification == null ||
                coursesCertification.getTitle() == null ||
                coursesCertification.getTitle().equals("") ||
                coursesCertification.getStartDate() == null) {
            throw new NoSuchFieldException("The fields in courses or certification must not be empty");
        }

        if (coursesCertification.getEndDate() != null &&
                coursesCertification.getEndDate().before(coursesCertification.getStartDate())) {
            throw new DateTimeException("StartDate is after EndDate");
        }

        //Getting relevant CV object
        CV cvDB = cvRepository.findById(cvID).get();
        CoursesCertification coursesCertificationDB = coursesCertificationsRepository.findByTitleAndCV(coursesCertification.getTitle(), cvDB).orElse(null);

        //If course is not found on the CV, add it to the CV.
        if (coursesCertificationDB == null) {
            coursesCertification.setCv(cvDB);
            cvDB.getCoursesCertifications().add(coursesCertification);
            coursesCertificationsRepository.save(coursesCertification);

            return coursesCertificationsRepository.findAllByCV(cvDB);

        } else {
            throw new ExistingEntity("The course or certification already exists in database");
        }
    }

    public List<CoursesCertification> updateCoursesCertificationsForCV(int cvID, List<CoursesCertification> newCoursesCertifications) throws NoSuchObjectException, ExistingEntity {
        if (newCoursesCertifications == null) {
            throw new NoSuchObjectException("List of courses and certifications is null");
        } else {
            coursesCertificationsRepository.deleteAllByCV(cvID);
            for (CoursesCertification coursesCertification : newCoursesCertifications) {
                addCoursesCertificationToCV(cvID, coursesCertification);
            }
            return coursesCertificationsRepository.findAllByCV(cvRepository.findById(cvID).get());
        }
    }

    public List<Education> updateEducationsForCV(int cvID, List<Education> newEducations) throws NoSuchObjectException, ExistingEntity {
        if (newEducations == null) {
            throw new NoSuchObjectException("The fields in education must not be empty");
        } else {
            educationRepository.deleteAllByCV(cvID);
            for (Education education : newEducations) {
                addEducationToCV(cvID, education);
            }
            return educationRepository.findAllByCVID(cvRepository.findById(cvID).get().getId());
        }
    }

    public List<Education> addEducationToCV(int cvID, Education education) throws ExistingEntity, NoSuchFieldException {
        //Input validation
        if (education == null ||
                education.getTitle() == null ||
                education.getTitle().equals("") ||
                education.getStartYear() == null) {
            throw new NoSuchFieldException("The fields in education must not be empty");
        }

        if (education.getEndYear() != null &&
                education.getEndYear() < education.getStartYear()) {
            throw new DateTimeException("StartDate is after EndDate");
        }

        //Getting relevant CV object
        CV cvDB = cvRepository.findById(cvID).get();
        Education educationDB = educationRepository.findByTitleAndCV(education.getTitle(), cvDB).orElse(null);

        //If course is not found on the CV, add it to the CV.
        if (educationDB == null) {
            education.setCv(cvDB);
            cvDB.getEducations().add(education);
            educationRepository.save(education);
            return educationRepository.findAllByCVID(cvDB.getId());

        } else {
            throw new ExistingEntity("The education already exists in database");
        }
    }

    public Project addProjectToCV(int cvID, Project projectToAdd) throws ExistingEntity, NoSuchFieldException, IllegalArgumentException {
        List<Project_Knowledge> projects_knowledgeDBList = new ArrayList<>();
        Project projectDB;
        Company companyDB;
        Broker brokerToAdd = null;
        if (projectToAdd.getDateEnd() != null && projectToAdd.getDateStart().getTime() > projectToAdd.getDateEnd().getTime()) {
            throw new IllegalArgumentException("Starting date cannot happen after ending date");
        } else if (projectToAdd == null || projectToAdd.getDescription() == null || projectToAdd.getDescription() == "" ||
                projectToAdd.getCustomer().getName() == null || projectToAdd.getCustomer().getName() == "" ||
                projectToAdd.getRoleForProject() == null || projectToAdd.getRoleForProject() == "" ||
                projectToAdd.getDateStart() == null
        ) {
            throw new NoSuchFieldException("Specific fields in project must nok be empty");
        } else {
            CV cvDB = cvRepository.findById(cvID).get();
            if (projectRepository.findByAllAttributesAndCV(projectToAdd, cvDB).isPresent()) {
                throw new ExistingEntity("The project already exist i database");
            } else {
                if (!companyRepository.findByName(projectToAdd.getCustomer().getName()).isPresent()) {
                    companyRepository.save(new Company(projectToAdd.getCustomer().getName()));
                }
                companyDB = companyRepository.findByName(projectToAdd.getCustomer().getName()).get();
                projectToAdd.getProjectsKnowledgeList().forEach(project_knowledge -> {
                    Knowledge knowledgeDB;
                    KnowCategory categoryDB;
                    if (project_knowledge.getKnowledge().getCategory().getName() == "Andet") {
                        categoryDB = categoryRepository.findByName("Andet").get();
                        knowledgeDB = categoryDB.getKnowledgeList().get(0);
                    } else {
                        knowledgeDB = knowledgeRepository.findByName(project_knowledge.getKnowledge().getName());
                    }
                    project_knowledge.setKnowledge(knowledgeDB);
                    projects_knowledgeDBList.add(project_knowledge);
                });
                projectToAdd.setCustomer(companyDB);
                projectToAdd.setCv(cvDB);
                cvDB.getProjects().add(projectToAdd);
                companyDB.getProjects().add(projectToAdd);
                projectToAdd.getBroker().setProject(projectToAdd);
                if (projectToAdd.getBroker() != null) {
                    brokerToAdd = projectToAdd.getBroker();
                    projectToAdd.setBroker(null);
                }
                projectDB = projectRepository.save(projectToAdd);
                brokerToAdd.setProject(projectDB);
                brokerRepository.save(brokerToAdd);
                projects_knowledgeDBList.forEach(project_knowledge -> {
                    project_knowledge.setProject(projectDB);
                    projectKnowledgeRepository.save(project_knowledge);
                });
                return projectRepository.findByAllAttributesAndCV(projectDB, cvDB).get();
            }
        }
    }

    public List<Project> updateProjectsForCV(int cvID, List<Project> newProjects) throws ChangeSetPersister.NotFoundException {
        CV cvToUpdate = cvRepository.findById(cvID).orElseThrow(ChangeSetPersister.NotFoundException::new);
        if (cvToUpdate != null) {
            cvToUpdate.getProjects().forEach(project -> {
                project.setCustomer(null);
                project.getProjectsKnowledgeList().forEach(project_knowledge -> {
                    project_knowledge.setKnowledge(null);
                    project_knowledge.setProject(null);
                    projectKnowledgeRepository.deleteById(project_knowledge.getId());
                });
                project.setCv(null);
                brokerRepository.deleteByProjectId(project.getId());
                projectRepository.save(project);
                projectRepository.deleteByProjectID(project.getId());
            });
            newProjects.forEach(project -> {
                try {
                    addProjectToCV(cvID, project);
                } catch (ExistingEntity e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return projectRepository.findAllByCV(cvID);
    }

    public List<CV> getAllNotInDraftSimpleCVsForConsultant(String phone) {
        return cvRepository.findAllPublishedByConsulentAsAuthour(phone);
    }

    public List<CV> getFirstPublishedCVsFromConsultants(List<User> consultants) {
        List<CV> publishedCVbyChosedConsultatsFromDB = new ArrayList<>();
        consultants.forEach(consultant -> {
            for (CV cv : consultant.getCvs()) {
                if (cv.getCVState() == CVState.published) {
                    publishedCVbyChosedConsultatsFromDB.add(cv);
                    break;
                }
            }
        });
        return publishedCVbyChosedConsultatsFromDB;
    }

    public List<SimpleCVDTO> getListOfMappedSimpleCvsAndSortingPointsByPublishedCVs(
            SearchFeaturesListObject searchFeaturesListObject, Pageable pageable) throws NoSuchObjectException, StopLoadingException {
        Set<User> allUsersWithCVsPublished = new HashSet<>();
        List<BestMatchOfCVHandler> handlerList = new ArrayList<>();

        List<CVs_Knowledge> cVs_knowledgeListForSorting = searchFeaturesListObject.getCvsKnowledgeList();
        Integer notBookedHoursForSorting = searchFeaturesListObject.getNotBookedHours();
        String companyWorkedForSorting = searchFeaturesListObject.getIndustryName();
        PractisedLanguage languageForSorting = searchFeaturesListObject.getLanguage();

        if (companyWorkedForSorting != null && !companyWorkedForSorting.equals("[Branche]")) {
            Knowledge knowledge = knowledgeRepository.findByName(companyWorkedForSorting);
            cVs_knowledgeListForSorting.add(new CVs_Knowledge("Lettere øvet", 0, "", knowledge));
        }

        List<CV> allCVsPublished = cvRepository.findAllPublished();
        allCVsPublished.forEach(cv -> {
            allUsersWithCVsPublished.add(cv.getConsultant());
        });

        for (User user : allUsersWithCVsPublished.stream().toList()) {
            BestMatchOfCVHandler cvHandler = new BestMatchOfCVHandler(
                    user, cVs_knowledgeListForSorting, languageForSorting, notBookedHoursForSorting,
                    searchFeaturesListObject.getPrioritiesNamesList(), searchFeaturesListObject.getAmountMostContainCVknowledgeInCV());
            handlerList.add(cvHandler);
        }
        return getCustomePagedSortedSimpleCVDTOList(handlerList, pageable);
    }

    public List<SimpleCVDTO> getCustomePagedSortedSimpleCVDTOList(List<BestMatchOfCVHandler> handlerList, Pageable pageable){
        Collections.sort(handlerList, new SortByHandler());
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), handlerList.size());
        Page<BestMatchOfCVHandler> cvsSorted = new PageImpl<>(handlerList.subList(start, end), pageable, handlerList.size());

        List<SimpleCVDTO> simpleCVDTOSSorted = new ArrayList<>();
        cvsSorted.getContent().forEach(cvHandler -> {
            SimpleCVDTO simpleCVDTO = mappingUtils.map(cvHandler.getBestMatchedCvForConsultant(), SimpleCVDTO.class);
            simpleCVDTO.setSortScore(cvHandler.getSecondPriorityScore());
            simpleCVDTOSSorted.add(simpleCVDTO);
        });
        return simpleCVDTOSSorted;
    }

}

