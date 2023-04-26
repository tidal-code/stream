package com.tidal.stream.mockserver;


import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class MockServer {

    private WireMockServer wiremockServer;

    private String response;
    private int port;
    private String contentType;

    public MockServer withContentType(String contentType){
        this.contentType = contentType;
        return this;
    }

    public MockServer withPort(int port){
        this.port= port;
        return this;
    }

    public MockServer shouldRespondWith(String response){
        this.response = response;
        return this;
    }


    public void postMockRequest(){
        wiremockServer = new WireMockServer(options().port(port));
        wiremockServer.start();

        wiremockServer.stubFor(post(urlPathMatching("/response/.*")).
                willReturn(aResponse().
                        withHeader("Content-Type", contentType).
                        withBody(response)));
    }

    public void getMockRequest(){
        wiremockServer = new WireMockServer(options().port(port));
        wiremockServer.start();

        wiremockServer.stubFor(get(urlPathMatching("/response/.*")).
                willReturn(aResponse().
                        withHeader("Content-Type", contentType).
                        withBody(response)));
    }

    public void stop(){
        wiremockServer.stop();
    }
}
