package com.CVP.cv_project.utils;

import com.CVP.cv_project.domain.*;
import com.CVP.cv_project.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PersistenceDummyData {

    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private CVsKnowledgeRepository cvsKnowledgeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrokerRepository brokerRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ProjectKnowledgeRepository projectKnowledgeRepository;

    Logger logger = LoggerFactory.getLogger(PersistenceDummyData.class);
    private LocalDateTime now = LocalDateTime.now();

    private String formatDate = "dd-MM-yyyy";
    private SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
    private Map<String, KnowCategory> categories= new HashMap<>();
    private Map<Integer, Company> customers = new HashMap<>();

    private HashMap<Integer,String> levelOfKnowledge = new HashMap<>();

    List<Knowledge> knowledgeList = new ArrayList<>();
    private Random r = new Random();

    public void populate(int amount) throws ChangeSetPersister.NotFoundException, ParseException, IOException {

        long startTime = System.currentTimeMillis();
        List<User> consultants = generateConsultants(amount);
        List<User> salesTeam = generateSalesEmployees(amount/10);
        
        levelOfKnowledge.put(1,"Lettere øvet");
        levelOfKnowledge.put(2,"Øvet");
        levelOfKnowledge.put(3,"Erfaren");
        levelOfKnowledge.put(4,"Ekspert");

        categories.put("Industry",new KnowCategory("Industribrancher", "Hvilke brancher du har arbejdet inden for?"));
        categories.put("DatabaseSQL",new KnowCategory("SQL", "Hvilke slags databasesprog inde for SQL har du beskæftiget med før og nu?"));
        categories.put("ProgramLanguages",new KnowCategory("Programmeringssprog", "Hvilke slags programmeringssprog har du anvendt før og nu?"));
        categories.put("Technologies",new KnowCategory("Andre teknologier", "Hvilke slags andre slags teknologier har du anvendt før og nu?"));
        categories.put("Frameworks",new KnowCategory("Frameworks", "Hvilke frameworks har du anvendt før og nu?"));
        categories.put("Other",new KnowCategory("Andet", "Noget du ikke kan finde?"));
        categories.put("Development tool",new KnowCategory("Procesmodeller", "Hvilke procesmodeller har du tidligere anvendt eller anvender nu?"));
        categories.forEach((key, category) -> categoryRepository.save(category));

        this.knowledgeList.add(new Knowledge("Spring Boot", categories.get("Frameworks")));
        this.knowledgeList.add(new Knowledge("Angular", categories.get("Frameworks")));
        this.knowledgeList.add(new Knowledge("Git", categories.get("Technologies")));
        this.knowledgeList.add(new Knowledge("Java", categories.get("ProgramLanguages")));
        this.knowledgeList.add(new Knowledge("Python", categories.get("ProgramLanguages")));
        this.knowledgeList.add(new Knowledge("C#", categories.get("ProgramLanguages")));
        this.knowledgeList.add(new Knowledge("C++", categories.get("ProgramLanguages")));
        this.knowledgeList.add(new Knowledge("C", categories.get("ProgramLanguages")));
        this.knowledgeList.add(new Knowledge("TypeScript", categories.get("ProgramLanguages")));
        this.knowledgeList.add(new Knowledge("Transportindustri", categories.get("Industry")));
        this.knowledgeList.add(new Knowledge("Fødevareindustri", categories.get("Industry")));
        this.knowledgeList.add(new Knowledge("Digitalindustri", categories.get("Industry")));
        this.knowledgeList.add(new Knowledge("PostgresSQL", categories.get("DatabaseSQL")));
        this.knowledgeList.add(new Knowledge("MySQL", categories.get("DatabaseSQL")));
        this.knowledgeList.add(new Knowledge("SCRUM",categories.get("Development tool")));
        this.knowledgeList.add(new Knowledge("Kanban",categories.get("Development tool")));
        this.knowledgeList.add(new Knowledge(null, categories.get("Other")));

        customers.put(1,new Company("TEC i Ballerup"));
        customers.put(2,new Company("Jysk"));
        customers.put(3, new Company("Fætter BR"));
        customers.put(4,new Company("Novo Nordisk"));

        userRepository.save(new User("Knud Knudsen", "20202020","Admin@42.dk"));
        User admin = userRepository.findByName("Knud Knudsen");
        String todayInDanishFormat = now.toLocalDate().getDayOfMonth() + "-" + now.toLocalDate().getMonth().getValue() + "-"+ now.toLocalDate().getYear();
        UserRole URAdmin = new UserRole(formatter.parse(todayInDanishFormat, new ParsePosition(0)),null,roleRepository.findByTitle("Administrator"));
        UserRole URConsultant = new UserRole(formatter.parse(todayInDanishFormat, new ParsePosition(0)),null,roleRepository.findByTitle("Konsulent"));
        URAdmin.setUser(admin);
        URConsultant.setUser(admin);
        admin.getUserRoles().add(URAdmin);
        userRoleRepository.save(URAdmin);
        userRoleRepository.save(URConsultant);
        userRepository.save(admin);

        customers.forEach((key, customer)->companyRepository.save(customer));
        categories.forEach((key,value) -> knowledgeRepository.saveAll(value.getKnowledgeList()));
        List<Knowledge> knowledgeListDB = knowledgeRepository.findAllKnowledge();

        List<CV> cvs = generateCVs(4, consultants, salesTeam);
        mixCVsWithProjects(cvs);
        mixKnowlegdeToCVs(cvs,knowledgeListDB);
        long endTime = System.currentTimeMillis();
        logger.info("Time generating data for " + amount + " consultants :"+ + ((endTime-startTime)/60) + " sec");
    }

    public List<User> generateSalesEmployees(int amount) throws IOException {
        Role salesRole = roleRepository.save(new Role("Sælger"));
        return generateUsers(amount,salesRole.getTitle());
    }

    public List<User> generateUsers(int amount, String jobTitle) throws IOException {
        List<User> users = new ArrayList<>();
        String firstName;
        String lastName;
        String nameTotal;
        String phone;
        String email;
        Date dateStart;
        Date dateEnd;
        String start;
        String end;
        User user;
        UserRole userRole;
        Role consultant;
        for (int i = 1; i <= amount; i++) {
            int indexName = r.nextInt(101,DataForDummies.getFirstNames().size()+1);
            firstName = DataForDummies.getFirstNames().get(r.nextInt(0,1360));
            lastName = DataForDummies.getFirstNames().get(r.nextInt(0,1360))+"sen";
            nameTotal = firstName + " " + lastName;
            phone = ""+r.nextInt(11111111, 100000000);
            email = firstName + "@" + phone + ".tester"+indexName;
            start = (String) (r.nextInt(1, 30 + 1) + "-" + r.nextInt(1, 12 + 1) + "-" + r.nextInt(2000, 2010 + 1));
            end = (String) (r.nextInt(1, 30 + 1) + "-" + r.nextInt(1, 12 + 1) + "-" + r.nextInt(2010, 2021 + 1));
            dateStart = formatter.parse(start, new ParsePosition(0));
            dateEnd = formatter.parse(end,new ParsePosition(0));
            user = new User(nameTotal, phone, email);
            consultant = roleRepository.findByTitle(jobTitle);
            userRole = new UserRole(dateStart, dateEnd, consultant);
            user = userRepository.save(user);
            userRole.setUser(user);
            user.getUserRoles().add(userRole);
            userRoleRepository.save(userRole);
            userRepository.save(user);
            System.out.println("Generated user no.: " + i );
        }
        userRepository.findAll().forEach(users :: add);
        return users;
    }

    public List<User> generateConsultants(int amount) throws IOException {
        System.out.println("Generating " + amount + " consultants...");
        Role consultantRole = roleRepository.save(new Role("Konsulent"));
        roleRepository.save(new Role("Administrator"));
        List<User> consultants = generateUsers(amount,consultantRole.getTitle());
        System.out.println("Finished generating " + amount + " consultants");
        return consultants;
   }
    public List<Project> mixCVsWithProjects(List<CV> cvs) throws ChangeSetPersister.NotFoundException {
        System.out.println("Pairing projects with CV's...");
        final long startTime = System.currentTimeMillis();
        List<Project> projects = new ArrayList<>();
        String word;
        Map<Integer, String> roles = new HashMap<>();
        roles.put(1,"Systemudvikler");
        roles.put(2,"Sælger");
        roles.put(3,"Underviser");
        roles.put(4,"Leder");
        String start;
        String end;
        Date dateStart;
        Date dateEnd;

        for (int i = 0; i < cvs.size() ; i++) {
            for (int j = 0; j < r.nextInt(7); j++) {
                if (i == j){
                    word = "stort";
                }else{
                    word = "lille";
                }
                start = (String) (r.nextInt(1,31+1) + "-"+r.nextInt(1,12+1)+"-"+r.nextInt(1900,2000));
                end = (String) (r.nextInt(1,30+1) + "-"+r.nextInt(1,12+1)+"-"+r.nextInt(2000,2021+1));
                dateStart = formatter.parse(start, new ParsePosition(0));
                dateEnd = formatter.parse(end, new ParsePosition(0));
                CV cvDB = cvRepository.findById(cvs.get(i).getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);
                Project project = new Project("Dette er et " + word + " projekt", roles.get(r.nextInt(1,roles.size()+1)),dateStart,dateEnd);
                Company company = companyRepository.findById(r.nextInt(1,customers.size()+1)).orElseThrow(ChangeSetPersister.NotFoundException::new);
                project.setCustomer(company);
                project = projectRepository.save(project);
                project.setCv(cvDB);
                List<Knowledge> knowledgeListTmp = new ArrayList<>(this.knowledgeList);
                int amountOfProjectKowledge = r.nextInt(1,(knowledgeList.size()/3)+1);
                for (int k = 0; k < amountOfProjectKowledge; k++) {
                    Knowledge knowledgeToFindForAdding = knowledgeListTmp.get(r.nextInt(0, knowledgeListTmp.size()));
                    Project_Knowledge project_knowledge = new Project_Knowledge(null, knowledgeRepository.findByName(knowledgeToFindForAdding.getName()), null);
                    knowledgeListTmp.remove(knowledgeToFindForAdding);
                    if (project_knowledge.getKnowledge().getCategory().getName().equals("Andet")) {
                        project_knowledge.setNote("Angular");
                    }
                    project = projectRepository.save(project);
                    project_knowledge.setProject(project);
                    project.addProjectKnowledge(project_knowledge);
                    projects.add(project);
                    projectKnowledgeRepository.save(project_knowledge);
                }
                cvRepository.save(cvDB);
                if(i%3 == 0){
                    Broker broker = new Broker(dateStart,dateEnd,"Netcompany",project);
                    brokerRepository.save(broker);
                }

            }
            System.out.println("Generated projekts for cv for user no.: " + cvs.get(i).getConsultant().getId() );
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Finished pairing projects with CV's in time: " + (endTime-startTime));

        return projects;
    }


    public List<CV> generateCVs(int amount, List<User> consultants, List<User> salesTeam) throws ChangeSetPersister.NotFoundException, ParseException {
        System.out.println("Generating and pairing" + amount + " CV's with " + consultants.size() + " consultants...");
        List<CV> cvs = new ArrayList<>();
        List<Integer> salesTeamsIDs = new ArrayList<>();
        User consultant;
          User author;
          CV cv;
          String start;
          String end;
          Date dateStart;
          Date dateEnd;
          PractisedLanguage languageDanish;
          PractisedLanguage languageEnglish;
          CV cvDB;
          CoursesCertification coursesCertification;
          salesTeam.forEach(user -> salesTeamsIDs.add(user.getId()));
          customers.forEach((key, customer)->companyRepository.findByName(customer.getName()));
        for (User user: consultants) {
            consultant = user;
            int amountCVs = r.nextInt(1,amount+1);
            for (int i = 1; i < amountCVs + 1; i++) {
                int randomAmountOfHours = r.nextInt(60 + 1);
                cv = new CV(consultant, randomAmountOfHours, r.nextInt(randomAmountOfHours, 60 + 1), "Nice type", "IT-gøgl","CV");
                if (i % 3 == 0) {
                    cv.setCVState(CVState.inDraft);
                } else if (i % 3 == 1) {
                    cv.setCVState(CVState.published);
                } else {
                    cv.setCVState(CVState.archived);
                }

                if(i!=1){
                    cv.setTitle(consultant.getName() + cv.getConsultant().getId() + " (" + i + ")");
                }else {
                    cv.setTitle(consultant.getName() + cv.getConsultant().getId());
                }
                languageDanish = new PractisedLanguage("Dansk", "Modersmål", "Modersmål");
                languageDanish.setCv(cv);
                cv.getPractisedLanguages().add(languageDanish);
                if(i%4 == 0) {
                    languageEnglish = new PractisedLanguage("Engelsk", "God", "Flydende");
                    languageEnglish.setCv(cv);
                    cv.getPractisedLanguages().add(languageEnglish);
                }
                Education middleSchool = new Education("Folkeskole", 1990, 1999);
                middleSchool.setCv(cv);
                cv.getEducations().add(educationRepository.save(middleSchool));
                start = (String) (r.nextInt(1, 30 + 1) + "-" + r.nextInt(1, 12 + 1) + "-" + r.nextInt(2000, 2021 + 1));
                end = (String) (r.nextInt(1, 30 + 1) + "-" + r.nextInt(1, 12 + 1) + "-" + r.nextInt(2000, 2021 + 1));
                dateStart = formatter.parse(start, new ParsePosition(0));
                dateEnd = formatter.parse(end, new ParsePosition(0));
                coursesCertification = new CoursesCertification("EDB", dateStart, dateEnd);
                cv.addCoursesCertafications(coursesCertification);
                coursesCertification.setCv(cv);
                Education university = new Education("Universitet", 2010, 2015);
                university.setCv(cv);
                cv.getEducations().add(educationRepository.save(university));
                cvDB = cvRepository.save(cv);
                String todayInDanishFormat = now.toLocalDate().getDayOfMonth() + "-" + now.toLocalDate().getMonth().getValue() + "-" + now.toLocalDate().getYear();
                String aboutAMonthInDanishFormat = now.toLocalDate().getDayOfMonth() + 1 + "-" + now.toLocalDate().getMonth().getValue() + "-" + now.toLocalDate().getYear();
                Job job = new Job("IT-konsulent", formatter.parse(todayInDanishFormat, new ParsePosition(0)), formatter.parse(aboutAMonthInDanishFormat, new ParsePosition(0)));
                job.setCv(cvDB);
                Job jobDB = jobRepository.save(job);
                List<Company> customersDB = new ArrayList<>();
                companyRepository.findAll().forEach(customersDB::add);
                job.setCompany(customersDB.get(r.nextInt(1, customersDB.size())));
                cvDB.addJob(jobDB);
                cvDB = cvRepository.save(cvDB);
                if (i % 3 == 0) {
                    author = userRepository.findById(salesTeamsIDs.get(r.nextInt(0, salesTeam.size()))).orElseThrow(ChangeSetPersister.NotFoundException::new);
                    CV cvEdited = new CV(author, cvDB);
                    cvEdited.setTitle(cv.getTitle() + " (Redigeret)");
                    Education computerSienceAP = new Education("Datamatiker", 2019, 2022);
                    cvEdited.setCVState(CVState.inDraft);
                    computerSienceAP.setCv(cvEdited);
                    educationRepository.save(computerSienceAP);
                    cvEdited.setCvOriginal(cvDB);
                    cvRepository.save(cvEdited);
                }
            }

            System.out.println("Paired CVs with consultant no.: " + (consultants.indexOf(user)+1));
        }
        cvRepository.findAll().forEach(cvs :: add);
        System.out.println("Finished pairing and generating " + amount + " CV's, with " + consultants.size() + " consultants");
        return cvs;
    }
   public List<CVs_Knowledge> mixKnowlegdeToCVs(List<CV> cvs, List<Knowledge> knowledgeList) throws ChangeSetPersister.NotFoundException {
       System.out.println("Pairing knowledge with CV's...");
       final long startTime = System.currentTimeMillis();
        List<CVs_Knowledge> knowlegdesWithCV = new ArrayList<>();
        String note;
        for (int i = 1; i <= cvs.size(); i++) {
            List<Knowledge> knowledgeNamesList = new ArrayList<>(knowledgeList);
            int cvKnowledgeAmount = r.nextInt(1,(knowledgeList.size()/2)+1); // TODO - to many?
            CV cv = cvRepository.findById(i).orElseThrow(ChangeSetPersister.NotFoundException::new); // TODO Needed ?
            for (int j = 0; j <cvKnowledgeAmount ; j++) {
                note = "";
                Knowledge knowledgeToFindForAdding = knowledgeNamesList.get(r.nextInt(0, knowledgeNamesList.size()));
                Knowledge knowledge = knowledgeRepository.findByName(knowledgeToFindForAdding.getName());
                if (knowledge.getName()==null) {
                    note = "REACT";
                }
                CVs_Knowledge cVs_knowledge = new CVs_Knowledge(levelOfKnowledge.get(r.nextInt(1,levelOfKnowledge.size()+1)), r.nextInt(20+1),note, knowledge);
                cVs_knowledge.setCv(cv);
                cVs_knowledge = cvsKnowledgeRepository.save(cVs_knowledge);
                cv.addCVToCV_Knowledge(cVs_knowledge);
                knowledgeNamesList.remove(knowledge);
            }
            System.out.println("Paired knowledge with cv for consultant no.: " + cvs.get(i-1).getConsultant().getId());
        }
        cvsKnowledgeRepository.findAll().forEach(knowlegdesWithCV :: add);
       final long endTime = System.currentTimeMillis();
       System.out.println("Finished pairing knowledge with CV's in time: " + (endTime-startTime));

       return knowlegdesWithCV;
    }

}
