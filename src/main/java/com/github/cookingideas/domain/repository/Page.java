package com.github.cookingideas.domain.repository;

import java.util.List;

public record Page<T>(List<T> elements, int totalElements) {
}