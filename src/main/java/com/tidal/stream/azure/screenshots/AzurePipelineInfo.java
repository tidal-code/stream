package com.tidal.stream.azure.screenshots;

import com.tidal.utils.utils.CheckString;

public class AzurePipelineInfo {
    private String azureDevopsOrgName;
    private String azureDevopsProjectName;

    private String azureToken;

    private String azureBuildUri;

    public String getAzureDevopsOrgName() {
        return azureDevopsOrgName;
    }

    public void setAzureDevopsOrgName(String azureDevopsOrgName) {
        this.azureDevopsOrgName = azureDevopsOrgName;
    }

    public String getAzureDevopsProjectName() {
        return azureDevopsProjectName;
    }

    public void setAzureDevopsProjectName(String azureDevopsProjectName) {
        this.azureDevopsProjectName = azureDevopsProjectName;
    }

    public String getAzureToken() {
        return azureToken;
    }

    public void setAzureToken(String azureToken) {
        this.azureToken = azureToken;
    }

    public String getAzureBuildUri() {
        return azureBuildUri;
    }

    public void setAzureBuildUri(String azureBuildUri) {
        this.azureBuildUri = azureBuildUri;
    }

    public boolean isValidBuildInfoSupplied(){
        return CheckString.isNotNullOrEmpty(this.azureBuildUri) && CheckString.isNotNullOrEmpty(this.azureToken) && CheckString.isNotNullOrEmpty(this.azureDevopsOrgName) && CheckString.isNotNullOrEmpty(this.azureDevopsProjectName);
    }
}
