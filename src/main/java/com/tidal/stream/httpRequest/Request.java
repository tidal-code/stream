package com.tidal.stream.httpRequest;

import okhttp3.Response;

import java.util.Optional;


public class Request {

    private static final ThreadLocal<FluentRequest> FLUENT_REQUEST = ThreadLocal.withInitial(FluentRequest::new);


    private Request() {
    }

    /**
     * Method to set your own custom HttpRequest in case the built-in builder is not sufficient
     * for your request
     */
    public static void setHttpRequest(okhttp3.Request builtRequest) {
        FLUENT_REQUEST.get().setHttpRequest(builtRequest);
    }

    /**
     * Initiate the RequestSpecification for the Http Request  without a base URI.
     * Base URI can be added later or used with method names.
     */
    public static void set() {
        FLUENT_REQUEST.get().set();
    }

    /**
     * This method sets the Base URI with the RequestSpecification and in turn overrides
     * the Base URI value if it is already present
     *
     * @param baseUri the base uri for the request to be made
     */
    public static void set(String baseUri) {
       FLUENT_REQUEST.get().set(baseUri);
    }

    /**
     * Sets the base Uri as a rest assured property instead of a request specification instance.
     * This would enable the creation of a new instance of request specification without deleting
     * the base Uri.
     *
     * @param baseUri the base uri or end point
     */
    public static void setBaseUri(String baseUri) {
        FLUENT_REQUEST.get().set(baseUri);
    }

    /**
     * Sets the media type as json or xml or other types
     *
     * @param mediaType media type
     */
    public static void setMediaType(String mediaType) {
        FLUENT_REQUEST.get().setMediaType(mediaType);
    }

    /**
     * Resets the request specification to a new instance
     * This is required to flush the stored data in case a new signature is needed
     */
    public static void reset() {
        FLUENT_REQUEST.remove();
    }

    /**
     * Sets header values to the request. There is no limit to the number of headers
     *
     * @param key   header key
     * @param value header value
     */
    public static void setHeader(String key, Object value) {
        FLUENT_REQUEST.get().setHeader(key, value);
    }

    /**
     * Sets the query params. Can only add a maximum of two
     *
     * @param key   query param key
     * @param value query param value
     */
    public static void setQueryParams(String key, Object value) {
        FLUENT_REQUEST.get().setQueryParams(key, value);
    }

    public static void setFormParam(String key, String value) {
        FLUENT_REQUEST.get().setFormParam(key, value);
    }

    public static void setPayload(String payload) {
        FLUENT_REQUEST.get().setPayload(payload);
    }

    /**
     * A data context map set up to carry data across steps
     *
     * @param key   key for assigning value
     * @param value assigned value
     * @param <T>   type of 'value' set
     */
    public static <T> void setData(String key, T value) {
        FLUENT_REQUEST.get().setData(key, value);
    }

    public static <T> void setData(DataEnum data, T value) {
       FLUENT_REQUEST.get().setData(data, value);
    }

    /**
     * Retrieves the data from a static context map and at the same time ensured thread safety
     *
     * @param key key to assign the value
     * @param <T> object type
     * @return a static map in context of the current thread
     */
    @SuppressWarnings("unchecked")
    public static <T> T getData(String key) {
        return (T) FLUENT_REQUEST.get().getData(key);
    }

    public static <T> Optional<T> getData(DataEnum data) {
        return FLUENT_REQUEST.get().getData(data);
    }

    /**
     * Sends the request to the end point with a Uri path
     *
     * @param reqType specifies Get, Post, Delete etc...
     */
    public static void send(ReqType reqType) {
        FLUENT_REQUEST.get().send(reqType);
    }

    public static Response response() {
       return FLUENT_REQUEST.get().response();
    }

    public static int getStatusCode() {
        return FLUENT_REQUEST.get().getStatusCode();
    }

    public static String getResponseString() {
        return FLUENT_REQUEST.get().getResponseString();
    }
}



