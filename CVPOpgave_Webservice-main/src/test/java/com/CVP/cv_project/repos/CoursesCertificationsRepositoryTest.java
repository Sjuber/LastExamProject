package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.CoursesCertification;
import com.CVP.cv_project.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class CoursesCertificationsRepositoryTest {

    @Autowired
    private CoursesCertificationsRepository coursesCertRepoForTest;
    @Autowired
    private CVRepository cVRepoForTest;

    @BeforeEach
    void setup() {

        //Setup required testdata and relations required for tests
        CoursesCertification course1 = new CoursesCertification("Certificate AAA", new Date(0), new Date(10000000));
        CoursesCertification course2 = new CoursesCertification("Certificate BBB", new Date(0), new Date(10000000));
        CoursesCertification course3 = new CoursesCertification("Certificate CCC", new Date(0), new Date(10000000));

        User testUser = new User("Name AAA", "12345678", "aaa@email.com");
        CV testCv = new CV(testUser,0,37, "Test description",
                "Some more text", "CV headline");

        testCv.addCoursesCertafications(course1);
        testCv.addCoursesCertafications(course2);
        testCv.addCoursesCertafications(course3);

        cVRepoForTest.save(cVRepoForTest.save(testCv));
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    @Disabled
    @Test
    @Transactional
    void testFindAllByCVID() {
        //Arrange
        int expected = 3;
        int cvId = 1;
        //Act
        int actual = coursesCertRepoForTest.findAllByCVID(cvId).size();
        //Assert
        assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }
}