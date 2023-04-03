package com.github.cookingideas.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void greetingShouldReturnDefaultMessage() {
        String url = baseUrl + "/recipe/1";
        ResponseEntity<Recipe> response = this.restTemplate.getForEntity(url, Recipe.class);

        // TODO: We should first insert and then assert that we get the inserted recipe,
        // not rely on data already on the db
        // Also we should probably test against a json schema and not relying on the domain object
        Recipe expectedRecipe = new Recipe(
            1L,
            "Pizza margherita",
            "Meglio se vai in pizzeria",
            List.of(
                new Ingredient(1L, "Farina"),
                new Ingredient(2L, "Acqua"),
                new Ingredient(3L, "Lievito"),
                new Ingredient(4L, "Sale"),
                new Ingredient(5L, "Pomodoro"),
                new Ingredient(6L, "Mozzarella")
            ));

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(expectedRecipe);
    }
}