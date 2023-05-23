package com.tidal.stream.azure.testresults;

import org.junit.Test;

public class NewTestRunTest {

    @Test
    public void createTestRunTest(){
        //These ids would be captured by framework before the test run
        int[] testPointIds = {1, 3, 4, 5};

        new NewTestRun.Builder()
                .withPayload("json/create-run.json")
                .withRunName("Azure Demo Test Plan 5") //Name of test run
                .withPlanId(1751)
                .withTestPointIds(testPointIds)
                .build().createTestRun();
    }
}
