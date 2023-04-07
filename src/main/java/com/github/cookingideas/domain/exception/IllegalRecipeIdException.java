package com.github.cookingideas.domain.exception;

import com.github.cookingideas.domain.entity.Recipe;

public class IllegalRecipeIdException extends ValidationException {
    public IllegalRecipeIdException(long id) {
        super("Recipe id " + id + " is invalid");
    }
}
