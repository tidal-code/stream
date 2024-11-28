package com.tidal.stream.rest;

import com.tidal.stream.filehandler.FileReader;
import com.tidal.stream.json.JsonReader;
import dev.tidalcode.flow.assertions.Assert;
import org.junit.After;
import org.junit.Test;

import static dev.tidalcode.flow.assertions.Assert.verify;


public class RestRequestTest {

    @After
    public void afterTest(){
        RestRequest.reset();
    }

    @Test
    public void queryParamTest(){
        RestRequest.set("https://reqres.in/api/users");
        RestRequest.setQueryParams("page", "2");
        RestRequest.send(ReqType.GET);
        Assert.verify("", JsonReader.readValue("page", RestRequest.getResponseString()).toString()).isEqualTo("2");
    }

    @Test
    public void getTest(){
        RestRequest.set("https://reqres.in/api/users/2");
        RestRequest.send(ReqType.GET);
        verify("", JsonReader.readValue("data.id", RestRequest.getResponseString()).toString()).isEqualTo("2");
    }

    @Test
    public void postTest(){
        RestRequest.set("https://reqres.in/api/users");
        RestRequest.setPayload(FileReader.readFileToString("reqrespost.json"));
        RestRequest.send(ReqType.POST);
        verify("", JsonReader.readValue("name", RestRequest.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void putTest(){
        RestRequest.set("https://reqres.in/api/users/2");
        RestRequest.setPayload(FileReader.readFileToString("reqresput.json"));
        RestRequest.send(ReqType.PUT);
        verify("", JsonReader.readValue("name", RestRequest.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void patchTest(){
        RestRequest.set("https://reqres.in/api/users");
        RestRequest.setPayload(FileReader.readFileToString("reqrespost.json"));
        RestRequest.send(ReqType.PATCH);
        verify("", JsonReader.readValue("name", RestRequest.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void deleteTest(){
        RestRequest.set("https://reqres.in/api/users/2");
        RestRequest.setPayload(FileReader.readFileToString("reqrespost.json"));
        RestRequest.send(ReqType.DELETE);
        verify("", RestRequest.getResponseString()).isEqualTo("");
//        verify("", RestRequest.getStatusCode()).isEqualTo(204);
    }
}
