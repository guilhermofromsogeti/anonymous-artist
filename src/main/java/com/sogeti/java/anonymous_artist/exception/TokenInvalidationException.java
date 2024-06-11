package com.sogeti.java.anonymous_artist.exception;

public class TokenInvalidationException extends NoDataFoundException {

    public TokenInvalidationException() {
        super("The token is no longer valid. Please try to login again.");
    }
}
