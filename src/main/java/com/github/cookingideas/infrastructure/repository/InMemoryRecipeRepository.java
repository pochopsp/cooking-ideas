package com.github.cookingideas.infrastructure.repository;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryRecipeRepository implements RecipeRepository {

    private final Map<Recipe.Id, Recipe> recipes;
    private final AtomicLong id;

    public InMemoryRecipeRepository(Map<Recipe.Id, Recipe> recipes) {
        this.recipes = recipes;
        id = new AtomicLong();
        id.set(recipes.keySet().stream()
            .map(Recipe.Id::value)
            .max(Long::compare)
            .orElse(0L));
    }

    @Override
    public Recipe.Id getNextId() {
        return new Recipe.Id(id.incrementAndGet());
    }

    @Override
    public Optional<Recipe> get(Recipe.Id id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @Override
    public void store(Recipe recipe) {
        recipes.put(recipe.id(), recipe);
    }
}
