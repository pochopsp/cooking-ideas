package com.github.cookingideas.application.controller;

import com.github.cookingideas.application.dto.RecipeRequest;
import com.github.cookingideas.application.dto.RecipeResponse;
import com.github.cookingideas.application.mapper.RecipeMapper;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<RecipeResponse> get(@PathVariable long id) {
        return recipeService.get(new Recipe.Id(id))
            .map(recipeMapper::from)
            .map(recipeResponse -> new ResponseEntity<>(recipeResponse, OK))
            .orElseGet(() -> new ResponseEntity<>(NO_CONTENT));
    }

    @PostMapping
    public RecipeResponse save(@RequestBody RecipeRequest recipeRequest) {
        List<Recipe.Ingredient> ingredients = recipeRequest.getIngredients().stream()
            .map(ingredient -> new Recipe.Ingredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getMeasureUnit()))
            .toList();
        Recipe recipe = recipeService.store(recipeRequest.getName(), recipeRequest.getDescription(), ingredients);
        return recipeMapper.from(recipe);
    }

    @PutMapping("/{id}")
    public RecipeResponse update(@PathVariable long id, @RequestBody RecipeRequest recipeRequest) {
        List<Recipe.Ingredient> ingredients = recipeRequest.getIngredients().stream()
            .map(ingredient -> new Recipe.Ingredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getMeasureUnit()))
            .toList();
        Recipe recipe = recipeService.update(new Recipe.Id(id), recipeRequest.getName(), recipeRequest.getDescription(), ingredients);
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        recipeService.delete(new Recipe.Id(id));
    }
}
