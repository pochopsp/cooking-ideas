package com.github.cookingideas.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryRecipeRepositoryTest {

    private final RecipeRepository repository = new InMemoryRecipeRepository(new HashMap<>());

    @Test
    @DisplayName("save and retrieve a Recipe")
    void storeAndRetrieve() {
        long recipeId = 1L;
        Recipe recipe = new Recipe(
            recipeId,
            "a name",
            "a description",
            List.of(
                new Ingredient(1L, "first ingredient"),
                new Ingredient(2L, "second ingredient")
            ));

        repository.store(recipe);

        assertThat(repository.get(recipeId))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("Return empty optional if the recipe is not found")
    void getReturnEmpty() {
        assertThat(repository.get(1L)).isNotPresent();
    }

}