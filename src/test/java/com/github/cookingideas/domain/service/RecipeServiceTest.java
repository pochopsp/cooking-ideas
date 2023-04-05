package com.github.cookingideas.domain.service;

import com.github.cookingideas.RecordFactory;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.infrastructure.data.repository.InMemoryRecipeRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeServiceTest {

    private final RecipeService recipeService;
    private final EasyRandom easyRandom;

    public RecipeServiceTest() {
        RecipeRepository repository = new InMemoryRecipeRepository(new HashMap<>());
        recipeService = new RecipeService(repository);
        EasyRandomParameters parameters = new EasyRandomParameters().objectFactory(new RecordFactory());
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("save and return the Recipe")
    void returnTheRecipe() {
        Recipe recipe = storeRandomRecipe();

        assertThat(recipeService.get(recipe.id()))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("Return empty optional if the recipe is not found")
    void getReturnEmpty() {
        assertThat(recipeService.get(new Recipe.Id(1L))).isNotPresent();
    }

    @Test
    @DisplayName("return recipes page")
    void page() {
        List<Recipe> recipes = IntStream.range(0, 19)
            .mapToObj(operand -> storeRandomRecipe())
            .toList();

        List<Recipe> expectedResults1 = recipes.subList(0, 5);
        List<Recipe> expectedResults2 = recipes.subList(5, 10);
        List<Recipe> expectedResults3 = recipes.subList(10, 15);
        List<Recipe> expectedResults4 = recipes.subList(15, 19);

        Page<Recipe> page1 = recipeService.list(new PageRequest(1, 5));
        assertThat(page1.pages()).isEqualTo(4);
        assertThat(page1.elements()).usingRecursiveComparison().isEqualTo(expectedResults1);

        Page<Recipe> page2 = recipeService.list(new PageRequest(2, 5));
        assertThat(page2.pages()).isEqualTo(4);
        assertThat(page2.elements()).usingRecursiveComparison().isEqualTo(expectedResults2);

        Page<Recipe> page3 = recipeService.list(new PageRequest(3, 5));
        assertThat(page3.pages()).isEqualTo(4);
        assertThat(page3.elements()).usingRecursiveComparison().isEqualTo(expectedResults3);

        Page<Recipe> page4 = recipeService.list(new PageRequest(4, 5));
        assertThat(page4.pages()).isEqualTo(4);
        assertThat(page4.elements()).usingRecursiveComparison().isEqualTo(expectedResults4);
    }

    @Test
    @DisplayName("return empty page if elements does not exists")
    void emptyPage() {
        List<Recipe> recipes = IntStream.range(0, 20)
            .mapToObj(operand -> storeRandomRecipe())
            .toList();

        Page<Recipe> page = recipeService.list(new PageRequest(5, 5));
        assertThat(page.pages()).isEqualTo(4);
        assertThat(page.elements()).isEmpty();
    }

    @Test
    @DisplayName("return empty page if no elements are stored")
    void emptyPageNoElements() {
        Page<Recipe> page = recipeService.list(new PageRequest(1, 5));
        assertThat(page.pages()).isEqualTo(0);
        assertThat(page.elements()).isEmpty();
    }

    @Test
    @DisplayName("delete a Recipe")
    void deleteARecipe() {
        Recipe recipe = storeRandomRecipe();
        assertThat(recipeService.get(recipe.id())).isPresent();
        recipeService.delete(recipe.id());

        assertThat(recipeService.get(recipe.id())).isNotPresent();
    }

    private Recipe storeRandomRecipe() {
        String name = easyRandom.nextObject(String.class);
        String description = easyRandom.nextObject(String.class);
        List<Recipe.Ingredient> ingredients = easyRandom.objects(Recipe.Ingredient.class, 5).toList();

        return recipeService.store(name, description, ingredients);
    }
}