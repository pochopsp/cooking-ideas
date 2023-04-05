package com.github.cookingideas.infrastructure.data.repository;

import com.github.cookingideas.infrastructure.data.entity.DbRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SpringRecipeRepository extends JpaRepository<DbRecipe, Long> {
    @Query(value = "SELECT nextval('recipe_id_seq')", nativeQuery = true)
    long getNextSeriesId();

    Optional<DbRecipe> findDbRecipeById(long id);
}