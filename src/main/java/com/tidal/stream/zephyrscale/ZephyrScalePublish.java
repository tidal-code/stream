package com.tidal.stream.zephyrscale;

import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.propertieshandler.PropertiesFinder;

import java.util.function.Function;

public class ZephyrScalePublish {

    Function<String, String> readProperty = PropertiesFinder::getProperty;

    public String toZephyrScale(String payLoad){
        Request.set(readProperty.apply("zephyrScaleBaseUrl"));
        Request.setMediaType("application/json");
        Request.setHeader("Authorization", "Bearer " +
                readProperty.apply("zephyr.auth.token"));
        Request.setHeader("Content-Type", "application/json");
        Request.setPayload(payLoad);
        Request.send(ReqType.POST);
        String response = Request.getResponseString();
        Request.reset();
        return response;
    }
}
