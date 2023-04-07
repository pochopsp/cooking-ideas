package com.github.cookingideas.domain.service;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.exception.RecipeNotFoundException;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

public class RecipeService {

    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public Optional<Recipe> get(Recipe.Id id) {
        return repository.get(id);
    }

    public Page<Recipe> list(PageRequest pageRequest) {
        return repository.list(pageRequest);
    }

    public Recipe store(String name, String description, List<Recipe.Ingredient> ingredients) {
        Recipe.Id id = repository.nextId();
        return store(id, name, description, ingredients);
    }

    public void delete(Recipe.Id id) {
        repository.delete(id);
    }

    public Recipe update(Recipe.Id id, String name, String description, List<Recipe.Ingredient> ingredients) {
        if (get(id).isEmpty()) {
            throw new RecipeNotFoundException(id.value());
        }
        return store(id, name, description, ingredients);
    }

    private Recipe store(Recipe.Id id, String name, String description, List<Recipe.Ingredient> ingredients) {
        Recipe recipe = new Recipe(id, name, description, ingredients);
        repository.store(recipe);
        return recipe;
    }
}
