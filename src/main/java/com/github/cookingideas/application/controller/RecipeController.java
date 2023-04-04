package com.github.cookingideas.application.controller;

import com.github.cookingideas.application.dto.RecipeDTO;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipe(@PathVariable long id) {
        return recipeService.get(new Recipe.Id(id))
            .map(RecipeDTO::from)
            .orElse(null);
    }
}
