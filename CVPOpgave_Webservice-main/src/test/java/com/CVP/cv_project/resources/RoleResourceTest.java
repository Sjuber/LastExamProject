package com.CVP.cv_project.resources;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.domain.User;
import com.CVP.cv_project.domain.UserRole;
import com.CVP.cv_project.dtos.RoleDTO;
import com.CVP.cv_project.repos.RoleRepository;
import com.CVP.cv_project.repos.UserRepository;
import com.CVP.cv_project.repos.UserRoleRepository;
import com.CVP.cv_project.services.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.testcontainers.shaded.org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;


//@ExtendWith(SpringExtension.class)
//@TestPropertySource(
//        locations = "classpath:testApplication.properties")
//@WebMvcTest(RoleResource.class)
//server.port=8081 (in case needed in test properties)
@Disabled
@RunWith(SpringRunner.class)
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class RoleResourceTest {

    @Autowired
    private WebApplicationContext wac;

    //@Autowired
    //private MockMvc mvc;

    /*@MockBean
    RoleService roleService;

    @MockBean
    RoleRepository roleRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserRoleRepository userRoleRepository;
*/
    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    private static String urlRoles = "http://localhost:8080/users/v1/roles";

    RestTemplate restTemplate = new RestTemplate();


    @BeforeEach
    void setUp() {
       // mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        String phoneNumber = "20202020";
        User testUser = new User("Karl", phoneNumber, "alle@carl.dk");
        Role testPerson = new Role("Testperson");
        UserRole ur = new UserRole(null,null,testPerson);
        ur.setUser(testUser);
        testUser.addUserRole(ur);
        Role role = roleRepository.save(testPerson);
        userRepository.save(testUser);
        userRoleRepository.save(ur);
    }

    @AfterEach
    void tearDown() {
    }
/*
    //TODO Fix test !
    @Test
    void roleSalesmanToTestEndpointCreateRole() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ModelMapper modelMapper = new ModelMapper();
        RoleDTO roleDTO = modelMapper.map(new Role("Sælger"), RoleDTO.class);

        String expectedDTO = objectMapper.writeValueAsString(roleDTO);
        System.out.println(expectedDTO + "   ------------ her Cath ");
        mvc.perform(MockMvcRequestBuilders
                        .post("/users/v1/role")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(expectedDTO))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists());
    }
    @Test
    void toTestEndpointGetRoleByUsersPhoneNumber20202020() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ModelMapper modelMapper = new ModelMapper();
        String phoneNumber = "20202020";
        User testUser = new User("Karl", phoneNumber, "alle@carl.dk");
        Role testPerson = new Role("Testperson");
        UserRole ur = new UserRole(null,null,testPerson);
        ur.setUser(testUser);
        testUser.addUserRole(ur);
        roleRepository.save(testPerson);
        userRepository.save(testUser);
        userRoleRepository.save(ur);

        mvc.perform(get("/users/v1/role/user/"+phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isCreated()).andExpect(
        MockMvcResultMatchers.jsonPath("*.size()",containsInAnyOrder(testPerson.getTitle())).value(testPerson.getTitle()));
    }
}

*/

   @Test
   @Transactional
    void getRoleByIDHTTPResponeTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Role roleTester = roleRepository.findByTitle("Testperson");
        ResponseEntity<String> result = restTemplate.exchange(urlRoles + "/"+roleTester.getId(), HttpMethod.GET, entity, String.class);
        assertEquals(result.getStatusCode().toString(),"201 CREATED");
    }
    @Test
    @Transactional
    void getRoleByIDTest() {
        //ResponseEntity<RoleDTO> result = restTemplate.getForEntity(urlRoles, RoleDTO.class, param);

        //assertTrue(result.getBody().getTitle().equals("Konsulent"));

        Role roleTester = roleRepository.findByTitle("Testperson");
        RoleDTO role = restTemplate
                .getForObject(urlRoles + "/"+ roleTester.getId(), RoleDTO.class);
        Assertions.assertNotNull(role.getTitle());
        Assertions.assertEquals(role.getTitle(), "Testperson");

    }

    @Test
    void countRolesInDBTest() {
    }

    @Test
    void createRoleTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        JSONObject jsonRole = new JSONObject();
        try {
            jsonRole.put("title", "Sælger");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonNode root;
        HttpEntity<String> request = new HttpEntity<String>(jsonRole.toString(),headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlRoles,request,String.class);
        try {
            root = objectMapper.readTree(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RoleDTO resultRole = restTemplate
                .getForObject(urlRoles + "/1", RoleDTO.class);

    assertNotNull(responseEntity.getBody());
    assertNotNull(root.path("tittle").asText());
    assertEquals(resultRole.getTitle(), "Sælger");
    }
}