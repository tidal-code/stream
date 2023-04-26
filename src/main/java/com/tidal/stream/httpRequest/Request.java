package com.tidal.stream.httpRequest;
import com.tidal.stream.exceptions.RuntimeTestException;
import okhttp3.*;

import java.io.IOException;
import java.util.*;


public class Request {

    private static final String BASE_URI = "baseURI";
    private static final String MEDIA_TYPE = "mediaType";
    private static final String PAYLOAD = "payload";
    private static final String RESPONSE_STRING = "responseString";
    private static final String QUERY_PARAM_ONE_KEY = "queryParamOneKey";
    private static final String QUERY_PARAM_TWO_KEY = "queryParamTwoKey";

    private static final ThreadLocal<OkHttpClient> CLIENT = new ThreadLocal<>();
    private static final ThreadLocal<Response> RESPONSE = new ThreadLocal<>();
    private static final ThreadLocal<okhttp3.Request> HTTP_REQUEST = new ThreadLocal<>();
    private static final ThreadLocal<HashMap<String, Object>> DATA_MAP = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> HEADER_MAP = new ThreadLocal<>();
    private static final ThreadLocal<Headers> REQUEST_HEADERS = new ThreadLocal<>();


    private Request() {
    }

    /**
     * Method to set your own custom HttpRequest in case the built-in builder is not sufficient
     * for your request
     *
     */
    public static void setHttpRequest(okhttp3.Request builtRequest) {
        HTTP_REQUEST.set(builtRequest);
    }

    /**
     * Initiate the RequestSpecification for the Http Request  without a base URI.
     * Base URI can be added later or used with method names.
     */
    public static void set() {
        CLIENT.set(new OkHttpClient().newBuilder().build());
        createMap();
    }

    /**
     * This method sets the Base URI with the RequestSpecification and in turn overrides
     * the Base URI value if it is already present
     *
     * @param baseUri the base uri for the request to be made
     */
    public static void set(String baseUri) {
        if (CLIENT.get() == null) {
            CLIENT.set(new OkHttpClient().newBuilder().build());
        }
        createMap();
        DATA_MAP.get().put(BASE_URI, baseUri);
    }

    /**
     * Sets the base Uri as a rest assured property instead of a request specification instance.
     * This would enable the creation of a new instance of request specification without deleting
     * the base Uri
     *
     * @param baseUri the base uri or end point
     */
    public static void setBaseUri(String baseUri) {
        if(DATA_MAP.get() == null) {
            createMap();
        }
        DATA_MAP.get().put(BASE_URI, baseUri);
    }

    /**
     * Sets the media type as json or xml or other types
     * @param mediaType media type
     */
    public static void setMediaType(String mediaType){
        DATA_MAP.get().put(MEDIA_TYPE, mediaType);
    }


    /**
     * Resets the request specification to a new instance
     * This is required to flush the stored data in case a new signature is needed
     */
    public static void reset() {
        CLIENT.remove();
        DATA_MAP.remove();
        HEADER_MAP.remove();
        HTTP_REQUEST.remove();
        RESPONSE.remove();
        REQUEST_HEADERS.remove();
        CLIENT.set(new OkHttpClient().newBuilder().build());
    }

    /**
     * Sets header values to the request. There is no limit to the number of headers
     * @param key header key
     * @param value header value
     */
    public static void setHeader(String key, Object value) {
        HEADER_MAP.get().put(key, value);
    }

    /**
     * Sets the query params. Can only add a maximum of two
     * @param key query param key
     * @param value query param value
     */
    public static void setQueryParams(String key, Object value) {
        if(DATA_MAP.get().get(QUERY_PARAM_ONE_KEY) == null){
            DATA_MAP.get().put(QUERY_PARAM_ONE_KEY, key);
            DATA_MAP.get().put("queryParamOneValue", value);
        }

        else

        if(DATA_MAP.get().get(QUERY_PARAM_TWO_KEY) == null){
            DATA_MAP.get().put(QUERY_PARAM_TWO_KEY, key);
            DATA_MAP.get().put("queryParamTwoValue", value);
        }

    }

    public static void setPayload(String payload){
        DATA_MAP.get().put(PAYLOAD, payload);
    }

    /**
     * A data context map set up to carry data across steps
     *
     * @param key   key for assigning value
     * @param value assigned value
     * @param <T>   type of 'value' set
     */
    public static <T> void setData(String key, T value) {
        DATA_MAP.get().put(key, value);
    }

    public static <T> void setData(DataEnum data, T value) {
        createMap();
        DATA_MAP.get().put(data.getValue(), value);
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
        return (T) DATA_MAP.get().get(key);
    }

    public static <T> Optional<T> getData(DataEnum data) {
        if(DATA_MAP.get() == null){
            Request.set();
        }
        T value = (T) DATA_MAP.get().get(data.getValue());
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }


    /**
     * Sends the request to the end point with a Uri path
     *
     * @param reqType   specifies Get, Post, Delete etc...
     */
    public static void send(ReqType reqType) {

        MediaType mediaType = MediaType.parse("application/json");
        if((String) DATA_MAP.get().get(MEDIA_TYPE) != null) {
            mediaType = MediaType.parse((String) DATA_MAP.get().get(MEDIA_TYPE));
        }
        RequestBody body = RequestBody.create("", mediaType);
        if((String) DATA_MAP.get().get(PAYLOAD) != null) {
            body = RequestBody.create((String) DATA_MAP.get().get(PAYLOAD), mediaType);
        }
        applyHeaders();

        if(HTTP_REQUEST.get() == null) {
            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                    .url(queryBuilder().build());

            switch (reqType) {
                case GET:
                    HTTP_REQUEST.set(requestBuilder
                            .get()
                            .headers(REQUEST_HEADERS.get())
                            .build());
                    break;
                case DELETE:
                    HTTP_REQUEST.set(requestBuilder
                            .delete()
                            .headers(REQUEST_HEADERS.get())
                            .build());
                    break;
                default:
                    HTTP_REQUEST.set(requestBuilder
                            .method(reqType.getReqType().toUpperCase(Locale.ROOT), body)
                            .headers(REQUEST_HEADERS.get())
                            .build());
            }
        }

        try{
            RESPONSE.set(CLIENT.get().newCall(HTTP_REQUEST.get()).execute());
        } catch (IOException e) {
            throw new RuntimeTestException("IOException with request" + e.getMessage());
        }
    }

    private static HttpUrl.Builder queryBuilder(){
        HttpUrl.Builder builder = HttpUrl.get((String)DATA_MAP.get().get(BASE_URI)).newBuilder();
        if(DATA_MAP.get().get(QUERY_PARAM_ONE_KEY) != null){
            builder.addQueryParameter((String)DATA_MAP.get().get(QUERY_PARAM_ONE_KEY), (String)DATA_MAP.get().get("queryParamOneValue"));
        }
        if(DATA_MAP.get().get(QUERY_PARAM_TWO_KEY) != null){
            builder.addQueryParameter((String)DATA_MAP.get().get(QUERY_PARAM_TWO_KEY), (String)DATA_MAP.get().get("queryParamTwoValue"));
        }
        return builder;
    }

    private static void applyHeaders(){
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String key : HEADER_MAP.get().keySet()) {
            headerBuilder.add(key, (String) Objects.requireNonNull(HEADER_MAP.get().get(key)));
        }

        REQUEST_HEADERS.set(headerBuilder.build());
    }

    public static Response response() {
        if (RESPONSE.get() == null) {
            throw new RuntimeTestException("Response is null : Check if the request is sent");
        }
        return RESPONSE.get();
    }

    public static int getStatusCode(){
        if (RESPONSE.get() == null) {
            throw new RuntimeTestException("Status code is null : Check if the request is sent");
        }
        return RESPONSE.get().code();
    }

    public static String getResponseString(){
        if (RESPONSE.get() == null) {
            throw new RuntimeTestException("Response string is null : Check if the request is sent");
        }

        try{
            //The response has to be stored because the default response.body().string() is auto-closeable
            if(DATA_MAP.get().get(RESPONSE_STRING) == null) {
                DATA_MAP.get().put(RESPONSE_STRING, RESPONSE.get().body().string());
            }
            return (String)DATA_MAP.get().get(RESPONSE_STRING);
        } catch (IOException e) {
            throw new RuntimeTestException("IO Exception with response: " + e.getMessage());
        }
    }
    private static void createMap() {
        if (DATA_MAP.get() == null) {
            DATA_MAP.set(new HashMap<>());
        }

        if (HEADER_MAP.get() == null) {
            HEADER_MAP.set(new HashMap<>());
        }
    }
}



