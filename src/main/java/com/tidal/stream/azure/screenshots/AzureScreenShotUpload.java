package com.tidal.stream.azure.screenshots;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AzureScreenShotUpload {


    public static final Logger logger = LoggerFactory.getLogger(AzureScreenShotUpload.class);

    /**
     * Main method to be called in the exec plugin to upload screenshots.
     * It should always be - public static void main(String... args).
     * @param args 0 or more default args to be passed.
     */
    public static void main(String... args) {
        //Read Current Build Info and AzureToken
        AzurePipelineInfo azurePipelineInfo=new AzurePipelineInfo();
        azurePipelineInfo.setAzureToken(System.getProperty("adoToken"));
        azurePipelineInfo.setAzureDevopsOrgName(System.getProperty("adoOrgName"));
        azurePipelineInfo.setAzureDevopsProjectName(System.getProperty("adoProjectName"));
        azurePipelineInfo.setAzureBuildUri(System.getProperty("adoBuildUri"));
        if (azurePipelineInfo.isValidBuildInfoSupplied() ) {
            AzureSSOperations azureSSOperations = new AzureScreenshotOperations(azurePipelineInfo);
            azureSSOperations.uploadScreenShot();
        } else {
            logger.info("Exiting the job as the build id (adoBuildUri), project name (adoProjectName), org name (adoOrgName)  and the token(adoToken) is not supplied..");
        }
    }
}
