package com.github.cookingideas.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static io.restassured.RestAssured.get;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    public void greetingShouldReturnDefaultMessage() {
        // TODO: We should first insert and then assert that we get the inserted recipe,
        // not rely on data already on the db
        get("/recipe/1").then()
            .statusCode(OK.value())
            .assertThat().body(matchesJsonSchemaInClasspath("responses/recipe-schema.json"));
    }
}