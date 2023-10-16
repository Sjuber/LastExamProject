package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.KnowCategory;
import com.CVP.cv_project.domain.Knowledge;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class KnowledgeRepositoryTest {


    //Autowired
    @Autowired
    private KnowledgeRepository knowledgeRepoForTest;
    @Autowired
    private CategoryRepository categoryRepoForTest;

    @BeforeEach
    void setup() {
        knowledgeRepoForTest.save(new Knowledge("Knowledge AAA", categoryRepoForTest.save(new KnowCategory("Category AAA", "Test description"))));
    }




    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    @Test
    @Transactional
    void testFindKnowledgeByName() {
        //Arrange
        String expected = "Knowledge AAA";
        //Act
        String actual = knowledgeRepoForTest.findByName(expected).getName();
        //Assert
        Assert.assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }

    @Test
    @Transactional
    void testFindAllKnowledge() {
        //Arrange
        int expected = 0;
        //Act
        int actual = knowledgeRepoForTest.findAllKnowledge().size();
        //Assert
        assertNotEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }
}