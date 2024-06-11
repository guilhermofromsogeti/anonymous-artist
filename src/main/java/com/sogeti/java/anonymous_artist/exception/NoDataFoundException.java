package com.sogeti.java.anonymous_artist.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String entity) {
        super("Cannot find data: " + entity);
    }
}
