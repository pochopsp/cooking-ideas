package com.github.cookingideas.infrastructure.repository;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import java.util.Map;
import java.util.Optional;

public class InMemoryRecipeRepository implements RecipeRepository {

    private final Map<Long, Recipe> recipes;

    public InMemoryRecipeRepository(Map<Long, Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public Optional<Recipe> get(long id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @Override
    public void store(Recipe recipe) {
        recipes.put(recipe.id(), recipe);
    }
}
