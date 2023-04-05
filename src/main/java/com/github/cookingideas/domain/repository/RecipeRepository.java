package com.github.cookingideas.domain.repository;

import com.github.cookingideas.domain.entity.Recipe;

import java.util.Optional;

public interface RecipeRepository {

    Recipe.Id nextId();

    Optional<Recipe> get(Recipe.Id id);

    Page<Recipe> list(PageRequest pageRequest);

    void store(Recipe recipe);

    void delete(Recipe.Id id);
}
