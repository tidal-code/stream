package com.tidal.stream.exceptions;

public class TimeoutException extends RuntimeException{
    public TimeoutException(){
    }

    public TimeoutException(String message){
        super(message);
    }

    public TimeoutException(Throwable cause){
        super(cause);
    }

    public TimeoutException(String message, Throwable cause){
        super(message, cause);
    }
}
