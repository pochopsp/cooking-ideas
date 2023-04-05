package com.github.cookingideas.infrastructure.data.repository;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRecipeRepositoryTest extends RecipeRepositoryTest {

    public InMemoryRecipeRepositoryTest() {
        super(() -> new InMemoryRecipeRepository(new ConcurrentHashMap<>()));
    }

    @Test
    @DisplayName("If the storage is not empty, first Id is next long")
    void idForNotEmptyStorage() {
        Recipe.Id recipeId = new Recipe.Id(1L);
        Recipe recipe = new Recipe(
            recipeId,
            "a name",
            "a description",
            easyRandom.objects(Recipe.Ingredient.class, 5).toList()
        );
        Map<Recipe.Id, Recipe> storage = Map.of(recipeId, recipe);
        RecipeRepository repository = new InMemoryRecipeRepository(storage);

        Recipe.Id expected = new Recipe.Id(2L);
        assertThat(repository.nextId()).isEqualTo(expected);
    }

}
