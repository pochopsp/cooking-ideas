package com.github.cookingideas.application.mapper;

import com.github.cookingideas.application.dto.IngredientResponse;
import com.github.cookingideas.application.dto.RecipeResponse;
import com.github.cookingideas.domain.entity.Recipe;

import java.util.List;

public class RecipeMapper {

    public RecipeResponse from(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.id().value());
        response.setName(recipe.name());
        response.setDescription(recipe.description());
        response.setIngredients(this.from(recipe.ingredients()));
        return response;
    }

    private RecipeResponse.IngredientList from(List<Recipe.Ingredient> ingredients) {
        RecipeResponse.IngredientList ingredientList = new RecipeResponse.IngredientList();
        ingredientList.setData(ingredients.stream().map(this::from).toList());
        return ingredientList;
    }

    private IngredientResponse from(Recipe.Ingredient ingredient) {
        IngredientResponse response = new IngredientResponse();
        response.setName(ingredient.name());
        response.setQuantity(ingredient.quantity());
        response.setMeasureUnit(ingredient.measureUnit());
        return response;
    }
}
