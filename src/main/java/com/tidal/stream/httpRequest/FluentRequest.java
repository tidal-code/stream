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

    private final String BASE_URI = "baseURI";
    private final String MEDIA_TYPE = "mediaType";
    private final String PAYLOAD = "payload";
    private final String RESPONSE_STRING = "responseString";


    private OkHttpClient client;
    private Response response;
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
     * Initiate the RequestSpecification for the Http Request  without a base URI.
     * Base URI can be added later or used with method names.
     */
    public FluentRequest set() {
        client = getNewOkHttpClient();
        createMap();
        logger.info("Creating a basic OKHttp client without a url");
        return this;
    }

    /**
     * This method sets the Base URI with the RequestSpecification and in turn overrides
     * the Base URI value if it is already present
     *
     * @param baseUri the base uri for the request to be made
     */
    public FluentRequest set(String baseUri) {
        if (client == null) {
            client = getNewOkHttpClient();
        }
        createMap();
        dataMap.put(BASE_URI, baseUri);
        logger.info("Creating a basic OKHttp client with a url {}", baseUri);
        return this;
    }

    /**
     * Sets the base Uri as a rest assured property instead of a request specification instance.
     * This would enable the creation of a new instance of request specification without deleting
     * the base Uri
     *
     * @param baseUri the base uri or end point
     */
    public FluentRequest setBaseUri(String baseUri) {
        if (dataMap == null) {
            createMap();
        }
        logger.info("Resetting the original client with new url {}", baseUri);
        dataMap.put(BASE_URI, baseUri);
        return this;
    }

    /**
     * Sets the media type as json or xml or other types
     *
     * @param mediaType media type
     */
    public FluentRequest setMediaType(String mediaType) {
        dataMap.put(MEDIA_TYPE, mediaType);
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
        dataMap.put(PAYLOAD, payload);
        logger.info("Setting the payload:  {}", payload);
        return this;
    }

    /**
     * A data context map set up to carry data across steps
     *
     * @param key   key for assigning value
     * @param value assigned value
     * @param <T>   type of 'value' set
     */
    public <T> FluentRequest setData(String key, T value) {
        dataMap.put(key, value);
        logger.info("Storing test context data {} : {}", key, value);
        return this;
    }

    public <T> FluentRequest setData(DataEnum data, T value) {
        createMap();
        dataMap.put(data.getValue(), value);
        logger.info("Storing test context data  with DataEnum contact{} : {}", data.getValue(), value);
        return this;
    }

    /**
     * Retrieves the data from a static context map and at the same time ensured thread safety
     *
     * @param key key to assign the value
     * @param <T> object type
     * @return a static map in context of the current thread
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) dataMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getData(DataEnum data) {
        if (dataMap == null) {
            this.set();
        }
        T value = (T) dataMap.get(data.getValue());
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }


    /**
     * Sends the request to the end point with a Uri path
     *
     * @param reqType specifies Get, Post, Delete etc...
     */
    public FluentRequest send(ReqType reqType) {

        MediaType mediaType = MediaType.parse("application/json");
        if (dataMap.get(MEDIA_TYPE) != null) {
            mediaType = MediaType.parse((String) dataMap.get(MEDIA_TYPE));
        }
        RequestBody body = RequestBody.create("", mediaType);
        if (formBodyBuilder == null && dataMap.get(PAYLOAD) != null) {
            body = RequestBody.create((String) dataMap.get(PAYLOAD), mediaType);
        } else {
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
            logger.info("Sending the request type as {} to the url {}", reqType, dataMap.get(BASE_URI));
        }

        try {
            response = client.newCall(httpRequest).execute();
            //The response has to be stored because the default response.body().string() is auto-closeable
            if (dataMap.get(RESPONSE_STRING) == null) {
                dataMap.put(RESPONSE_STRING, response.body().string());
            }
            response.close();
            logger.info("Received the response: {}" , dataMap.get(RESPONSE_STRING));
        } catch (IOException e) {
            throw new RuntimeTestException("IOException with request" + e.getMessage());
        }
        return this;
    }

    private HttpUrl.Builder queryBuilder() {
        HttpUrl.Builder builder = HttpUrl.get((String) dataMap.get(BASE_URI)).newBuilder();

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
        return (String) dataMap.get(RESPONSE_STRING);
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
