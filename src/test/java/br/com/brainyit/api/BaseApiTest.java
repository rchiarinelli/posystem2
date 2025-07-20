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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Slf4j
public abstract class BaseApiTest {

    public static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:17.5-alpine3.21"));

    static {
        POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> POSTGRESQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username", () -> POSTGRESQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password", () -> POSTGRESQL_CONTAINER.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.defer-datasource-initialization", () -> "true");
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.platform", () -> "postgresql");
    }

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    static void setup() {

    }

}
