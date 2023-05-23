package com.tidal.stream.azure.screenshots;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.PathNotFoundException;
import com.tidal.stream.httpRequest.ReqType;
import com.tidal.stream.httpRequest.Request;
import com.tidal.utils.exceptions.AzureOperationsException;
import com.tidal.utils.filehandlers.FilePaths;
import com.tidal.utils.filehandlers.FileReader;
import com.tidal.utils.filehandlers.Finder;
import com.tidal.utils.json.JsonReader;
import com.tidal.utils.json.JsonWriter;
import com.tidal.utils.utils.Helper;
import okhttp3.Credentials;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AzureScreenshotOperations implements AzureSSOperations {

    private static final Logger logger = Logger.getLogger(AzureScreenshotOperations.class);

    private static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    private static final String JSON_PATH_LATEST_RUN_ID = "value[0].id";
    private static final String JSON_PATH_TEST_CASE_ID = "value[%d].id";
    private static final String JSON_PATH_TEST_CASE_NAME = "value[%d].testCase.name";
    private final String authorizationCredential;
    private final String buildUri;

    private static final Path TARGET_FOLDER_PATH = Paths.get(Helper.getAbsoluteFromRelativePath(FilePaths.TARGET_FOLDER_PATH.getPath()));

    public AzureScreenshotOperations(String currentBuildUri, String azureToken) {
        buildUri = currentBuildUri;
        authorizationCredential = Credentials.basic("", azureToken);
    }

    /**
     * Get the latest test run id from Azure for the provided buildUri
     * @param buildUri The buildUri of the azure build pipeline
     * @return the latest test run id for the provided buildUri
     */

    @Override
    public int getLatestTestRunId(String buildUri) {
        int result;
        try {
            logger.info("Finding the test run for build URI: " + buildUri);
            String endPoint = AzureEndPoints.AZURE_BASE_URI + String.format(AzureEndPoints.TEST_RUN_ID_ENDPOINT, buildUri);
            Request.set(endPoint);
            Request.setHeader(AUTHENTICATION_HEADER_NAME, authorizationCredential);
            Request.send(ReqType.GET);

            //Retrieving the 'id' value from the response string: Json Path(Value[0].id)
            result = JsonReader.readValue(JSON_PATH_LATEST_RUN_ID, Request.getResponseString());

            logger.info("The test run ID in Azure for for build URI: " + buildUri + " is " + result);
            Request.reset();
        } catch (PathNotFoundException exception) {
            logger.info("Unable to find a test run in Azure for build URI: " + buildUri);
            throw new AzureOperationsException(exception.getMessage());
        }
        return result;
    }

    @Override
    public List<AzureTestCaseModel> getFailedTestCasesFromAzureTestRun(Integer runId) {
        List<AzureTestCaseModel> failedTestId = new ArrayList<>();
        try {
            String endPoint = AzureEndPoints.AZURE_BASE_URI + String.format(AzureEndPoints.FAILED_RUN_RESULT_ID_ENDPOINT, runId);
            Request.set(endPoint);
            Request.setHeader(AUTHENTICATION_HEADER_NAME, authorizationCredential);
            Request.send(ReqType.GET);
            String result = Request.getResponseString();

            //Retrieving the count value from the response: Json Path(count)
            int noOfFailedTests = JsonReader.readValue("count", result);

            logger.info("No of failed tests in azure for the run " + runId + " is " + noOfFailedTests);

            for (int count = 0; count < noOfFailedTests; count++) {
                AzureTestCaseModel azureTestCaseModel = new AzureTestCaseModel();

                //Retrieving the azure test case id; Json Path (Value[index].id)
                String testCaseIdPath = String.format(JSON_PATH_TEST_CASE_ID, count);

                //Retrieving the azure test case name; Json Path (value[%d].testCase.name)
                String testCaseNamePath = String.format(JSON_PATH_TEST_CASE_NAME, count);

                azureTestCaseModel.setTestCaseId(JsonReader.readValue(testCaseIdPath, result));
                azureTestCaseModel.setTestCaseName(JsonReader.readValue(testCaseNamePath, result));
                failedTestId.add(azureTestCaseModel);
            }
            Request.reset();
        } catch (PathNotFoundException exception) {
            logger.info("Unable to get run results for the test run ID " + runId);
            throw new AzureOperationsException(exception.getMessage());
        }
        return failedTestId;
    }

    /**
     * Post the screenshot data to azure
     *
     * @param runId               the test run ID for the latest build in azure
     * @param azureTestCaseModels list of model class data on test cases
     */
    @Override
    public void postScreenshotToAzure(int runId, List<AzureTestCaseModel> azureTestCaseModels) {
        logger.info("Starting upload process for failed tests under the run " + runId);
        azureTestCaseModels.parallelStream().forEach(azureTestCaseModel -> {
            String formattedScenarioName = azureTestCaseModel.getTestCaseName().replaceAll("[^a-zA-Z0-9]", "");
            if (Finder.findFileIfExists(formattedScenarioName + ".txt", TARGET_FOLDER_PATH).isPresent()) {
                String endPoint = AzureEndPoints.AZURE_BASE_URI + String.format(AzureEndPoints.POST_SCREENSHOT_END_POINT, runId, azureTestCaseModel.getTestCaseId());
                Request.set(endPoint);
                Request.setHeader(AUTHENTICATION_HEADER_NAME, authorizationCredential);
                String payload = generateAzureScreenshotPayload(azureTestCaseModel);
                Request.setPayload(payload);
                Request.send(ReqType.POST);
                Request.reset();
            }
        });
        logger.info("Uploading of screenshots to azure devops is completed");
    }


    /**
     * Generate the payload to use in the azure post attachment API call
     *
     * @param azureTestCaseModel model class which holds the scenario name and azure test ID
     * @return payload in post attachment API
     */
    @Override
    public String generateAzureScreenshotPayload(AzureTestCaseModel azureTestCaseModel) {
        logger.info("Generating upload payload for test case " + azureTestCaseModel.getTestCaseName());
        AzureAttachmentModel azureAttachmentModel = new AzureAttachmentModel();
        String formattedScenarioName = azureTestCaseModel.getTestCaseName().replaceAll("[^a-zA-Z0-9]", "");
        //Setting stream in the payload as base64 encoded string of screenshot image
        String screenshotContent = FileReader.readFileToString(formattedScenarioName + ".txt", TARGET_FOLDER_PATH);
        azureAttachmentModel.setStream(screenshotContent);
        //Setting the name of attachment in azure to scenario name (removes space and replaces with 'underscore')
        String fileNameForAzurePayload = azureTestCaseModel.getTestCaseName().replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
        azureAttachmentModel.setFileName(fileNameForAzurePayload + ".png");
        String payLoad = serializeModelClass(azureAttachmentModel);
        payLoad = new JsonWriter("fileName", payLoad).setValue(fileNameForAzurePayload + ".png").extract();
        logger.info("Successfully generated payload for " + azureTestCaseModel.getTestCaseName());
        return payLoad;
    }


    @Override
    public void uploadScreenShot() {
        try {
            logger.info("Build Uri for the current build is " + buildUri);
            int runId = getLatestTestRunId(buildUri);
            List<AzureTestCaseModel> testCaseModelList = getFailedTestCasesFromAzureTestRun(runId);
            postScreenshotToAzure(runId, testCaseModelList);
        } catch (AzureOperationsException ex) {
            logger.info("Failed to upload screenshot to azure devops");
            logger.info(ex.getMessage());
        }
    }

    /**
     * Serialize the azure attachment model class to json string. This json string is used as the payload in the
     * post attachment api call
     *
     * @param azureAttachmentModel The object of azure attachment model class
     * @return serialized json string
     */
    private String serializeModelClass(AzureAttachmentModel azureAttachmentModel) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(azureAttachmentModel);
        } catch (JsonProcessingException ex) {
            logger.info("Failed to serialize the attachment model class");
            logger.info(ex.getMessage());
            throw new AzureOperationsException("Failed to serialize the attachment model class for file " + azureAttachmentModel.getFileName());
        }
    }

}
