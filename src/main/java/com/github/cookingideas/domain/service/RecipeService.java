package com.github.cookingideas.domain.service;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;

import java.util.Optional;

public class RecipeService {

    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public Optional<Recipe> get(Recipe.Id id) {
        return repository.get(id);
    }
}
