package com.sogeti.java.anonymous_artist.exception;

import java.util.UUID;

public class ProductAlreadyExistException extends RuntimeException {
    public ProductAlreadyExistException(UUID id) {
        super(String.format("product with id '%s' already exist in your system", id));
    }
}
