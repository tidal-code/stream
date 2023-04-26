package com.tidal.stream.exceptions;

public class DataException extends RuntimeException {
    public DataException(){
        this("No data set for this key");
    }

    public DataException(String message){
        super("No data set for this key \n\r" + message);
    }

    public DataException(Throwable cause){
        super(cause);
    }

    public DataException(String message, Throwable cause){
        super(message, cause);
    }
}
