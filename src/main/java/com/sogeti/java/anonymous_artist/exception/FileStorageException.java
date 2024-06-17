package com.sogeti.java.anonymous_artist.exception;

import java.io.IOException;

public class FileStorageException extends RuntimeException{
    public FileStorageException(IOException entity) {
        super( entity);
    }
}


