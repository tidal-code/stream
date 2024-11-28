package com.tidal.stream.rest;

import com.tidal.stream.filehandler.FileReader;
import com.tidal.stream.json.JsonReader;
import dev.tidalcode.flow.assertions.Assert;
import org.junit.After;
import org.junit.Test;

import static dev.tidalcode.flow.assertions.Assert.verify;


public class RequestTest {

    @After
    public void afterTest(){
        Request.reset();
    }

    @Test
    public void queryParamTest(){
        Request.set("https://reqres.in/api/users");
        Request.setQueryParams("page", "2");
        Request.send(ReqType.GET);
        Assert.verify("", JsonReader.readValue("page", Request.getResponseString()).toString()).isEqualTo("2");
    }

    @Test
    public void getTest(){
        Request.set("https://reqres.in/api/users/2");
        Request.send(ReqType.GET);
        verify("", JsonReader.readValue("data.id", Request.getResponseString()).toString()).isEqualTo("2");
    }

    @Test
    public void postTest(){
        Request.set("https://reqres.in/api/users");
        Request.setPayload(FileReader.readFileToString("reqrespost.json"));
        Request.send(ReqType.POST);
        verify("", JsonReader.readValue("name", Request.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void putTest(){
        Request.set("https://reqres.in/api/users/2");
        Request.setPayload(FileReader.readFileToString("reqresput.json"));
        Request.send(ReqType.PUT);
        verify("", JsonReader.readValue("name", Request.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void patchTest(){
        Request.set("https://reqres.in/api/users");
        Request.setPayload(FileReader.readFileToString("reqrespost.json"));
        Request.send(ReqType.PATCH);
        verify("", JsonReader.readValue("name", Request.getResponseString()).toString()).isEqualTo("morpheus");
    }

    @Test
    public void deleteTest(){
        Request.set("https://reqres.in/api/users/2");
        Request.setPayload(FileReader.readFileToString("reqrespost.json"));
        Request.send(ReqType.DELETE);
        verify("", Request.getResponseString()).isEqualTo("");
//        verify("", Request.getStatusCode()).isEqualTo(204);
    }
}
