package com.tidal.stream.encryptor;

public class DecryptorException extends RuntimeException {
    public DecryptorException(String message) {
        super(message);
    }

    public DecryptorException(Throwable cause) {
        super(cause);
    }

    public DecryptorException(String message, Throwable cause) {
        super(message, cause);
    }
}
