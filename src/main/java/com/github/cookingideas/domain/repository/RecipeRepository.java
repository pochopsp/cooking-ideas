package com.github.cookingideas.domain.repository;

import com.github.cookingideas.domain.entity.Recipe;

import java.util.Optional;

public interface RecipeRepository {

    Recipe.Id nextId();

    Optional<Recipe> get(Recipe.Id id);

    Page<Recipe> list(int offset, int size);

    void store(Recipe recipe);
}
