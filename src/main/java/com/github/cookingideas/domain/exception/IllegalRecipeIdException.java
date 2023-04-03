package com.github.cookingideas.domain.exception;

import com.github.cookingideas.domain.entity.Recipe;

public class IllegalRecipeIdException extends IllegalIdException {
    public IllegalRecipeIdException() {
        super(Recipe.Id.class);
    }
}
