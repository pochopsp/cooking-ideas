package com.github.cookingideas.infrastructure.repository;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryRecipeRepositoryTest {

    private final RecipeRepository repository = new InMemoryRecipeRepository(new HashMap<>());

    @Test
    @DisplayName("If the storage is empty, fist Id is 1")
    void idForEmptyStorage() {
        Recipe.Id expected = new Recipe.Id(1L);
        assertThat(repository.getNextId()).isEqualTo(expected);
    }

    @Test
    @DisplayName("New id is the next long")
    void nextId() {
        Recipe.Id recipeId = repository.getNextId();
        Recipe recipe = new Recipe(
            recipeId,
            "a name",
            "a description",
            List.of(
                new Ingredient(new Ingredient.Id(1L), "first ingredient"),
                new Ingredient(new Ingredient.Id(2L), "second ingredient")
            ));
        repository.store(recipe);

        Recipe.Id expected = new Recipe.Id(2L);
        assertThat(repository.getNextId()).isEqualTo(expected);
    }

    @Test
    @DisplayName("If the storage is not empty, fist Id is next long")
    void idForNotEmptyStorage() {
        Recipe.Id recipeId = new Recipe.Id(1L);
        Recipe recipe = new Recipe(
            recipeId,
            "a name",
            "a description",
            List.of(
                new Ingredient(new Ingredient.Id(1L), "first ingredient"),
                new Ingredient(new Ingredient.Id(2L), "second ingredient")
            ));
        Map<Recipe.Id, Recipe> storage = Map.of(recipeId, recipe);
        RecipeRepository repository = new InMemoryRecipeRepository(storage);

        Recipe.Id expected = new Recipe.Id(2L);
        assertThat(repository.getNextId()).isEqualTo(expected);
    }

    @Test
    @DisplayName("save and retrieve a Recipe")
    void storeAndRetrieve() {
        Recipe.Id recipeId = repository.getNextId();
        Recipe recipe = new Recipe(
            recipeId,
            "a name",
            "a description",
            List.of(
                new Ingredient(new Ingredient.Id(1L), "first ingredient"),
                new Ingredient(new Ingredient.Id(2L), "second ingredient")
            ));

        repository.store(recipe);

        assertThat(repository.get(recipeId))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("Return empty optional if the recipe is not found")
    void getReturnEmpty() {
        assertThat(repository.get(new Recipe.Id(1L))).isNotPresent();
    }

}