package com.tidal.stream.zephyrscale;

import com.tidal.stream.httpRequest.FluentRequest;
import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.propertieshandler.PropertiesFinder;

import java.util.function.Function;

public class ZephyrScalePublish {

    Function<String, String> readProperty = PropertiesFinder::getProperty;

    public String toZephyrScale(String payLoad){
        return new FluentRequest()
                .set(readProperty.apply("zephyrScaleBaseUrl"))
                .setMediaType("application/json")
                .setHeader("Authorization", "Bearer " +
                        readProperty.apply("zephyr.auth.token"))
                .setHeader("Content-Type", "application/json")
                .setPayload(payLoad)
                .send(ReqType.POST)
                .getResponseString();

    }
}
