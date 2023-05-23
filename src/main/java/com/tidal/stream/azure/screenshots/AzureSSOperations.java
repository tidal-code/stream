package com.tidal.stream.azure.screenshots;

import java.util.List;

/**
 * To upload azure screenshots
 */
public interface AzureSSOperations {

    int getLatestTestRunId(String buildUri);

    List<AzureTestCaseModel> getFailedTestCasesFromAzureTestRun(Integer runId);

    void postScreenshotToAzure(int runId, List<AzureTestCaseModel> azureTestCaseModels);

    String generateAzureScreenshotPayload(AzureTestCaseModel azureTestCaseModel);

    void uploadScreenShot();
}
