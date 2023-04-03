package com.github.cookingideas.domain.exception;

import com.github.cookingideas.domain.entity.Ingredient;

public class IllegalIngredientIdException extends IllegalIdException {
    public IllegalIngredientIdException() {
        super(Ingredient.Id.class);
    }
}
