package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.Education;
import com.CVP.cv_project.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class EducationRepositoryDeleteTest {

    @Autowired
    private EducationRepository educationRepoForTest;
    @Autowired
    private CVRepository cVRepoForTest;

    @BeforeEach
    void setup() throws ParseException {

        //Setup required testdata and relations required for tests
        Education edu1 = new Education("Education AAA", 2016,2018);
        Education edu2 = new Education("Education BBB", 2014,2017);
        Education edu3 = new Education("Education CCC", 2012,2017);


        //Constructing testCv1
        User testUser1 = new User("Name AAA", "12345678", "aaa@email.com");
        CV testCv1 = new CV(testUser1,0,37, "Test description", "Some more text", "some title");

        testCv1.addEducation(edu1);
        testCv1.addEducation(edu2);
        testCv1.addEducation(edu3);

        cVRepoForTest.save(cVRepoForTest.save(testCv1));


        //Constructing testCv2
        User testUser2 = new User("Name BBB", "23853948", "bbb@email.com");
        CV testCv2 = new CV(testUser2,5,20, "Test description", "Some more text", "some title");

        testCv2.addEducation(edu3);

        cVRepoForTest.save(cVRepoForTest.save(testCv2));
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    @Disabled
    @Test
    @Transactional
    void testDeleteAllByCV() {
        //Arrange
        int cvId = 1;
        int expected = 0;
        //Act
        educationRepoForTest.deleteAllByCV(cvId); //Method for testing
        int actual = educationRepoForTest.findAllByCVID(cvId).size(); //Checking the result
        //Assert
        assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }
}