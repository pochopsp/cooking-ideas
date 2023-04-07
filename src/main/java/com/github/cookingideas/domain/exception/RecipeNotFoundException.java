package com.github.cookingideas.domain.exception;

public class RecipeNotFoundException extends ValidationException {
    public RecipeNotFoundException(long id) {
        super("Recipe " + id + " not found");
    }
}
