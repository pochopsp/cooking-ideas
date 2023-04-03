package com.github.cookingideas.acceptance;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static io.restassured.RestAssured.get;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Return a recipe")
    public void greetingShouldReturnDefaultMessage() {
        // TODO: We should first insert and then assert that we get the inserted recipe,
        // not rely on data already on the db
        get("/recipe/1").then()
            .statusCode(HttpStatus.SC_OK)
            .assertThat().body(matchesJsonSchemaInClasspath("responses/recipe-schema.json"));
    }

    @Test
    @DisplayName("Return bad request if the id is not valid")
    public void badRequest() {
        get("/recipe/0").then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}