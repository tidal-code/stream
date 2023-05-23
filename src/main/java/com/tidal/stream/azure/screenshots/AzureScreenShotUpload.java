package com.tidal.stream.azure.screenshots;

import com.tidal.utils.utils.CheckString;
import org.apache.log4j.Logger;


public class AzureScreenShotUpload {


    public static final Logger logger = Logger.getLogger(AzureScreenShotUpload.class);

    /**
     * Main method to be called in the exec plugin to upload screenshots.
     * It should always be - public static void main(String... args).
     * @param args 0 or more default args to be passed.
     */
    public static void main(String... args) {
        //Read Current BuildUri and AzureToken
        String currentBuildUri = System.getProperty("adoBuildUri");
        String azureToken = System.getProperty("adoToken");

        if (CheckString.isNotNullOrEmpty(currentBuildUri) && CheckString.isNotNullOrEmpty(azureToken)) {
            AzureSSOperations azureSSOperations = new AzureScreenshotOperations(currentBuildUri, azureToken);
            azureSSOperations.uploadScreenShot();
        } else {
            logger.info("Exiting the job as the build id and the token is not supplied..");
        }
    }
}
