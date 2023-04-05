package com.github.cookingideas.acceptance;

import com.github.cookingideas.application.dto.RecipeRequest;
import com.github.cookingideas.application.dto.RecipeResponse;
import com.github.cookingideas.domain.repository.Page;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class RecipeControllerTest {

    @Container
    private static final PostgreSQLContainer<?> database =
        new PostgreSQLContainer<>("postgres:15.2-alpine").waitingFor(Wait.forListeningPort());
    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();
    private static final TypeRef<Page<RecipeResponse>> pageResponseTypeRef = new PageResponseTypeRef();

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

    @Value(value = "${local.server.port}")
    private int port;
    private final EasyRandom easyRandom;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        try (Connection con = dataSource.getConnection(); java.sql.Statement stmt = con.createStatement()) {
            stmt.executeUpdate("DELETE FROM ingredients");
            stmt.executeUpdate("DELETE FROM recipe");
            stmt.executeUpdate("ALTER SEQUENCE recipe_id_seq RESTART;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeControllerTest() {
        EasyRandomParameters parameters = new EasyRandomParameters()
            .stringLengthRange(1, 25)
            .collectionSizeRange(1, 10);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("Return no content if the entity does not exists")
    public void noContent() {
        when().get("/recipe/1").then()
            .assertThat()
            .statusCode(NO_CONTENT.value());
    }

    @Test
    @DisplayName("Store and return a recipe")
    public void storeAndReturn() {

        RecipeRequest recipeRequest = easyRandom.nextObject(RecipeRequest.class);
        RecipeResponse savedRecipe = saveRecipe(recipeRequest);

        RecipeResponse retrievedRecipe = when().get("/recipe/" + savedRecipe.getId()).then()
            .assertThat()
            .statusCode(OK.value())
            .body(matchesJsonSchemaInClasspath("responses/recipe-schema.json"))
            .extract()
            .as(RecipeResponse.class);

        assertThat(retrievedRecipe).usingRecursiveComparison().isEqualTo(savedRecipe);
    }

    @Test
    @DisplayName("Modify a recipe")
    public void modifyARecipe() {

        RecipeRequest recipeRequest = easyRandom.nextObject(RecipeRequest.class);
        saveRecipe(recipeRequest);

        recipeRequest.setName(recipeRequest.getName() + "new");
        recipeRequest.setDescription(recipeRequest.getDescription() + "new");
        recipeRequest.setIngredients(recipeRequest.getIngredients().subList(0, recipeRequest.getIngredients().size() - 1));
        RecipeResponse savedRecipe = saveRecipe(recipeRequest);

        RecipeResponse retrievedRecipe = when().get("/recipe/" + savedRecipe.getId()).then()
            .assertThat()
            .statusCode(OK.value())
            .body(matchesJsonSchemaInClasspath("responses/recipe-schema.json"))
            .extract()
            .as(RecipeResponse.class);

        assertThat(retrievedRecipe).usingRecursiveComparison().isEqualTo(savedRecipe);
    }

    @Test
    @DisplayName("Delete a recipe")
    public void deleteARecipe() {

        RecipeRequest recipeRequest = easyRandom.nextObject(RecipeRequest.class);
        RecipeResponse savedRecipe = saveRecipe(recipeRequest);

        when().delete("/recipe/" + savedRecipe.getId()).then()
            .assertThat()
            .statusCode(OK.value());

        when().get("/recipe/" + savedRecipe.getId()).then()
            .assertThat()
            .statusCode(NO_CONTENT.value());
    }

    @Test
    @DisplayName("Return bad request if the id is not valid")
    public void badRequest() {
        get("/recipe/0").then()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Return a page of recipes")
    public void page() {
        List<RecipeResponse> recipes = easyRandom.objects(RecipeRequest.class, 6)
            .map(RecipeControllerTest::saveRecipe)
            .toList();

        List<RecipeResponse> expectedResults1 = recipes.subList(0, 5);
        List<RecipeResponse> expectedResults2 = recipes.subList(5, 6);

        Page<RecipeResponse> page1 = given()
            .param("page", 1)
            .param("size", 5)
            .get("/recipe").then()
            .statusCode(OK.value())
            .assertThat()
            .body(matchesJsonSchemaInClasspath("responses/recipePage-schema.json"))
            .extract()
            .as(pageResponseTypeRef);
        assertThat(page1.pages()).isEqualTo(2);
        assertThat(page1.elements()).usingRecursiveComparison().isEqualTo(expectedResults1);

        Page<RecipeResponse> page2 = given()
            .param("page", 2)
            .param("size", 5)
            .get("/recipe").then()
            .statusCode(OK.value())
            .assertThat()
            .body(matchesJsonSchemaInClasspath("responses/recipePage-schema.json"))
            .extract()
            .as(pageResponseTypeRef);
        assertThat(page2.pages()).isEqualTo(2);
        assertThat(page2.elements()).usingRecursiveComparison().isEqualTo(expectedResults2);

    }

    private static RecipeResponse saveRecipe(RecipeRequest recipeRequest) {
        return given()
            .accept(APPLICATION_JSON_VALUE)
            .and()
            .contentType(APPLICATION_JSON_VALUE)
            .and()
            .body(recipeRequest)
            .when()
            .post("/recipe")
            .as(RecipeResponse.class);
    }

    private static class PageResponseTypeRef extends TypeRef<Page<RecipeResponse>> {
    }
}