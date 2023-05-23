package com.tidal.stream.azure.testresults;



import com.tidal.utils.json.JsonReader;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * ADO scripts to upload data
 */
public class AdoResultReader {

    /**
     * Returns the test run id
     * @param testCaseName test case name
     * @param jsonPayload json payload
     * @return last run id
     */
    public static int getRunId(String testCaseName, String jsonPayload){
        int resultSize = JsonReader.readValue("value.size()", jsonPayload);
        AtomicInteger testRunId = new AtomicInteger();
        IntStream.range(0, resultSize).forEach(e -> {
            if(JsonReader.readValue(String.format("value.[%d].testCase.name", e), jsonPayload).equals(testCaseName)){
                testRunId.set(Integer.parseInt(JsonReader.readValue(String.format("value.[%d].lastTestRun.id", e), jsonPayload)));
            }
        });
        return testRunId.get();
    }

    /**
     * Get the last run id
     * @param testCaseName Test Case Name
     * @param jsonPayload Jason Payload
     * @return last run id
     */
    public static int getLastResultId(String testCaseName, String jsonPayload){
        int resultSize = JsonReader.readValue("value.size()", jsonPayload);
        AtomicInteger lastResultId = new AtomicInteger();
        IntStream.range(0, resultSize).forEach(e -> {
            if(JsonReader.readValue(String.format("value.[%d].testCase.name", e), jsonPayload).equals(testCaseName)){
                lastResultId.set(Integer.parseInt(JsonReader.readValue(String.format("value.[%d].lastResult.id", e), jsonPayload)));
            }
        });
        return lastResultId.get();
    }
}
