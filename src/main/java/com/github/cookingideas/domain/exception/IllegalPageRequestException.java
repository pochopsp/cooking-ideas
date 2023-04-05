package com.github.cookingideas.domain.exception;

import com.github.cookingideas.domain.repository.PageRequest;

public class IllegalPageRequestException extends ValidationException {
    public IllegalPageRequestException(PageRequest request) {
        super("PageRequest " + request + " is invalid. Page number and size must be both positive");
    }
}