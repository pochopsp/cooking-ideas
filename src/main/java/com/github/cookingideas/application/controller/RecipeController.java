package com.github.cookingideas.application.controller;

import com.github.cookingideas.application.dto.RecipeRequest;
import com.github.cookingideas.application.dto.RecipeResponse;
import com.github.cookingideas.application.mapper.RecipeMapper;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.service.RecipeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
        this.recipeMapper = new RecipeMapper();
    }

    @GetMapping("/{id}")
    public RecipeResponse get(@PathVariable long id) {
        return recipeService.get(new Recipe.Id(id))
            .map(recipeMapper::from)
            .orElse(null);
    }

    @PostMapping
    public RecipeResponse save(@RequestBody RecipeRequest recipeRequest) {
        List<Recipe.Ingredient> ingredients = recipeRequest.getIngredients().stream()
            .map(ingredient -> new Recipe.Ingredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getMeasureUnit()))
            .toList();
        Recipe recipe = recipeService.store(recipeRequest.getName(), recipeRequest.getDescription(), ingredients);
        return recipeMapper.from(recipe);
    }

    // TODO: Not sure if we should have a DTO also for the Page
    @GetMapping
    public Page<RecipeResponse> getPage(@RequestParam int page, @RequestParam int size) {
        Page<Recipe> recipes = recipeService.list(new PageRequest(page, size));
        List<RecipeResponse> recipesResponse = recipes.elements().stream()
            .map(recipeMapper::from)
            .toList();
        return new Page<>(recipesResponse, recipes.pages());
    }
}
