package com.github.cookingideas.infrastructure.di;

import com.github.cookingideas.domain.repository.RecipeRepository;
import com.github.cookingideas.domain.service.RecipeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipeConfiguration {

    @Bean
    public RecipeService provideRecipeService(RecipeRepository repository) {
        return new RecipeService(repository);
    }
}
