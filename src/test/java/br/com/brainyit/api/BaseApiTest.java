package br.com.brainyit.api;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Slf4j
public abstract class BaseApiTest {

//    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName
//            .parse("mockserver/mockserver")
//            .withTag("latest");
//
//    public static MockServerContainer mockServer = new MockServerContainer(MOCKSERVER_IMAGE);
//
//    static {
//        mockServer.start();
//    }

    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
//        log.info("Registering ReliaQuest base url: {}", mockServer.getEndpoint());
//        registry.add("reliaquest.base-url", () -> mockServer.getEndpoint());
    }



    @Autowired
    protected MockMvc mockMvc;

    // @Autowired
    // EntityManager entityManager;

    // @AfterEach
    // public void tearDown() {
    //     entityManager.clear();
    // }

    @BeforeAll
    static void setup() {
//        final MockServerClient client = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
//
//        client.when(request().withMethod("GET").withPath("/employees"))
//                .respond(response()
//                        .withStatusCode(200)
//                        .withBody(json(EMPLOYEES_RESPONSE)));
//
//        client.when(request().withMethod("POST").withPath("/create")
//                        .withBody(json("{\"id\":0,\"name\": \"Jane Doe\",\"salary\":60000,\"age\": 41}", MatchType.ONLY_MATCHING_FIELDS)))
//                .respond(response()
//                        .withStatusCode(200)
//                        .withBody(json(ADD_EMPLOYEE_RESPONSE)));
//
//        client.when(request().withMethod("DELETE").withPath("/delete/2"))
//                .respond(response()
//                        .withStatusCode(200)
//                        .withBody(json(REMOVED_EMPLOYEE_RESPONSE)));
    }



}
