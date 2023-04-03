package com.github.cookingideas.domain.usecase;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;

import java.util.Optional;

public class GetRecipeById {

    private final RecipeRepository repository;

    public GetRecipeById(RecipeRepository repository) {
        this.repository = repository;
    }

    public Optional<Recipe> execute(Recipe.Id id) {
        return repository.get(id);
    }
}
