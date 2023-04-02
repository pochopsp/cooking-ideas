package com.github.cookingideas.domain.repository;

import com.github.cookingideas.domain.entity.Recipe;
import java.util.Optional;

public interface RecipeRepository {

    Optional<Recipe> get(long id);

    void store(Recipe recipe);
}
