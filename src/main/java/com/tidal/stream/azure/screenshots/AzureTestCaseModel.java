package com.tidal.stream.azure.screenshots;


/**
 * Data model to upload azure screenshots
 */
public class AzureTestCaseModel {

    private int testCaseId;
    private String testCaseName;

    public int getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }
}
