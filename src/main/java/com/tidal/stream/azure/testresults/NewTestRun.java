package com.tidal.stream.azure.testresults;

import com.tidal.stream.azure.AppConstants;
import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.filehandlers.FileReader;
import com.tidal.utils.json.JsonWriter;
import com.tidal.utils.urlbuilders.Protocol;
import com.tidal.utils.urlbuilders.Url;

import java.io.InputStream;

public class NewTestRun {

    private final String testRunName;
    private final int testPlanId;
    private String payLoad;
    private final int[] testPointIds;

    public NewTestRun(Builder builder) {
        this.testPlanId = builder.testPlanId;
        this.testRunName = builder.testRunName;
        this.payLoad = builder.payLoad;
        this.testPointIds = builder.testPointIds;
    }

    public String loadJson(String json) {
        if (json.startsWith("{") | json.startsWith("[")) {
            payLoad = json;
        } else if (json.endsWith(".json")) {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(json);
            payLoad = FileReader.readStreamToString(inputStream);
        }

        payLoad = new JsonWriter("name", payLoad).setValue(testRunName).extract();
        payLoad = new JsonWriter("plan.id", payLoad).setValue(testPlanId).extract();
        payLoad = new JsonWriter("pointIds", payLoad).setValue(testPointIds).extract();

        return payLoad;
    }

    public void createTestRun() {
        String projectUrl = new Url.Builder()
                .basePath("runs?api-version=5.0")
                .build(Protocol.SECURE, AppConstants.PROJECT_TEST_API_ENDPOINT)
                .value();

        Request.set(projectUrl);
        Request.setMediaType("application/json");
        Request.setHeader("Authorization", AppConstants.AZURE_AUTHORIZATION.get());
        Request.setPayload(loadJson(payLoad));
        Request.send(ReqType.POST);
        System.out.println(Request.getResponseString());
    }

    public static class Builder{
        private String testRunName;
        private int testPlanId;
        private String payLoad;
        private int[] testPointIds;

        public Builder withRunName(String testRunName){
            this.testRunName = testRunName;
            return this;
        }

        public Builder withPlanId(int testPlanId){
            this.testPlanId = testPlanId;
            return this;
        }

        public Builder withPayload(String payload){
            this.payLoad = payload;
            return this;
        }

        public Builder withTestPointIds(int[] testPointIds){
            this.testPointIds = testPointIds;
            return this;
        }

        public NewTestRun build(){
           return new NewTestRun(this);
        }
    }
}
