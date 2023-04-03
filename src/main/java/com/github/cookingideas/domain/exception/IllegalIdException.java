package com.github.cookingideas.domain.exception;

public class IllegalIdException extends ValidationException {
    public IllegalIdException(Class<?> clazz) {
        super("Id for " + clazz.getName() + " is invalid");
    }
}
