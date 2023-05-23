package com.tidal.stream.azure.testresults;


import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.filehandlers.FileReader;
import com.tidal.utils.json.JsonWriter;
import com.tidal.utils.propertieshandler.PropertiesFinder;

import java.util.Base64;
import java.util.function.UnaryOperator;

/**
 * Result update status
 */
public class AdoTestResultUpdates {

    public enum Results{
        PASSED("Passed"),
        FAILED("Failed");

        final String value;

        Results(String value) {
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }

    public static void updateTestResult(String scenarioName, Results results){
        UnaryOperator<String> getProperty = PropertiesFinder::getProperty;
        Request.set(getProperty.apply("ado.testpoints"));

        String authorization = "Basic " + Base64.getEncoder().encodeToString( (":" + getProperty.apply("ado.auth")).getBytes());
        Request.setMediaType("text/plain");
        Request.setHeader("Authorization", authorization);
        Request.setPayload("");
        Request.send(ReqType.GET);
        String result = Request.getResponseString();

        int testPointId = AdoResultReader.getRunId(scenarioName, result);

        Request.reset();

        String testUpdateUrl = getProperty.apply("ado.testresults");
        String updatedUrlWithTestPointId = testUpdateUrl.replace("{testpoint}", String.valueOf(testPointId));
        Request.set(updatedUrlWithTestPointId);
        Request.setMediaType("application/json");
        Request.setHeader("Authorization", authorization);

        String payload = FileReader.readFileToString("test-result.json");
        int testRunId = AdoResultReader.getLastResultId(scenarioName, result);
        String updatedPayload = payload.replace("{status}", results.getValue());
        updatedPayload = new JsonWriter("$[0].id", updatedPayload).setValue(testRunId).extract();

        Request.setPayload(updatedPayload);
        Request.send(ReqType.PATCH);
    }

}
