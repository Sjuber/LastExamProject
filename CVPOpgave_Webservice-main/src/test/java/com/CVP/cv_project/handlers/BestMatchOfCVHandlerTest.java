package com.CVP.cv_project.handlers;

import com.CVP.cv_project.domain.*;
import com.CVP.cv_project.domain.Enums.LevelOfCVKnowledgeSkill;
import com.CVP.cv_project.domain.Enums.LevelOfLanguage;
import com.CVP.cv_project.repos.CVRepository;
import com.CVP.cv_project.repos.KnowledgeRepository;
import com.CVP.cv_project.repos.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class BestMatchOfCVHandlerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CVRepository cvRepository;

    @Autowired
    KnowledgeRepository knowledgeRepository;

    @BeforeEach
    public void setUp() throws Exception {
        User consultantATest = userRepository.save(new User("Anne Hansen Tester", "10199238", "a@mail.dk"));

        List<CVs_Knowledge> cVs_knowledgeListA1 = new ArrayList<>();
        List<CVs_Knowledge> cVs_knowledgeListA2 = new ArrayList<>();

        Map<String, KnowCategory> categories= new HashMap<>();

        categories.put("Industry",new KnowCategory("Industribrancher", "Hvilke brancher du har arbejdet inden for?"));
        categories.put("DatabaseSQL",new KnowCategory("SQL", "Hvilke slags databasesprog inde for SQL har du beskæftiget med før og nu?"));
        categories.put("ProgramLanguages",new KnowCategory("Programmeringssprog", "Hvilke slags programmeringssprog har du anvendt før og nu?"));
        categories.put("Technologies",new KnowCategory("Andre teknologier", "Hvilke slags andre slags teknologier har du anvendt før og nu?"));
        categories.put("Frameworks",new KnowCategory("Frameworks", "Hvilke frameworks har du anvendt før og nu?"));
        categories.put("Other",new KnowCategory("Andet", "Noget du ikke kan finde?"));
        categories.put("Development tool",new KnowCategory("Procesmodeller", "Hvilke procesmodeller har du tidligere anvendt eller anvender nu?"));

        Knowledge knowledgeTestA = new Knowledge("Spring Boot", categories.get("Frameworks"));
        Knowledge knowledgeTestB = new Knowledge("Angular", categories.get("Frameworks"));
        Knowledge knowledgeTestC = new Knowledge("Git", categories.get("Technologies"));
        Knowledge knowledgeTestD = new Knowledge("Java", categories.get("ProgramLanguages"));
        Knowledge knowledgeTestE = new Knowledge("Python", categories.get("ProgramLanguages"));
        Knowledge knowledgeTestF = new Knowledge("C#", categories.get("ProgramLanguages"));
        Knowledge knowledgeTestG = new Knowledge("C++", categories.get("ProgramLanguages"));
        Knowledge knowledgeTestH = new Knowledge("C", categories.get("ProgramLanguages"));
        Knowledge knowledgeTestI = new Knowledge("TypeScript", categories.get("ProgramLanguages"));
        Knowledge knowledgeTestJ = new Knowledge("Transportindustri", categories.get("Industry"));
        Knowledge knowledgeTestK = new Knowledge("Fødevareindustri", categories.get("Industry"));
        Knowledge knowledgeTestL = new Knowledge("Digitalindustri", categories.get("Industry"));
        Knowledge knowledgeTestM = new Knowledge("PostgresSQL", categories.get("DatabaseSQL"));
        Knowledge knowledgeTestN = new Knowledge("MySQL", categories.get("DatabaseSQL"));
        Knowledge knowledgeTestO = new Knowledge("SCRUM",categories.get("Development tool"));
        Knowledge knowledgeTestP = new Knowledge("Kanban",categories.get("Development tool"));
        Knowledge knowledgeTestQ = new Knowledge(null, categories.get("Other"));

        knowledgeRepository.save(knowledgeTestA);
        knowledgeRepository.save(knowledgeTestB);
        knowledgeRepository.save(knowledgeTestC);
        knowledgeRepository.save(knowledgeTestD);
        knowledgeRepository.save(knowledgeTestE);
        knowledgeRepository.save(knowledgeTestF);
        knowledgeRepository.save(knowledgeTestG);
        knowledgeRepository.save(knowledgeTestH);
        knowledgeRepository.save(knowledgeTestI);
        knowledgeRepository.save(knowledgeTestJ);
        knowledgeRepository.save(knowledgeTestK);
        knowledgeRepository.save(knowledgeTestL);
        knowledgeRepository.save(knowledgeTestM);
        knowledgeRepository.save(knowledgeTestN);
        knowledgeRepository.save(knowledgeTestO);
        knowledgeRepository.save(knowledgeTestP);
        knowledgeRepository.save(knowledgeTestQ);

        CVs_Knowledge cVs_knowledgeATest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Ekspert.name(), 10, "",knowledgeTestA);
        CVs_Knowledge cVs_knowledgeBTest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Ekspert.name(), 10, "",knowledgeTestD);
        CVs_Knowledge cVs_knowledgeCTest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Lettere_øvet.name(), 2, "",knowledgeTestN);
        CVs_Knowledge cVs_knowledgeETest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Erfaren.name(), 8, "",knowledgeTestI);

        CVs_Knowledge cVs_knowledgeFTest = new CVs_Knowledge(null, 8, "",knowledgeTestJ); //Industry

        cVs_knowledgeListA1.add(cVs_knowledgeATest);
        cVs_knowledgeListA1.add(cVs_knowledgeBTest);
        cVs_knowledgeListA1.add(cVs_knowledgeCTest);
        cVs_knowledgeListA1.add(cVs_knowledgeETest);

        CVs_Knowledge cVs_knowledgeDTest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Erfaren.name(), 6, "",knowledgeTestO);

        cVs_knowledgeListA2.add(cVs_knowledgeDTest);
        cVs_knowledgeListA2.add(cVs_knowledgeFTest);//Industry

        PractisedLanguage practisedLanguageA = new PractisedLanguage("Dansk", LevelOfLanguage.Modersmål.name(),LevelOfLanguage.Modersmål.name());
        PractisedLanguage practisedLanguageB = new PractisedLanguage("Engelsk", LevelOfLanguage.Flydende.name(),LevelOfLanguage.God.name());
        PractisedLanguage practisedLanguageC = new PractisedLanguage("Dansk", LevelOfLanguage.Begynder.name(),LevelOfLanguage.God.name());
        PractisedLanguage practisedLanguageD = new PractisedLanguage("Engelsk", LevelOfLanguage.Modersmål.name(),LevelOfLanguage.Modersmål.name());
        PractisedLanguage practisedLanguageF = new PractisedLanguage("Engelsk", LevelOfLanguage.God.name(),LevelOfLanguage.God.name());
        PractisedLanguage practisedLanguageE = new PractisedLanguage("Ungarnsk", LevelOfLanguage.Begynder.name(),LevelOfLanguage.Begynder.name());

        CV cvA1 = new CV(consultantATest,18, 37, "Jeg er passioneret omrking at være konsulent!", "Jeg ser mig selv som teknisk dygtig!", "En passioneret konsulent");
        CV cvA2 = new CV(consultantATest,4, 10, "Jeg er passioneret omrking at være leder!", "Jeg ser mig selv som teknisk dygtig!", "En passioneret leder");

        cvA1.setCVState(CVState.published);
        cvA2.setCVState(CVState.published);

        cvA1.setCV_KnowledgeList(cVs_knowledgeListA1);
        cvA2.setCV_KnowledgeList(cVs_knowledgeListA2);

        cvA1.getPractisedLanguages().add(practisedLanguageA);
        cvA1.getPractisedLanguages().add(practisedLanguageF);
        cvA1.getPractisedLanguages().add(practisedLanguageE);

        cvA2.getPractisedLanguages().add(practisedLanguageA);
        cvA2.getPractisedLanguages().add(practisedLanguageF);
        cvA2.getPractisedLanguages().add(practisedLanguageE);

        cvRepository.save(cvA1);
        cvRepository.save(cvA2);

        consultantATest.getCvs().add(cvA1);
        consultantATest.getCvs().add(cvA2);
    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    @Transactional
    void getWeightsAndBestMatchedCVForConsultantTestForFreeHours() {
        //Arrange
        int searchFreeHours = 8;
        User consultant = userRepository.findByPhone("10199238").get();

        List<String> priorityListNames = new ArrayList<>();
        priorityListNames.add("freeHours");
        priorityListNames.add("cvs_knowlegde");
        priorityListNames.add("industry");
        priorityListNames.add("language");

        PractisedLanguage practisedLanguageF = new PractisedLanguage("Engelsk", LevelOfLanguage.God.name(),LevelOfLanguage.God.name());

        Knowledge knowledgeTestI = new Knowledge("TypeScript", new KnowCategory("Programmeringssprog", "Hvilke slags programmeringssprog har du anvendt før og nu?"));
        CVs_Knowledge cVs_knowledgeETest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Erfaren.name(), 8, "",knowledgeTestI);

        Knowledge knowledgeTestIndustry = new Knowledge("Transportindustri", new KnowCategory("Industribrancher", "Hvilke brancher du har arbejdet inden for?"));
        CVs_Knowledge industry = new CVs_Knowledge(null, 8, "",knowledgeTestIndustry);

        List<CVs_Knowledge> cVs_knowledgeList = new ArrayList<>();
        cVs_knowledgeList.add(cVs_knowledgeETest);
        cVs_knowledgeList.add(industry);

        BestMatchOfCVHandler handlerTestFreeHours = new BestMatchOfCVHandler(consultant, cVs_knowledgeList,practisedLanguageF,searchFreeHours,priorityListNames,0);

        //Act
        CV actualCV = handlerTestFreeHours.getWeightsAndBestMatchedCVForConsultant();
        CV expectedCV = consultant.getCvs().get(0); // Getting consultant CV for first user "Anne Hansen" with title "En passioneret konsulent"

        //Assert
        assertEquals(expectedCV.getId(),actualCV.getId());
    }
    @Test
    @Transactional
    void getWeightsAndBestMatchedCVForConsultantTestForIndustry() {
        //Arrange
        int searchFreeHours = 8;
        User consultant = userRepository.findByPhone("10199238").get();

        List<String> priorityListNames = new ArrayList<>();
        priorityListNames.add("industry");
        priorityListNames.add("freeHours");
        priorityListNames.add("cvs_knowlegde");
        priorityListNames.add("language");

        PractisedLanguage practisedLanguageF = new PractisedLanguage("Engelsk", LevelOfLanguage.God.name(),LevelOfLanguage.God.name());

        Knowledge knowledgeTestI = new Knowledge("TypeScript", new KnowCategory("Programmeringssprog", "Hvilke slags programmeringssprog har du anvendt før og nu?"));
        CVs_Knowledge cVs_knowledgeETest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Erfaren.name(), 8, "",knowledgeTestI);

        Knowledge knowledgeTestIndustry = new Knowledge("Transportindustri", new KnowCategory("Industribrancher", "Hvilke brancher du har arbejdet inden for?"));
        CVs_Knowledge industry = new CVs_Knowledge(null, 8, "",knowledgeTestIndustry);

        List<CVs_Knowledge> cVs_knowledgeList = new ArrayList<>();
        cVs_knowledgeList.add(cVs_knowledgeETest);
        cVs_knowledgeList.add(industry);

        BestMatchOfCVHandler handlerTestFreeHours = new BestMatchOfCVHandler(consultant, cVs_knowledgeList,practisedLanguageF,searchFreeHours,priorityListNames,0);

        //Act
        CV actualCV = handlerTestFreeHours.getWeightsAndBestMatchedCVForConsultant();
        CV expectedCV = consultant.getCvs().get(1); // Getting consultant CV for first user "Anne Hansen" with title "En passioneret leder"

        //Assert
        assertEquals(expectedCV.getId(),actualCV.getId());
    }
    @Test
    @Transactional
    void getWeightsAndBestMatchedCVForConsultantTestForMostSearchedCompentenciesFromCVKnowledgeList() {
        //Arrange
        int searchFreeHours = 8;
        User consultant = userRepository.findByPhone("10199238").get();

        List<String> priorityListNames = new ArrayList<>();
        priorityListNames.add("cvs_knowlegde");
        priorityListNames.add("industry");
        priorityListNames.add("language");
        priorityListNames.add("freeHours");

        PractisedLanguage practisedLanguageF = new PractisedLanguage("Engelsk", LevelOfLanguage.God.name(),LevelOfLanguage.God.name());

        Knowledge knowledgeTestI = new Knowledge("TypeScript", new KnowCategory("Programmeringssprog", "Hvilke slags programmeringssprog har du anvendt før og nu?"));
        Knowledge knowledgeTestA = new Knowledge("Spring Boot", new KnowCategory("Frameworks", "Hvilke frameworks har du anvendt før og nu?"));
        Knowledge knowledgeTestD = new Knowledge("Java", new KnowCategory("Programmeringssprog", "Hvilke slags programmeringssprog har du anvendt før og nu?"));
        Knowledge knowledgeTestO = new Knowledge("SCRUM",new KnowCategory("Procesmodeller", "Hvilke procesmodeller har du tidligere anvendt eller anvender nu?"));

        CVs_Knowledge cVs_knowledgeETest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Erfaren.name(), 6, "",knowledgeTestI);
        CVs_Knowledge cVs_knowledgeATest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Erfaren.name(), 16, "",knowledgeTestA);
        CVs_Knowledge cVs_knowledgeBTest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Øvet.name(), 3, "",knowledgeTestD);
        CVs_Knowledge cVs_knowledgeCTest = new CVs_Knowledge(LevelOfCVKnowledgeSkill.Lettere_øvet.name(), 2, "",knowledgeTestO);

        Knowledge knowledgeTestIndustry = new Knowledge("Transportindustri", new KnowCategory("Industribrancher", "Hvilke brancher du har arbejdet inden for?"));
        CVs_Knowledge industry = new CVs_Knowledge(null, 8, "",knowledgeTestIndustry);

        List<CVs_Knowledge> cVs_knowledgeList = new ArrayList<>();
        cVs_knowledgeList.add(cVs_knowledgeETest);
        cVs_knowledgeList.add(cVs_knowledgeATest);
        cVs_knowledgeList.add(cVs_knowledgeBTest);
        cVs_knowledgeList.add(cVs_knowledgeCTest);

        cVs_knowledgeList.add(industry);

        BestMatchOfCVHandler handlerTestFreeHours = new BestMatchOfCVHandler(consultant, cVs_knowledgeList,practisedLanguageF,searchFreeHours,priorityListNames,0);

        //Act
        CV actualCV = handlerTestFreeHours.getWeightsAndBestMatchedCVForConsultant();
        CV expectedCV = consultant.getCvs().get(0); // Getting consultant CV for first user "Anne Hansen" with title "En passioneret konsulent"

        //Assert
        assertEquals(expectedCV.getId(),actualCV.getId());
    }

    @Disabled
    @Test
    @Transactional
    void validatingCVForCVknowledgeListToScoreListTest() {
        //Arrange
        //Act
        //Assert
    }

    @Disabled
    @Test
    @Transactional
    void validatingCVForIndustryToScoreTest() {
        //Arrange
        //Act
        //Assert
    }
    @Disabled
    @Test
    @Transactional
    void validatingCVForLanguageWithArrayTest() {
        //Arrange
        //Act
        //Assert
    }
    @Disabled
    @Test
    @Transactional
    void validatingCVForFreeHoursToScoreTestMatching() {
        //Arrange
        int searchFreeHours = 7;
        User consultant = userRepository.findByPhone("10199238").get();
        List<String> priorityListNames = new ArrayList<>();
        priorityListNames.add("freeHours");
        priorityListNames.add("cvs_knowlegde");
        priorityListNames.add("industry");
        priorityListNames.add("language");
        BestMatchOfCVHandler handlerTestFreeHours = new BestMatchOfCVHandler(consultant, new ArrayList<>(),null,searchFreeHours,priorityListNames,0);

        //Act
        int actual = handlerTestFreeHours.validatingCVForFreeHours(searchFreeHours,consultant.getCvs().get(0)); // Getting consultant CV for first user "Anne Hansen" with title "En passioneret konsulent"
        int expected = 1;

        //Assert
        assertEquals(expected,actual);
    }
    @Test
    @Transactional
    void validatingCVForFreeHoursToScoreTestNoMatch() {
        //Arrange
        int searchFreeHours = 7;
        User consultant = userRepository.findByPhone("10199238").get();
        List<String> priorityListNames = new ArrayList<>();
        priorityListNames.add("freeHours");
        priorityListNames.add("cvs_knowlegde");
        priorityListNames.add("industry");
        priorityListNames.add("language");
        BestMatchOfCVHandler handlerTestFreeHours = new BestMatchOfCVHandler(consultant, new ArrayList<>(),null,searchFreeHours,priorityListNames,0);

        //Act
        int actual = handlerTestFreeHours.validatingCVForFreeHours(searchFreeHours,consultant.getCvs().get(1)); // Getting consultant CV for first user "Anne Hansen" with title "En passioneret leder"
        int expected = 0;

        //Assert
        assertEquals(expected,actual);
    }

    @Disabled
    @Test
    @Transactional
    void validatingCVForCVknowledgeTest() {
        //Arrange
        //Act
        //Assert
    }
    @Disabled
    @Test
    @Transactional
    void validatingCVForIndustryTest() {
        //Arrange
        //Act
        //Assert
    }
    @Disabled
    @Test
    @Transactional
    void validatingCVForLanguage() {
        //Arrange
        //Act
        //Assert
    }

}