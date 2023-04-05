package com.github.cookingideas.domain.entity;

import com.github.cookingideas.domain.exception.IllegalRecipeIdException;

import java.util.List;

// TODO: Should not be a record
public record Recipe(Recipe.Id id, String name, String description, List<Ingredient> ingredients) {

    public record Id(long value) {
        public Id {
            if (value <= 0) {
                throw new IllegalRecipeIdException();
            }
        }
    }

    public record Ingredient(String name, int quantity, String measureUnit) {
    }
}
