package com.github.cookingideas.infrastructure.repository;

import com.github.cookingideas.RecordFactory;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.repository.RecipeRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryRecipeRepositoryTest {

    private final RecipeRepository repository;
    private final EasyRandom easyRandom;

    public InMemoryRecipeRepositoryTest() {
        repository = new InMemoryRecipeRepository(new HashMap<>());
        EasyRandomParameters parameters = new EasyRandomParameters()
            .objectFactory(new RecordFactory())
            .randomize(Recipe.Id.class, repository::nextId);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("If the storage is empty, first Id is 1")
    void idForEmptyStorage() {
        Recipe.Id expected = new Recipe.Id(1L);
        assertThat(repository.nextId()).isEqualTo(expected);
    }

    @Test
    @DisplayName("New id is the next long")
    void nextId() {
        Recipe.Id recipeId = repository.nextId();
        Recipe recipe = new Recipe(
            recipeId,
            "a name",
            "a description",
            easyRandom.objects(Recipe.Ingredient.class, 5).toList()
        );
        repository.store(recipe);

        Recipe.Id expected = new Recipe.Id(2L);
        assertThat(repository.nextId()).isEqualTo(expected);
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

    @Test
    @DisplayName("save and retrieve a Recipe")
    void storeAndRetrieve() {
        Recipe recipe = easyRandom.nextObject(Recipe.class);
        repository.store(recipe);

        assertThat(repository.get(recipe.id()))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("modify a Recipe")
    void modifyARecipe() {
        Recipe recipe = easyRandom.nextObject(Recipe.class);
        repository.store(recipe);
        Recipe modifiedRecipe = new Recipe(recipe.id(), "new name", recipe.description(), recipe.ingredients());

        repository.store(modifiedRecipe);

        assertThat(repository.get(recipe.id()))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(modifiedRecipe);
    }

    @Test
    @DisplayName("Return empty optional if the recipe is not found")
    void getReturnEmpty() {
        assertThat(repository.get(new Recipe.Id(1L))).isNotPresent();
    }

    @Test
    @DisplayName("return recipes page")
    void page() {
        List<Recipe> recipes = easyRandom.objects(Recipe.class, 19).toList();
        recipes.forEach(repository::store);

        List<Recipe> expectedResults1 = recipes.subList(0, 5);
        List<Recipe> expectedResults2 = recipes.subList(5, 10);
        List<Recipe> expectedResults3 = recipes.subList(10, 15);
        List<Recipe> expectedResults4 = recipes.subList(15, 19);

        Page<Recipe> page1 = repository.list(new PageRequest(1, 5));
        assertThat(page1.totalElements()).isEqualTo(recipes.size());
        assertThat(page1.elements()).usingRecursiveComparison().isEqualTo(expectedResults1);

        Page<Recipe> page2 = repository.list(new PageRequest(2, 5));
        assertThat(page2.totalElements()).isEqualTo(recipes.size());
        assertThat(page2.elements()).usingRecursiveComparison().isEqualTo(expectedResults2);

        Page<Recipe> page3 = repository.list(new PageRequest(3, 5));
        assertThat(page3.totalElements()).isEqualTo(recipes.size());
        assertThat(page3.elements()).usingRecursiveComparison().isEqualTo(expectedResults3);

        Page<Recipe> page4 = repository.list(new PageRequest(4, 5));
        assertThat(page4.totalElements()).isEqualTo(recipes.size());
        assertThat(page4.elements()).usingRecursiveComparison().isEqualTo(expectedResults4);
    }

    @Test
    @DisplayName("return empty page if elements does not exists")
    void emptyPage() {
        List<Recipe> recipes = easyRandom.objects(Recipe.class, 19).toList();
        recipes.forEach(repository::store);

        Page<Recipe> page = repository.list(new PageRequest(5, 5));
        assertThat(page.totalElements()).isEqualTo(recipes.size());
        assertThat(page.elements()).isEmpty();
    }

    @Test
    @DisplayName("return empty page if no elements are stored")
    void emptyPageNoElements() {
        Page<Recipe> page = repository.list(new PageRequest(1, 5));
        assertThat(page.totalElements()).isEqualTo(0);
        assertThat(page.elements()).isEmpty();
    }

}
