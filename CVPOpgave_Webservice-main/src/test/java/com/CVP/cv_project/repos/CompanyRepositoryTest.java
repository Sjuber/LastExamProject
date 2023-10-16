package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepoForTest;

    @BeforeEach
    void setup() {
        companyRepoForTest.save(new Company("AAA"));
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    @Test
    @Transactional
    void testFindByName() {
        //Arrange
        String expected = "AAA";
        //Act
        String actual = "";
        if (companyRepoForTest.findByName(expected).isPresent()) {
            actual = companyRepoForTest.findByName(expected).get().getName();
        }
        //Assert
        assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }
}