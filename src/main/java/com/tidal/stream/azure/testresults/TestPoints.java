package com.tidal.stream.azure.testresults;

import com.tidal.stream.azure.AppConstants;
import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.json.JsonReader;
import com.tidal.utils.propertieshandler.PropertiesFinder;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class TestPoints {

    public int[] testPointIds(String[] tests) {
        return getTestPointsFromAzure(tests);
    }

    private int[] getTestPointsFromAzure(String[] tests) {
        UnaryOperator<String> getProperty = PropertiesFinder::getProperty;
        Request.set(getProperty.apply("ado.testpoints"));

        String authorization = AppConstants.AZURE_AUTHORIZATION.get();
        Request.setMediaType("text/plain");
        Request.setHeader("Authorization", authorization);
        Request.send(ReqType.GET);
        String result = Request.getResponseString();
        Request.reset();
        return extractTestPoints(result, tests);
    }

    private int[] extractTestPoints(String azureResponse, String[] tests){
        JSONArray testCases = JsonReader.readValue("value[*].testCase.name", azureResponse);
        List<Integer> testPoints = new ArrayList<>();

        for (String test : tests) {
            for (int i1 = 0; i1 < testCases.size(); i1++) {
                if (test.equals(testCases.get(i1))) {
                    int testPointId = JsonReader.readValue("value[" + i1 + "].id", azureResponse);
                    testPoints.add(testPointId);
                }
            }
        }
        return testPoints.stream().mapToInt(Integer::intValue).toArray();
    }
}
