package com.github.cookingideas.domain.entity;

import com.github.cookingideas.domain.exception.IllegalIngredientIdException;

// TODO: Should not be a record
public record Ingredient(Ingredient.Id id, String name) {

    public record Id(long value) {
        public Id {
            if (value <= 0) {
                throw new IllegalIngredientIdException();
            }
        }
    }

}
