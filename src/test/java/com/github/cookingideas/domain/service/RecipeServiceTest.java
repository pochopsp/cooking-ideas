package com.github.cookingideas.domain.service;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.infrastructure.repository.InMemoryRecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeServiceTest {

    private final RecipeRepository repository = new InMemoryRecipeRepository(new HashMap<>());
    private final RecipeService recipeService = new RecipeService(repository);

    @Test
    @DisplayName("return the Recipe")
    void returnTheRecipe() {
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

        assertThat(recipeService.get(recipeId))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("Return empty optional if the recipe is not found")
    void getReturnEmpty() {
        assertThat(recipeService.get(new Recipe.Id(1L))).isNotPresent();
    }

}