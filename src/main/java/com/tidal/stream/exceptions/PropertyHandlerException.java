package com.tidal.stream.exceptions;

public class PropertyHandlerException extends RuntimeException{
    public PropertyHandlerException(){}

    public PropertyHandlerException(String message){
        super(message);
    }

    public PropertyHandlerException(Throwable cause){
        super(cause);
    }

    public PropertyHandlerException(String message, Throwable cause){
        super(message, cause);
    }
}
