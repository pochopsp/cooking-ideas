package com.github.cookingideas.domain.exception;

public class IllegalPageRequestException extends ValidationException {
    public IllegalPageRequestException(int page, int size) {
        super("PageRequest with page number " + page + " and size " + size + " is invalid. Both must be positives");
    }
}