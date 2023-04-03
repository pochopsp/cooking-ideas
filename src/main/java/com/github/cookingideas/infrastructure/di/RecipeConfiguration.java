package com.github.cookingideas.infrastructure.di;

import com.github.cookingideas.domain.entity.Ingredient;
import com.github.cookingideas.domain.entity.Recipe;
import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.domain.usecase.GetRecipeById;
import com.github.cookingideas.infrastructure.repository.InMemoryRecipeRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipeConfiguration {

    @Bean
    public GetRecipeById provideGetRecipeById(RecipeRepository repository) {
        return new GetRecipeById(repository);
    }

    @Bean
    public InMemoryRecipeRepository provideInMemoryRecipeRepository(Map<Long, Recipe> storage) {
        return new InMemoryRecipeRepository(storage);
    }

    @Bean
    public Map<Long, Recipe> provideInMemoryRecipeRepositoryStorageWithSampleData() {
        Map<Long, Recipe> storage = new ConcurrentHashMap<>();
        storage.put(1L, new Recipe(
            1L,
            "Pizza margherita",
            "Meglio se vai in pizzeria",
            List.of(
                new Ingredient(1L, "Farina"),
                new Ingredient(2L, "Acqua"),
                new Ingredient(3L, "Lievito"),
                new Ingredient(4L, "Sale"),
                new Ingredient(5L, "Pomodoro"),
                new Ingredient(6L, "Mozzarella")
            )));

        storage.put(2L, new Recipe(
            2L,
            "Caprese",
            "Taglia la mozzarella ed il pomodoro e mettili in un piatto",
            List.of(
                new Ingredient(5L, "Pomodoro"),
                new Ingredient(6L, "Mozzarella")
            )));

        return storage;
    }
}
