package com.github.cookingideas.infrastructure.di;

import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.domain.service.RecipeService;
import com.github.cookingideas.infrastructure.repository.InMemoryRecipeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RecipeConfiguration {

    @Bean
    public RecipeService provideRecipeService(RecipeRepository repository) {
        return new RecipeService(repository);
    }

    @Bean
    public InMemoryRecipeRepository provideInMemoryRecipeRepository() {
        return new InMemoryRecipeRepository(new ConcurrentHashMap<>());
    }
}
