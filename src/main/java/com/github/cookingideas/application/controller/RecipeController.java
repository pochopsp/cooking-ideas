package com.github.cookingideas.application.controller;

import com.github.cookingideas.application.dto.RecipeDTO;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.exception.IllegalIdException;
import com.github.cookingideas.domain.usecase.GetRecipeById;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ExceptionHandler(IllegalIdException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException(IllegalIdException e) {
    }
}