package com.tidal.stream.aws;


import com.tidal.stream.exceptions.RuntimeTestException;

public abstract class Authorisation {
    public <T> T getAuthHeaders(){
        throw new RuntimeTestException("Authorization has not been setup");
    }
    public <T> T getSignedHeaders(){
        throw new RuntimeTestException("Headers have not been setup");
    }
    public <T> T getParsedUrl(){
        throw new RuntimeTestException("Url has not been setup");
    }
}
