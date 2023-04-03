package com.github.cookingideas.application.dto;

import com.github.cookingideas.domain.entity.Ingredient;

public class IngredientDTO {
    private final long id;
    private final String name;

    public static IngredientDTO from(Ingredient ingredient) {
        return new IngredientDTO(ingredient.id().value(), ingredient.name());
    }

    private IngredientDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}