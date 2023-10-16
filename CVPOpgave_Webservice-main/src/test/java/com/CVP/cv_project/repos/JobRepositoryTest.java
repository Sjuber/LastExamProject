package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Company;
import com.CVP.cv_project.domain.Job;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepoForTest;

    @BeforeEach
    void setup() {
        jobRepoForTest.save(new Job("Title AAA", new Date(1), new Date(100000000)));
        jobRepoForTest.save(new Job("Title BBB", new Date(1), new Date(100000000)));
        jobRepoForTest.save(new Job("Title CCC", new Date(1), new Date(100000000)));
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----

    @Test
    @Transactional
    void testFindTitleByID() {
        //Arrange
        int expectedID = 1;
        String expectedTitle = "Title AAA";
        //Act
        String actual = "";
        if (jobRepoForTest.findById(expectedID).isPresent()) {
            actual = jobRepoForTest.findById(expectedID).get().getTitle();
        }
        //Assert
        Assert.assertEquals(expectedTitle, actual);
        System.out.println("Expected: \"" + expectedTitle + "\", actual: \"" + actual + "\"");
    }

    @Test
    @Transactional
    void testFindTotalIDCount() {
        //Arrange
        long expected = 3;
        //Act
        long actual = jobRepoForTest.count();
        //Assert
        Assert.assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }


    @Disabled
    @Test
    @Transactional
    void findAllByCVTest() {
        /*
        //Arrange
        int expected = 10;
        //Act
        int actual = jobRepForTest.findAllByCV();
        //Assert
        assertEquals(expected, actual);*/
    }

    @Disabled
    @Test
    @Transactional
    void findJobWithIDByJobAttributesTest() {
        /*
        //Arrange
        int expected = 0;
        //Act
        int actual = jobRepForTest.findAllKnowledge().size();
        //Assert
        assertNotEquals(expected, actual);*/
    }

    @Disabled
    @Test
    @Transactional
    void deleteAllByCVTest() {

        //TODO lav test
    }

}