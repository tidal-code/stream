package com.tidal.stream.azure.testresults;

import org.junit.Test;

public class TestPointsTest {

    @Test
    public void getTestPointsTest() {
        String[] tests = {"Create Sales Order", "Get Request Demo", "Put Request Demo"};
        int[] ints = new TestPoints().testPointIds(tests);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    @Test
    public void createTestRun() {
        //In real life scenarios, the list of test cases will be generated by test runner.
        //All these will form part of pipeline scripts where the test cases capture and
        //test run creation will be done in the beginning of test automation runs.

        String[] tests = {
                "Post Request Demo",
                "Get Request Demo",
                "Put Request Demo"
        };

        new NewTestRun.Builder()
                .withPayload("json/create-run.json") //Sample payload file.
                .withRunName("Create Test Run Demo 2")
                .withTestPointIds(new TestPoints().testPointIds(tests))
                .withPlanId(1751) //This will be fed from each project's configuration file.
                .build().createTestRun();
    }

    @Test
    public void createUITestRun() {


        //In real life scenarios, the list of test cases will be generated by test runner.
        //All these will form part of pipeline scripts where the test cases capture and
        //test run creation will be done in the beginning of test automation runs.

        String[] tests = {
                "Item numbers and their order quantities are displayed on OIS101/H1 panel for more than one customer order entries",
                "Item numbers and their order quantities are displayed on OIS101/B1 panel for one customer order entry",
                "Sign in with Cloud Identities",
                "Temporarily release credit limit",
                "Permanently release credit limits",
                "Create and process standing order"
        };


        new NewTestRun.Builder()
                .withPayload("json/create-run.json") //Sample payload file.
                .withRunName("Create Test Run UI Tests POC Demo")
                .withTestPointIds(new TestPoints().testPointIds(tests))
                .withPlanId(1751) //This will be fed from each project's configuration file.
                .build().createTestRun();
    }
}
