package com.tidal.stream.httpRequest;

import com.tidal.utils.exceptions.PropertyHandlerException;
import com.tidal.utils.exceptions.RuntimeTestException;
import com.tidal.utils.propertieshandler.PropertiesFinder;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FluentRequest {

    private static final Logger logger = LoggerFactory.getLogger(FluentRequest.class);
    private final Function<String, String> timeOut = PropertiesFinder::getProperty;

    private OkHttpClient client;
    private String baseURI;
    private String mediaType;
    private String responseBody;
    private Response response;
    private String payload;
    private okhttp3.Request httpRequest;
    private HashMap<String, Object> dataMap;
    private Map<String, Object> headerMap;
    private Map<String, Object> queryParamMap;
    private Headers requestHeaders;
    private FormBody.Builder formBodyBuilder;

    private final UnaryOperator<String> readTimeOut = s -> {
        try {
            return timeOut.apply(s) == null ? "10" : timeOut.apply(s);
        } catch (PropertyHandlerException ignored) {
            return "10";
        }
    };

    /**
     * Method to set your own custom HttpRequest in case the built-in builder is not sufficient
     * for your request
     */
    public void setHttpRequest(okhttp3.Request builtRequest) {
        httpRequest = builtRequest;
    }


    /**
     * This method sets the Base URI with the RequestSpecification and in turn overrides
     * the Base URI value if it is already present
     *
     * @param baseURI the base uri for the request to be made
     */
    public FluentRequest set(String baseURI) {
        if (client == null) {
            client = getNewOkHttpClient();
        }
        this.baseURI = baseURI;
        createMap();
        logger.info("Creating a basic OKHttp client with a url {}", baseURI);
        return this;
    }

    /**
     * Sets the media type as json or xml or other types
     *
     * @param mediaType media type
     */
    public FluentRequest setMediaType(String mediaType) {
        this.mediaType = mediaType;
        logger.info("Setting the media type as {}", mediaType);
        return this;
    }

    private OkHttpClient getNewOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("connection.timeout"))))
                .readTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("read.timeout"))))
                .writeTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("write.timeout"))))
                .callTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("call.timeout"))))
                .build();
    }

    /**
     * Sets header values to the request. There is no limit to the number of headers
     *
     * @param key   header key
     * @param value header value
     */
    public FluentRequest setHeader(String key, Object value) {
        headerMap.put(key, value);
        logger.info("Setting the header as {} : {}", key, value);
        return this;
    }

    public FluentRequest setBearerToken(String token){
        headerMap.put("Authorization", "Bearer " + token);
        return this;
    }

    /**
     * Sets the query params. Can only add a maximum of two
     *
     * @param key   query param key
     * @param value query param value
     */
    public FluentRequest setQueryParams(String key, Object value) {
        queryParamMap.put(key, value);
        return this;
    }

    /**
     * Sets the form param elements for authorization building
     * @param key form param key
     * @param value form param value
     */
    public FluentRequest setFormParam(String key, String value){
        if(null == formBodyBuilder) {
            formBodyBuilder = new FormBody.Builder();
        }
        formBodyBuilder.add(key, value);
        return this;
    }

    /**
     * Sets request payload
     * @param payload request payload
     */
    public FluentRequest setPayload(String payload) {
        this.payload = payload;
        logger.info("Setting the payload:  {}", payload);
        return this;
    }

    /**
     * Sends the request to the end point with a Uri path
     *
     * @param reqType specifies Get, Post, Delete etc...
     */
    public FluentRequest send(ReqType reqType) {

        MediaType parsedMediatype = null;

        if (mediaType != null) {
            parsedMediatype = MediaType.parse( mediaType);
        }

        RequestBody body = null;

        if (formBodyBuilder == null && payload != null) {
            body = RequestBody.create(payload, parsedMediatype);
        } else if(formBodyBuilder != null){
            body = formBodyBuilder.build();
        }

        applyHeaders();

        if (httpRequest == null) {
            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder().url(queryBuilder().build());

            switch (reqType) {
                case GET:
                    httpRequest = requestBuilder
                            .get()
                            .headers(requestHeaders)
                            .build();
                    break;
                case HEAD:
                    httpRequest = requestBuilder
                            .head()
                            .headers(requestHeaders)
                            .build();
                    break;
                case DELETE:
                    httpRequest = requestBuilder
                            .delete()
                            .headers(requestHeaders)
                            .build();
                    break;
                default:
                    httpRequest = requestBuilder
                            .method(reqType.getRequestType().toUpperCase(Locale.ROOT), body)
                            .headers(requestHeaders)
                            .build();
            }
            logger.info("Sending the request type as {} to the url {}", reqType, httpRequest.url());
        }

        try {
            response = client.newCall(httpRequest).execute();
            responseBody = response.body().string();
            response.close();
            logger.info("Response status: {} {}", response.code(), response.message());
            logger.info("Response Message: {}" , responseBody);
        } catch (IOException e) {
            throw new RuntimeTestException("IOException with request" + e.getMessage());
        }
        return this;
    }

    private HttpUrl.Builder queryBuilder() {
        HttpUrl.Builder builder = HttpUrl.get(baseURI).newBuilder();

        queryParamMap.forEach((k, v) -> {
            builder.addQueryParameter(k, String.valueOf(v));
        });

        return builder;
    }

    private void applyHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String key : headerMap.keySet()) {
            headerBuilder.add(key, (String) Objects.requireNonNull(headerMap.get(key)));
        }

        requestHeaders = headerBuilder.build();
    }

    public Response response() {
        if (response == null) {
            throw new RuntimeTestException("Response is null : Check if the request is sent");
        }
        return response;
    }

    public int getStatusCode() {
        if (response == null) {
            throw new RuntimeTestException("Status code is null : Check if the request is sent");
        }
        return response.code();
    }

    public String getResponseString() {
        if (response == null) {
            throw new RuntimeTestException("Response string is null : Check if the request is sent");
        }
        return responseBody;
    }

    private void createMap() {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }

        if (headerMap == null) {
            headerMap = new HashMap<>();
        }

        if(queryParamMap == null){
            queryParamMap =  new LinkedHashMap<>();
        }
    }
}
