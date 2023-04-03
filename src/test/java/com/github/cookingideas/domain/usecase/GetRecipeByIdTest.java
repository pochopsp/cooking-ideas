package com.github.cookingideas.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.exception.IllegalRecipeIdException;
import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.infrastructure.repository.InMemoryRecipeRepository;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetRecipeByIdTest {

    private final RecipeRepository repository = new InMemoryRecipeRepository(new HashMap<>());
    private final GetRecipeById getRecipeById = new GetRecipeById(repository);

    @Test
    @DisplayName("return the Recipe")
    void returnTheRecipe() {
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

        assertThat(getRecipeById.execute(1L))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("Return empty optional if the recipe is not found")
    void getReturnEmpty() {
        assertThat(getRecipeById.execute(1L)).isNotPresent();
    }

    @Test
    @DisplayName("Throw an error if the id is not valid")
    void throwErrorIfIdIsNotValid() {
        assertThatExceptionOfType(IllegalRecipeIdException.class)
            .isThrownBy(() -> getRecipeById.execute(-1L));

        assertThatExceptionOfType(IllegalRecipeIdException.class)
            .isThrownBy(() -> getRecipeById.execute(-0L));
    }

}