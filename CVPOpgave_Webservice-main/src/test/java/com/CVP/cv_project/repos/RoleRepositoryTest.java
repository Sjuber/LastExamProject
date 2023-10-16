package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Role;
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

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepoForTest;

    @BeforeEach
    void setup() {
        roleRepoForTest.save(new Role("Role AAA"));
        roleRepoForTest.save(new Role("Role BBB"));
    }


    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    @Test
    @Transactional
    void testFindRoleByTitle() {
        //Arrange
        String expected = "Role AAA";
        //Act
        String actual = roleRepoForTest.findByTitle(expected).getTitle();
        //Assert
        assertEquals(expected, actual);
        System.out.println("Unexpected: \"" + expected + "\", actual: \"" + actual + "\"");
    }

    @Test
    @Transactional
    void testFindByUserPhone() {
        //TODO Missing test
    }
}