package com.tidal.stream.training;

import com.tidal.stream.filehandler.FileReader;
import com.tidal.stream.http.Request;
import com.tidal.stream.rest.ReqType;
import org.junit.Test;

public class AuthTokenRequestTest {

    @Test
    public void getAccessTokenTest(){
       String token = getAccessToken();
    }

    public String getAccessToken(){
        Request.set("https://thinking-tester-contact-list.herokuapp.com/users/login");
        Request.setPayload(FileReader.readFileToString("accesstokenpayload.json"));
        Request.send(ReqType.POST);
        String token = Request.getResponseString(); //todo --  it was Request.getResponseValue();
        System.out.println(token);
        return token;
    }
}
