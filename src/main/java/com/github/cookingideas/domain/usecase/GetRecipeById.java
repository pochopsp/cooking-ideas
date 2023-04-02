package com.github.cookingideas.domain.usecase;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.exception.IllegalRecipeIdException;
import com.github.cookingideas.domain.repository.RecipeRepository;
import java.util.Optional;

public class GetRecipeById {

    private final RecipeRepository repository;

    public GetRecipeById(RecipeRepository repository) {
        this.repository = repository;
    }

    public Optional<Recipe> execute(long id) {
        if (id <= 0) {
            throw new IllegalRecipeIdException();
        }
        return repository.get(id);
    }
}
