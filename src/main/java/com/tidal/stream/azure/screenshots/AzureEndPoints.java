package com.tidal.stream.azure.screenshots;

public abstract class AzureEndPoints {
    public static final String AZURE_BASE_URI="%s/%s/_apis/";
    public static final String TEST_RUN_ID_ENDPOINT="test/runs?buildUri=%s";
    public static final String FAILED_RUN_RESULT_ID_ENDPOINT="test/runs/%d/results?outcomes=Failed&?api-version=6.0";
    public static final String POST_SCREENSHOT_END_POINT="test/Runs/%d/Results/%d/attachments?api-version=5.1-preview.1";

    //Not to be instantiated
    private AzureEndPoints(){
    }
}
