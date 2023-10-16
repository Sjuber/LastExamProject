package com.CVP.cv_project.resources;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.KnowCategory;
import com.CVP.cv_project.domain.Knowledge;
import com.CVP.cv_project.domain.User;
import com.CVP.cv_project.repos.CVRepository;
import com.CVP.cv_project.repos.CategoryRepository;
import com.CVP.cv_project.repos.KnowledgeRepository;
import com.CVP.cv_project.repos.UserRepository;
import com.CVP.cv_project.services.CVService;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.yaml.snakeyaml.reader.StreamReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CVResource.class)
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class CVResourceTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    KnowledgeRepository knowledgeRepository;
    @MockBean
    CVResource cvResource;
    @MockBean
    CVService cvService;
    @MockBean
    PopulateDummyDataResource populateDummyDataResource;
    @MockBean
    RoleResource roleResource;
    @MockBean
    UserResource userResource;

    KnowCategory knowCat = new KnowCategory("Category AAA", "Some description");
    Knowledge know1 = new Knowledge("Know AAA", knowCat);
    Knowledge know2 = new Knowledge("Know BBB", knowCat);
    Knowledge know3 = new Knowledge("Know CCC", knowCat);

    @BeforeEach
    void setUp() {
    }
    private final String url = "http://localhost:8080/";

    //Reference https://www.baeldung.com/integration-testing-a-rest-api
    //MockMvc https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/

    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----
    // ---- UNIT TESTS ----

    @Test
    public void testGetAllKnowlegde() throws Exception {
        //Arrange
        //List of objects "acting" as the database return from "findAllKnowledge()"-method
        List<Knowledge> knowledgeList = new ArrayList<>(Arrays.asList(know1,know2,know3));
        Mockito.when(knowledgeRepository.findAllKnowledge()).thenReturn(knowledgeList);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/v1/cvs/knowlegde") //TODO OBS!! check spelling
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    // ---- INTEGRATION TESTS ----
    // ---- INTEGRATION TESTS ----
    // ---- INTEGRATION TESTS ----

    @Disabled
    @Test
    void testGetAllOriginalCVsForUserHTTPCODE() throws IOException {
        //Arrange
        String phone = "71644029";
        String urlEndpoint = url + "users/v1/cvs/user/" + phone;
        HttpUriRequest request = new HttpGet(urlEndpoint);

        int expected = 202;

        //Act
        int actual = HttpClientBuilder.create().build().execute(request).getCode();

        //Assert
        Assert.assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }

    @Disabled
    @Test
    void testGetAllOriginalCVsForUserMEDIA() throws IOException {
        //Arrange
        String phone = "71644029";
        String urlEndpoint = url + "users/v1/cvs/user/" + phone;
        HttpUriRequest request = new HttpGet(urlEndpoint);

        String expected = "application/json";

        //Act
        String actual = HttpClientBuilder.create().build().execute(request).getEntity().getContentType();

        //Assert
        Assert.assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }

    @Disabled
    @Test
    void testGetAllOriginalCVsForUserPAYLOAD() throws IOException, JSONException {
        //Arrange
        String expected = "71644029";
        String urlEndpoint = url + "users/v1/cvs/user/" + expected;
        HttpUriRequest request = new HttpGet(urlEndpoint);

        //Act
            //Deserialize response to jsonObjects
        byte[] contentBytes = HttpClientBuilder.create().build().execute(request).getEntity().getContent().readAllBytes();
        JSONArray json = new JSONArray(new String(contentBytes));

            //Walking through the data structure
        JSONObject jsonAuthorDTO = (JSONObject)json.getJSONObject(0).get("authorDTO");
        String actual = jsonAuthorDTO.getString("phone");

        //Assert
        Assert.assertEquals(expected, actual);
        System.out.println("Expected: \"" + expected + "\", actual: \"" + actual + "\"");
    }
}