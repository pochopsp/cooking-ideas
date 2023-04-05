package com.github.cookingideas.infrastructure.data.repository;

import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Testcontainers
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class DatabaseRecipeRepositoryTest extends RecipeRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> database =
        new PostgreSQLContainer<>("postgres:15.2-alpine").waitingFor(Wait.forListeningPort());

    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        dataSource.setURL(database.getJdbcUrl());
        dataSource.setUser(database.getUsername());
        dataSource.setPassword(database.getPassword());
        dataSource.setDatabaseName(database.getDatabaseName());
    }

    @Autowired
    public DatabaseRecipeRepositoryTest(DatabaseRecipeRepository repository) {
        super(() -> repository);
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection con = dataSource.getConnection(); java.sql.Statement stmt = con.createStatement()) {
            stmt.executeUpdate("DELETE FROM ingredients");
            stmt.executeUpdate("DELETE FROM recipe");
            stmt.executeUpdate("ALTER SEQUENCE recipe_id_seq RESTART;");
        }
    }
}
