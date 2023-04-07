package com.github.cookingideas.infrastructure.data.repository;

import com.github.cookingideas.RecordFactory;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.repository.RecipeRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

abstract class RecipeRepositoryTest {

    private final RecipeRepository repository;
    protected final EasyRandom easyRandom;

    public RecipeRepositoryTest(Supplier<RecipeRepository> recipeRepositorySupplier) {
        repository = recipeRepositorySupplier.get();
        EasyRandomParameters parameters = new EasyRandomParameters()
            .objectFactory(new RecordFactory())
            .stringLengthRange(1, 25)
            .randomize(Recipe.Id.class, repository::nextId);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("First Id is 1")
    void firstId() {
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
    @DisplayName("save and retrieve a Recipe")
    void storeAndRetrieve() {
        List<Recipe.Ingredient> ingredients = easyRandom.objects(Recipe.Ingredient.class, 5).toList();
        Recipe recipe = new Recipe(
            easyRandom.nextObject(Recipe.Id.class),
            easyRandom.nextObject(String.class),
            easyRandom.nextObject(String.class),
            ingredients
        );
        repository.store(recipe);

        assertThat(repository.get(recipe.id()))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(recipe);
    }

    @Test
    @DisplayName("modify a Recipe")
    void modifyARecipe() {
        List<Recipe.Ingredient> ingredients = easyRandom.objects(Recipe.Ingredient.class, 5).toList();
        Recipe recipe = new Recipe(
            easyRandom.nextObject(Recipe.Id.class),
            easyRandom.nextObject(String.class),
            easyRandom.nextObject(String.class),
            ingredients
        );
        repository.store(recipe);
        Recipe modifiedRecipe = new Recipe(
            recipe.id(),
            recipe.name() + "new",
            recipe.description() + "new",
            recipe.ingredients().subList(0, 2)
        );

        repository.store(modifiedRecipe);

        assertThat(repository.get(recipe.id()))
            .isPresent()
            .get().usingRecursiveComparison().isEqualTo(modifiedRecipe);
    }

    @Test
    @DisplayName("delete a Recipe")
    void delete() {
        List<Recipe.Ingredient> ingredients = easyRandom.objects(Recipe.Ingredient.class, 5).toList();
        Recipe recipe = new Recipe(
            easyRandom.nextObject(Recipe.Id.class),
            easyRandom.nextObject(String.class),
            easyRandom.nextObject(String.class),
            ingredients
        );
        repository.store(recipe);
        assertThat(repository.get(recipe.id())).isPresent();

        repository.delete(recipe.id());
        assertThat(repository.get(recipe.id())).isNotPresent();
    }

    @Test
    @DisplayName("do not throw errors if try to delete a not existent Recipe")
    void doNotThrowOnDelete() {
        assertThatNoException().isThrownBy(() -> repository.delete(new Recipe.Id(1L)));
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
        assertThat(page1.pages()).isEqualTo(4);
        assertThat(page1.elements()).usingRecursiveComparison().isEqualTo(expectedResults1);

        Page<Recipe> page2 = repository.list(new PageRequest(2, 5));
        assertThat(page2.pages()).isEqualTo(4);
        assertThat(page2.elements()).usingRecursiveComparison().isEqualTo(expectedResults2);

        Page<Recipe> page3 = repository.list(new PageRequest(3, 5));
        assertThat(page3.pages()).isEqualTo(4);
        assertThat(page3.elements()).usingRecursiveComparison().isEqualTo(expectedResults3);

        Page<Recipe> page4 = repository.list(new PageRequest(4, 5));
        assertThat(page4.pages()).isEqualTo(4);
        assertThat(page4.elements()).usingRecursiveComparison().isEqualTo(expectedResults4);
    }

    @Test
    @DisplayName("return empty page if elements does not exists")
    void emptyPage() {
        List<Recipe> recipes = easyRandom.objects(Recipe.class, 19).toList();
        recipes.forEach(repository::store);

        Page<Recipe> page = repository.list(new PageRequest(5, 5));
        assertThat(page.pages()).isEqualTo(4);
        assertThat(page.elements()).isEmpty();
    }

    @Test
    @DisplayName("return empty page if no elements are stored")
    void emptyPageNoElements() {
        Page<Recipe> page = repository.list(new PageRequest(1, 5));
        assertThat(page.pages()).isEqualTo(0);
        assertThat(page.elements()).isEmpty();
    }

}
