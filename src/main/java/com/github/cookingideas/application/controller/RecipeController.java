package com.github.cookingideas.application.controller;

import com.github.cookingideas.application.dto.RecipeDTO;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.usecase.GetRecipeById;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recipe")
public class RecipeController {

    private final GetRecipeById getRecipeById;

    public RecipeController(GetRecipeById getRecipeById) {
        this.getRecipeById = getRecipeById;
    }

    @GetMapping("/{id}")
    public RecipeDTO getBook(@PathVariable long id) {
        return getRecipeById.execute(new Recipe.Id(id))
            .map(RecipeDTO::from)
            .orElse(null);
    }
}