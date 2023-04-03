package com.github.cookingideas.domain.repository;

import com.github.cookingideas.domain.entity.Recipe;

import java.util.Optional;

public interface RecipeRepository {

    Recipe.Id getNextId();

    Optional<Recipe> get(Recipe.Id id);

    void store(Recipe recipe);
}
