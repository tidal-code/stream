package com.tidal.stream.azure;

import com.tidal.utils.propertieshandler.PropertiesFinder;

import java.util.Base64;
import java.util.function.Supplier;

public class AppConstants {
    public static final String PROJECT_TEST_API_ENDPOINT = "https://dev.azure.com/SealordDev/Mahi%20Tahi/_apis/test/";
    public final static Supplier<String> AZURE_AUTHORIZATION = AppConstants::getAzureAuth;

    public static String getAzureAuth(){
        return "Basic " + Base64.getEncoder().encodeToString( (":" + PropertiesFinder.getProperty("ado.auth")).getBytes());
    }
}
