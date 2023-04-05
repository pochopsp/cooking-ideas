package com.github.cookingideas.infrastructure.repository;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.RecipeRepository;

import java.util.Comparator;
import java.util.List;
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
    public Recipe.Id nextId() {
        return new Recipe.Id(id.incrementAndGet());
    }

    @Override
    public Optional<Recipe> get(Recipe.Id id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @Override
    public Page<Recipe> list(int offset, int size) {
        List<Recipe> result = recipes.entrySet().stream()
            .sorted(Comparator.comparingLong(entry -> entry.getKey().value()))
            .skip(offset)
            .limit(size)
            .map(Map.Entry::getValue)
            .toList();

        return new Page<>(result, recipes.size());
    }

    @Override
    public void store(Recipe recipe) {
        recipes.put(recipe.id(), recipe);
    }
}
