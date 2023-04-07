package com.github.cookingideas.infrastructure.data.repository;

import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.Page;
import com.github.cookingideas.domain.repository.PageRequest;
import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.infrastructure.data.entity.DbIngredient;
import com.github.cookingideas.infrastructure.data.entity.DbRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
interface DatabaseRecipeRepository extends RecipeRepository, JpaRepository<DbRecipe, Long> {

    @Query(value = "SELECT nextval('recipe_id_seq')", nativeQuery = true)
    long getNextSeriesId();

    Optional<DbRecipe> findDbRecipeById(long id);
    
    default Recipe.Id nextId() {
        return new Recipe.Id(getNextSeriesId());
    }

    default Optional<Recipe> get(Recipe.Id id) {
        return findDbRecipeById(id.value()).map(this::toRecipe);
    }

    default Page<Recipe> list(PageRequest pageRequest) {
        org.springframework.data.domain.Page<DbRecipe> page =
            findAll(org.springframework.data.domain.PageRequest.of(pageRequest.page() - 1, pageRequest.size()));

        List<Recipe> recipes = page.stream().map(this::toRecipe).toList();
        int elements = page.getTotalPages();
        return new Page<>(recipes, elements);
    }

    @Transactional
    default void store(Recipe recipe) {
        DbRecipe dbRecipe = new DbRecipe();
        dbRecipe.setId(recipe.id().value());
        dbRecipe.setName(recipe.name());
        dbRecipe.setDescription(recipe.description());
        dbRecipe.setIngredients(recipe.ingredients().stream().map(ingredient -> {
            DbIngredient dbIngredient = new DbIngredient();
            dbIngredient.setName(ingredient.name());
            dbIngredient.setQuantity(ingredient.quantity());
            dbIngredient.setMeasureUnit(ingredient.measureUnit());
            dbIngredient.setDbRecipe(dbRecipe);
            return dbIngredient;
        }).toList());

        save(dbRecipe);
    }

    @Transactional
    default void delete(Recipe.Id id) {
        deleteById(id.value());
    }

    private Recipe toRecipe(DbRecipe dbRecipe) {
        return new Recipe(
            new Recipe.Id(dbRecipe.getId()),
            dbRecipe.getName(),
            dbRecipe.getDescription(),
            dbRecipe.getIngredients().stream()
                .map(dbIngredient -> new Recipe.Ingredient(
                    dbIngredient.getName(),
                    dbIngredient.getQuantity(),
                    dbIngredient.getMeasureUnit()))
                .toList()
        );
    }
}
