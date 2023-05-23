package com.tidal.stream.azure.testresults;

import com.tidal.stream.azure.AppConstants;
import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.json.JsonReader;
import com.tidal.utils.propertieshandler.PropertiesFinder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TestRun {

    public int getTestRunId(String testRunName){
        Request.set(PropertiesFinder.getProperty("ado.testrun.endpoint"));
        Request.setHeader("Authorization", AppConstants.AZURE_AUTHORIZATION.get());
        Request.setMediaType("text/plain");
        Request.send(ReqType.GET);

        return extractRunId(testRunName, Request.getResponseString());
    }

    private int extractRunId(String testRunName, String responseString){
        int resultSize = JsonReader.readValue("value.size()", responseString);
        AtomicInteger lastResultId = new AtomicInteger();
        IntStream.range(0, resultSize).forEach(e -> {
            if(JsonReader.readValue(String.format("value.[%d].name", e), responseString).equals(testRunName)){
                lastResultId.set(Integer.parseInt(JsonReader.readValue(String.format("value.[%d].id", e), responseString)));
            }
        });
        return lastResultId.get();
    }
}
