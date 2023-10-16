package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.KnowCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepoForTest;

    @BeforeEach
    void setup() {
        categoryRepoForTest.save(new KnowCategory("AAA", "Test description"));
        categoryRepoForTest.save(new KnowCategory("BBB", "Test description"));
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    @Test
    @Transactional
    void testFindByName() {
        //Arrange
        String expected = "AAA";
        String actual = "";
        //Act
        if (categoryRepoForTest.findByName(expected).isPresent()) {
            actual = categoryRepoForTest.findByName(expected).get().getName();
        }
        //Assert
        assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }



}