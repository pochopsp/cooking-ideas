package com.github.cookingideas.application.controller;

import com.github.cookingideas.domain.usecase.GetRecipeById;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recipe")
public class Recipe {

    private final GetRecipeById getRecipeById;

    public Recipe(GetRecipeById getRecipeById) {
        this.getRecipeById = getRecipeById;
    }

    @GetMapping("/{id}")
    public com.github.cookingideas.domain.entity.Recipe getBook(@PathVariable long id) {
        return getRecipeById.execute(id).orElse(null);
    }
}