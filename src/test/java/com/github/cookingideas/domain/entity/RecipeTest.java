package com.github.cookingideas.domain.entity;

import com.github.cookingideas.domain.exception.IllegalIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RecipeTest {

    @Test
    @DisplayName("Throw an error if the id is not valid")
    void throwErrorIfIdIsNotValid() {
        assertThatExceptionOfType(IllegalIdException.class)
            .isThrownBy(() -> new Recipe.Id(-1L));

        assertThatExceptionOfType(IllegalIdException.class)
            .isThrownBy(() -> new Recipe.Id(0L));
    }
}