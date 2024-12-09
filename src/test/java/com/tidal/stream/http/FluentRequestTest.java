package com.tidal.stream.http;

import com.tidal.stream.filehandler.FileReader;
import com.tidal.stream.httpRequest.FluentRequest;
import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.stream.json.JsonReader;
import org.junit.After;
import org.junit.Test;

import static dev.tidalcode.flow.assertions.Assert.verify;


public class FluentRequestTest {

    @Test
    public void queryParamTest() {
        FluentRequest fluentRequest = new FluentRequest();
        fluentRequest
                .set("https://reqres.in/api/users")
                .setMediaType("application/json")
                .setQueryParams("page", "2")
                .send(ReqType.GET);

        System.out.println("Response Code: " + fluentRequest.getStatusCode());
        verify("", JsonReader.readValue("page", fluentRequest.getResponseString()).toString()).isEqualTo("2");
    }

    @Test
    public void getTest() {
        FluentRequest fluentRequest = new FluentRequest();
        fluentRequest.set("https://reqres.in/api/users/2");
        fluentRequest.setMediaType("application/json");
        fluentRequest.send(ReqType.GET);
        verify("", JsonReader.readValue("data.id", fluentRequest.getResponseString()).toString()).isEqualTo("2");
    }

    @Test
    public void postTest() {
        FluentRequest fluentRequest = new FluentRequest();
        fluentRequest.set("https://reqres.in/api/users");
        fluentRequest.setMediaType("application/json");
        fluentRequest.setPayload(FileReader.readFileToString("reqrespost.json"));
        fluentRequest.send(ReqType.POST);
        verify("", JsonReader.readValue("name", fluentRequest.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void putTest() {
        FluentRequest fluentRequest = new FluentRequest();
        fluentRequest.set("https://reqres.in/api/users/2");
        fluentRequest.setMediaType("application/json");
        fluentRequest.setPayload(FileReader.readFileToString("reqresput.json"));
        fluentRequest.send(ReqType.PUT);
        verify("", JsonReader.readValue("name", fluentRequest.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void patchTest() {
        FluentRequest fluentRequest = new FluentRequest();
        fluentRequest.set("https://reqres.in/api/users");
        fluentRequest.setMediaType("application/json");
        fluentRequest.setPayload(FileReader.readFileToString("reqrespost.json"));
        fluentRequest.send(ReqType.PATCH);
//        fluentRequest.logResponse();
        verify("", JsonReader.readValue("name", fluentRequest.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void deleteTest() {
        FluentRequest fluentRequest = new FluentRequest();
        fluentRequest.set("https://reqres.in/api/users/2");
        fluentRequest.setMediaType("application/json");
        fluentRequest.setPayload(FileReader.readFileToString("reqrespost.json"));
        fluentRequest.send(ReqType.DELETE);
        verify("", fluentRequest.getResponseString()).isEqualTo("");
//        verify("", fluentRequest.getStatusCode()).isEqualTo(204);
    }
}
