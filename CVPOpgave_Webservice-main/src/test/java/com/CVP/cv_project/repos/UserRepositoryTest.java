package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import com.CVP.cv_project.domain.Job;
import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.domain.User;
import com.CVP.cv_project.domain.UserRole;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.java.SimpleFormatter;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepoForTest;

    @Autowired
    private RoleRepository roleRepoForTest;
    @Autowired
    private UserRoleRepository userRoleRepoForTest;

    @Autowired
    private KnowledgeRepository knowledgeRepoForTest;

    @Autowired
    private CVRepository cvRepoForTest;


    @BeforeEach
    void setup() {
        String formatDate = "dd-MM-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        userRepoForTest.save(new User("Name AAA", "12345678", "aaa@email.com"));
        userRepoForTest.save(new User("Knud", "20202020", "Admin@42.dk"));
        UserRole URMaleAdmin = new UserRole(formatter.parse("10-10-2010",new ParsePosition(0)), null, roleRepoForTest.findByTitle("Administrator"));
        UserRole URMaleConsultant = new UserRole(formatter.parse("20-10-2012", new ParsePosition(0)), null, roleRepoForTest.findByTitle("Konsulent"));
        User knud = userRepoForTest.findByName("Knud");
        URMaleAdmin.setUser(knud);
        URMaleConsultant.setUser(knud);
        knud.getUserRoles().add(URMaleAdmin);
        userRoleRepoForTest.save(URMaleAdmin);
        userRoleRepoForTest.save(URMaleConsultant);
        userRepoForTest.save(knud);

        Role roleConsultant = new Role("Konsulent");
        Role roleAdmin = new Role("Administrator");
        roleRepoForTest.save(roleConsultant);
        roleRepoForTest.save(roleAdmin);
        User user1 = new User("Brunhilde Nethermann", "20292029", "dengaleQuin@gmail.dk");
        user1.addUserRole(new UserRole(new Date("20-01-2022"), null, roleConsultant));
        User user2 = new User("Admiral Von Scheider", "20502030", "dengaleAdmiral@gmail.dk");
        user1.addUserRole(new UserRole(new Date("20-01-2022"), null, roleConsultant));
        user1.addUserRole(new UserRole(new Date("20-01-2022"), null, roleAdmin));
        user1 = userRepoForTest.save(user1);
        user2 = userRepoForTest.save(user2);

        CV cv1 = new CV(user1, 20, 40, "Jeg er en gal kvinde - muhahaha! ", "Men jeg er god til de tekniske!", user1.getName() + " (1)");
        CV cv2 = new CV(user1, 30, 40, "Jeg er en gal kvinde - muhahaha! ", "Men jeg er god til de tekniske!", user1.getName() + " (2)");
        CV cv3 = new CV(user1, 20, 60, "Jeg er en gal kvinde - muhahaha! ", "Men jeg er god til de tekniske!", user1.getName() + " (3)");
        CV cv4 = new CV(user1, 20, 40, "Jeg er en gal kvinde - muhahaha! ", "Men jeg er god til de tekniske!", user1.getName() + " (4)");
        CV cv5 = new CV(user2, 20, 40, "Jeg er en gal Admiral - muhahaha! ", "Men jeg er god til de tekniske!", user2.getName() + " (1)");
        CV cv6 = new CV(user2, 20, 40, "Jeg er en gal Admiral - muhahaha! ", "Men jeg er god til de administrative!", user2.getName() + " (2)");
        cv1.setCVState(CVState.published);
        cv2.setCVState(CVState.published);
        cv3.setCVState(CVState.published);
        cv4.setCVState(CVState.published);
        cv5.setCVState(CVState.published);
        cv6.setCVState(CVState.published);

        List<CVs_Knowledge> cVsKnowledges1 = new ArrayList<>();
        List<CVs_Knowledge> cVsKnowledges2 = new ArrayList<>();
        List<CVs_Knowledge> cVsKnowledges3 = new ArrayList<>();
        List<CVs_Knowledge> cVsKnowledges4 = new ArrayList<>();
        KnowCategory categoryProcess = new KnowCategory("Processmodeller", "Hvikle procesmodeller har du tidligere anvendt?");
        KnowCategory categoryTek = new KnowCategory("Teknologier", "Hvilke teknologier har du anvendt?");
        KnowCategory categoryDBs = new KnowCategory("Databaser", "Hvilke slags databaser har du beskæftiget med?");
        Knowledge knowledge1 = knowledgeRepoForTest.save(new Knowledge("SCRUM", categoryProcess));
        Knowledge knowledge2 = knowledgeRepoForTest.save(new Knowledge("Git", categoryTek));
        Knowledge knowledge3 = knowledgeRepoForTest.save(new Knowledge("PostgresSQL", categoryDBs));
        knowledge1 = knowledgeRepoForTest.save(knowledge1);
        knowledge2 = knowledgeRepoForTest.save(knowledge2);
        knowledge3 = knowledgeRepoForTest.save(knowledge3);

        CVs_Knowledge ck1 = new CVs_Knowledge("Ekspert", 9, null, knowledge1);
        CVs_Knowledge ck2 = new CVs_Knowledge("Øvet", 9, null, knowledge1);
        CVs_Knowledge ck3 = new CVs_Knowledge("Erfaren", 9, null, knowledge1);
        CVs_Knowledge ck4 = new CVs_Knowledge("Lettere øvet", 9, null, knowledge1);
        CVs_Knowledge ck5 = new CVs_Knowledge("Ekspert", 9, null, knowledge2);
        CVs_Knowledge ck6 = new CVs_Knowledge("Øvet", 9, null, knowledge2);
        CVs_Knowledge ck7 = new CVs_Knowledge("Øvet", 9, null, knowledge3);
        CVs_Knowledge ck8 = new CVs_Knowledge("Lettere øvet", 9, null, knowledge3);
        cVsKnowledges1.add(ck1);
        cVsKnowledges1.add(ck5);
        cVsKnowledges1.add(ck8);
        cVsKnowledges2.add(ck2);
        cVsKnowledges2.add(ck6);
        cVsKnowledges2.add(ck7);
        cVsKnowledges3.add(ck3);
        cVsKnowledges4.add(ck4);

        cv1.setCV_KnowledgeList(cVsKnowledges1);
        cv2.setCV_KnowledgeList(cVsKnowledges2);
        cv5.setCV_KnowledgeList(cVsKnowledges3);
        cv6.setCV_KnowledgeList(cVsKnowledges4);

        cvRepoForTest.save(cv1);
        cvRepoForTest.save(cv2);
        cvRepoForTest.save(cv3);
        cvRepoForTest.save(cv4);
        cvRepoForTest.save(cv5);
        cvRepoForTest.save(cv6);
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----

    @Disabled
    @Transactional
    @Test
    void testFindUserByName() {
        //Arrange
        String expected = "Name AAA";
        //Act
        String actual = userRepoForTest.findByName(expected).getName();
        //Assert
        Assert.assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }

@Disabled
    @Transactional
    @Test
    void testFindUserByPhone() throws ChangeSetPersister.NotFoundException {
        //Arrange
        String expected = "12345678";
        //Act
        String actual = userRepoForTest.findByPhone(expected).get().getPhone();
        //Assert
        assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }


    @Disabled
    @Transactional
    @Test
    void testFindUserByEmail() throws ChangeSetPersister.NotFoundException {
        //Arrange
        String expected = "aaa@email.com";
        //Act
        String actual = userRepoForTest.findByEmail(expected).get().getEmail();
        //Assert
        assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }


    // ---- INTEGRATION TESTS ----
    // ---- INTEGRATION TESTS ----
    // ---- INTEGRATION TESTS ----
    //TODO Disabled test
    @Disabled
    @Test
    void findUserByEmailForAdminTest() throws ChangeSetPersister.NotFoundException {
        /*
        //Arrange
        User anne = userRepoForTest.save(new User("Anne", "20190293", "Admin2@42.dk"));
        //User anne = new User("Anne", "20190293","Admin2@42.dk");
        UserRole URFemaleAdmin = new UserRole(formatter.parse(now.toLocalDate().toString(), new ParsePosition(0)), null, roleRepoForTest.findByTitle("Administrator"));
        UserRole URFemaleConsultant = new UserRole(formatter.parse(now.toLocalDate().toString(), new ParsePosition(0)), null, roleRepoForTest.findByTitle("Konsulent"));
        //User anne = userRepoForTest.findByName("Anne");
        URFemaleAdmin.setUser(anne);
        URFemaleConsultant.setUser(anne);
        UserRole urAdmin = userRoleRepoForTest.save(URFemaleAdmin);
        UserRole urConsultent = userRoleRepoForTest.save(URFemaleConsultant);
        anne.getUserRoles().add(urAdmin);
        anne.getUserRoles().add(urConsultent);
        userRepoForTest.save(anne);

        String adminEmail = "Admin2@42.dk";
        //Act
        User actual = userRepoForTest.findByEmail(adminEmail).orElseThrow();
        //Assert
        assertNotEquals(null, actual);
        assertEquals("20190293", actual.getPhone());

         */
    }


    // ---- UTILITIY ----
    // ---- UTILITIY ----
    // ---- UTILITIY ----
    public List<User> generateConsultants(int amount) {
        /*
        List<User> consultants = new ArrayList<>();
        roleRepoForTest.save(new Role("Konsulent"));
        roleRepoForTest.save(new Role("Administrator"));
        char letter;
        String name;
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
            letter = (char) (r.nextInt(1, 26) + 'a');
            name = "" + letter + "" + letter + "" + letter + "" + letter + "" + letter + "   " + i + "Tester";
            phone = "+" + r.nextInt(11111111, 100000000);
            email = letter + "@" + phone + ".tester";
            start = (String) (r.nextInt(1, 30 + 1) + "-" + r.nextInt(1, 12 + 1) + "-" + r.nextInt(2000, 2010 + 1));
            end = (String) (r.nextInt(1, 30 + 1) + "-" + r.nextInt(1, 12 + 1) + "-" + r.nextInt(2010, 2021 + 1));
            dateStart = formatter.parse(start, new ParsePosition(0));
            dateEnd = formatter.parse(end, new ParsePosition(0));
            user = new User(name, phone, email);
            consultant = roleRepoForTest.findByTitle("Konsulent");
            userRole = new UserRole(dateStart, dateEnd, consultant);
            user = userRepoForTest.save(user);
            userRole.setUser(user);
            user.getUserRoles().add(userRole);
            userRoleRepoForTest.save(userRole);
            userRepoForTest.save(user);
        }
        userRepoForTest.findAll().forEach(consultants::add);
        return consultants;

         */
        return null; //REMOVE THIS
    }

   //@Test
   //void testingGetSizeOfUsersListWhoHasCVKnowledgeMatchForPublishedCVs(){
   //    //Arrange
   //    int expectedAmountOfUsers = 2;
   //    List<CVs_Knowledge> cVs_knowledgeListToSortBy = new ArrayList<>();
   //    CVs_Knowledge cVs_knowledgeToSortBy = new CVs_Knowledge("Øvet",2, null,
   //            new Knowledge("SCRUM",
   //                    new KnowCategory("Procesmodeller","Hvikle procesmodeller har du tidligere anvendt?")));
   //    cVs_knowledgeListToSortBy.add(cVs_knowledgeToSortBy);
   //    Pageable pageable = PageRequest.of(0, 12);
   //    //Act
   //    List<Map<User,Integer>> resultUsersList = userRepoForTest.findAllUsersWithPublishedCVsAndCVKnowledgeFromListPerfectMatchOrBetter(
   //            cVs_knowledgeToSortBy.getKnowledge().getName(), cVs_knowledgeToSortBy.getLevelSkill(),0,pageable).getContent();
   //    //Assert
   //    assertEquals(expectedAmountOfUsers, resultUsersList.size());
   //}

}