package com.github.cookingideas.application.dto;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;

import java.util.List;

public class RecipeDTO {
    private final long id;
    private final String name;
    private final String description;
    private final IngredientList ingredients;

    public static RecipeDTO from(Recipe recipe) {
        return new RecipeDTO(
            recipe.id().value(),
            recipe.name(),
            recipe.description(),
            IngredientList.from(recipe.ingredients())
        );
    }

    private RecipeDTO(long id, String name, String description, IngredientList ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public IngredientList getIngredients() {
        return ingredients;
    }

    public static class IngredientList {
        private final List<IngredientDTO> data;

        public static IngredientList from(List<Ingredient> ingredients) {
            return new IngredientList(ingredients.stream().map((IngredientDTO::from)).toList());
        }

        private IngredientList(List<IngredientDTO> data) {
            this.data = data;
        }

        public List<IngredientDTO> getData() {
            return data;
        }
    }

}
