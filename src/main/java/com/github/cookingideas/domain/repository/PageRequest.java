package com.github.cookingideas.domain.repository;

import com.github.cookingideas.domain.exception.IllegalPageRequestException;

public record PageRequest(int page, int size) {
    public PageRequest {
        if (page <= 0 || size <= 0) {
            throw new IllegalPageRequestException(page, size);
        }
    }
}
