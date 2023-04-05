package com.github.cookingideas.domain.repository;

import com.github.cookingideas.domain.exception.IllegalPageRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PageRequestTest {

    @Test
    @DisplayName("Throw an error if the page number is not valid")
    void throwErrorIfIdIsNotValid() {
        assertThatExceptionOfType(IllegalPageRequestException.class)
            .isThrownBy(() -> new PageRequest(0, 1));

        assertThatExceptionOfType(IllegalPageRequestException.class)
            .isThrownBy(() -> new PageRequest(-1, 1));
    }

}