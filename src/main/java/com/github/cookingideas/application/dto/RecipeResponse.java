package com.github.cookingideas.application.dto;

import java.util.List;

public class RecipeResponse {
    private long id;
    private String name;
    private String description;
    private IngredientList ingredients;

    public RecipeResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IngredientList getIngredients() {
        return ingredients;
    }

    public void setIngredients(IngredientList ingredients) {
        this.ingredients = ingredients;
    }

    public static class IngredientList {
        private List<IngredientResponse> data;

        public List<IngredientResponse> getData() {
            return data;
        }

        public void setData(List<IngredientResponse> data) {
            this.data = data;
        }
    }

}
