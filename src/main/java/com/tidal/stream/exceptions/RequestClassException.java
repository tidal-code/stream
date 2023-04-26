package com.tidal.stream.exceptions;

public class RequestClassException extends RuntimeException {
    public RequestClassException(){
    }

    public RequestClassException(String message){
        super("Error occurred within Request class \n\r" + message);
    }

    public RequestClassException(Throwable cause){
        super(cause);
    }

    public RequestClassException(String message, Throwable cause){
        super(message, cause);
    }
}
