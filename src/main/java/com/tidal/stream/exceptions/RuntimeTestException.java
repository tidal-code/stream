package com.tidal.stream.exceptions;

public class RuntimeTestException extends RuntimeException{
    public RuntimeTestException(){}

    public RuntimeTestException(String message){
        super(message);
    }

    public RuntimeTestException(Throwable cause){
        super(cause);
    }

    public RuntimeTestException(String message, Throwable cause){
        super(message, cause);
    }
}
