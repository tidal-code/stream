package com.tidal.stream.httpRequest;

public enum ReqType {
    GET("get"),
    POST("post"),
    PUT("put"),
    PATCH("patch"),
    HEAD("head"),
    OPTIONS("options"),
    DELETE("delete");

    private final String value;

    ReqType(String value) {
        this.value = value;
    }

    public String getRequestType(){
        return value;
    }

    public static ReqType getEnum(String value) {
        return ReqType.valueOf(value.toUpperCase());
    }
}
