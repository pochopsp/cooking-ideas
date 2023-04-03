package com.github.cookingideas.domain.entity;

import java.util.List;

// TODO: Should not be a record
public record Recipe(long id, String name, String description, List<Ingredient> ingredients) {

}
