package com.tidal.stream.rest;


import com.tidal.stream.aws.Authorisation;
import com.tidal.stream.exceptions.RequestClassException;
import com.tidal.stream.exceptions.RuntimeTestException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;


public class Request {

//    private static final Log log = Log.getLogger(Request.class);

    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();
    private static final ThreadLocal<ValidatableResponse> response = new ThreadLocal<>();
    private static final ThreadLocal<HashMap<String, Object>> map = new ThreadLocal<>();


    private Request() {
    }

    /**
     * Get the RestAssured request specification instance where you add the properties
     * for your request
     *
     * @return instance of the request specification
     */
    public static RequestSpecification get() {
        if (requestSpec.get() == null) {
            throw new RequestClassException("Request Specification has not been set up");
        }
        return requestSpec.get();
    }

    /**
     * Initiate the RequestSpecification for the RestAssured call without a base URI.
     * Base URI can be added later or used with method names.
     */
    public static void set() {
        requestSpec.set(given().spec(new RequestSpecBuilder().build()));
        createMap();
    }

    /**
     * This method sets the Base Uri with the RequestSpecification and in turn overrides
     * the 'RestAssured.BaseUri' value if it is already present
     *
     * @param baseUri the base uri for the request to be made
     */
    public static void set(String baseUri) {
        requestSpec.set(given().spec(new RequestSpecBuilder().setBaseUri(baseUri).build()));
        createMap();
    }

    /**
     * Sets the base Uri as a com.tidal.api.rest assured property instead of a request specification instance.
     * This would enable the creation of a new instance of request specification without deleting
     * the base Uri
     *
     * @param baseUri the base uri or end point
     */
    public static void setBaseUri(String baseUri) {
        RestAssured.baseURI = baseUri;
        requestSpec.set(given().spec(new RequestSpecBuilder().build()));
        createMap();
    }

    /**
     * Resets the request specification to a new instance
     * This is required to flush the stored data in case a new signature is needed
     */
    public static void resetSpec() {
        requestSpec.remove();
        requestSpec.set(given().spec(new RequestSpecBuilder().build()));
    }

    /**
     * Resets the request specification to a new instance
     * This is required to flush the stored data in case a new signature is needed
     */
    public static void reset() {
        requestSpec.remove();
        map.remove();
        response.remove();
        requestSpec.set(given().spec(new RequestSpecBuilder().build()));
    }

    /**
     * Built for AWS Signature Authorization only
     *
     * @param authorisation instance of Auth class
     */
    @SuppressWarnings("unchecked")
    public static void set(Authorisation authorisation) {
        Map<String, String> headers = authorisation.getAuthHeaders();
        requestSpec.get().headers((Map<String, String>) authorisation.getAuthHeaders());
    }

    public static <T> void setHeader(String key, T value) {
        requestSpec.get().header(key, value);
    }

    public static <T> void setQueryParams(String key, T value) {
        requestSpec.get().queryParam(key, value);
    }

    public static <T> void setPayload(String payload){
        requestSpec.get().body(payload);
    }

    /**
     * A data context map set up to carry data across steps
     *
     * @param key   key for assigning value
     * @param value assigned value
     * @param <T>   type of 'value' set
     */
    public static <T> void setData(String key, T value) {
        map.get().put(key, value);
    }

    public static <T> void setData(DataEnum data, T value) {
        createMap();
        map.get().put(data.getValue(), value);
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
        return (T) map.get().get(key);
    }

    public static <T> Optional<T> getData(DataEnum data) {
        if(map.get() == null){
            Request.set();
        }
        T value = (T) map.get().get(data.getValue());
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    /**
     * Sends the request to the end point
     *
     * @param reqType specifies Get, Post, Delete etc...
     */
    public static void send(ReqType reqType) {
        send(reqType, "");
    }

    /**
     * Sends the request to the end point with a Uri path
     *
     * @param reqType   specifies Get, Post, Delete etc...
     * @param uriOrPath the uri path that can be appended
     */
    public static void send(ReqType reqType, String uriOrPath) {

        switch (reqType) {
            case GET:
                response.set(requestSpec.get().when().get(uriOrPath).then().log().all());
                break;
            case POST:
                response.set(requestSpec.get().when().post(uriOrPath).then().log().all());
                break;
            case DELETE:
                response.set(requestSpec.get().when().delete(uriOrPath).then().log().all());
                break;
            case PUT:
                response.set(requestSpec.get().when().put(uriOrPath).then().log().all());
                break;
            case PATCH:
                response.set(requestSpec.get().when().patch(uriOrPath).then().log().all());
        }
    }

    /**
     * This is a validatable response that is different from a normal restassured response
     *
     * @return a validatable response of restassured
     */
    public static ValidatableResponse response() {
        if (response.get() == null) {
            throw new RuntimeTestException("Response is null : Check if the request is sent");
        }
        return response.get();
    }

    public static String getResponseString(){
        if (response.get() == null) {
            throw new RuntimeTestException("Response is null : Check if the request is sent");
        }
        return response.get().extract().asString();
    }

    public static void logResponse() {
        if (response.get() == null) {
            throw new RuntimeTestException("Response is null : Check if the request is sent");
        }
//        log.info(response().extract().asString());
    }

    private static void createMap() {
        if (map.get() == null) {
            map.set(new HashMap<>());
        }
    }
}
