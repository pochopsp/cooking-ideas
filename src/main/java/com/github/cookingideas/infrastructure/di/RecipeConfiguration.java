package com.github.cookingideas.infrastructure.di;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.domain.service.RecipeService;
import com.github.cookingideas.infrastructure.repository.InMemoryRecipeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RecipeConfiguration {

    @Bean
    public RecipeService provideRecipeService(RecipeRepository repository) {
        return new RecipeService(repository);
    }

    @Bean
    public InMemoryRecipeRepository provideInMemoryRecipeRepository(Map<Recipe.Id, Recipe> storage) {
        return new InMemoryRecipeRepository(storage);
    }

    @Bean
    public Map<Recipe.Id, Recipe> provideInMemoryRecipeRepositoryStorageWithSampleData() {
        Map<Recipe.Id, Recipe> storage = new ConcurrentHashMap<>();
        storage.put(new Recipe.Id(1L), new Recipe(
            new Recipe.Id(1L),
            "Pizza margherita",
            "Meglio se vai in pizzeria",
            List.of(
                new Ingredient(new Ingredient.Id(1L), "Farina"),
                new Ingredient(new Ingredient.Id(2L), "Acqua"),
                new Ingredient(new Ingredient.Id(3L), "Lievito"),
                new Ingredient(new Ingredient.Id(4L), "Sale"),
                new Ingredient(new Ingredient.Id(5L), "Pomodoro"),
                new Ingredient(new Ingredient.Id(6L), "Mozzarella")
            )));

        storage.put(new Recipe.Id(2L), new Recipe(
            new Recipe.Id(2L),
            "Caprese",
            "Taglia la mozzarella ed il pomodoro e mettili in un piatto",
            List.of(
                new Ingredient(new Ingredient.Id(5L), "Pomodoro"),
                new Ingredient(new Ingredient.Id(6L), "Mozzarella")
            )));

        return storage;
    }
}
