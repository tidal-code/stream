package com.tidal.stream.zephyrscale;

import com.tidal.utils.csv.CsvData;
import com.tidal.utils.propertieshandler.PropertiesFinder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

public abstract class ZephyrScaleTest {

    Logger logger = LoggerFactory.getLogger(ZephyrScaleTest.class);

    public static List<String> tags = new LinkedList<>();
    String testTag;
    String testStatus;
    Function<String, String> readProperty = PropertiesFinder::getProperty;
    /*
    Pattern matcher is to be used for identifying valid tags.
    The issues will be generated using the project key as the start
     */
    String patternMatcher;


    public ZephyrScaleTest testStatus(boolean status) {
        if (status) {
            testStatus = "Pass";
        } else {
            testStatus = "Fail";
        }
        return this;
    }

    public static String getJiraIdFromCSV() {
        CsvData csvData = new CsvData();
        csvData.setCSVFolderAsDataFilePath();
        return csvData.readDataFrom("TestLinkData", "Key");
    }


    public void publish() {
        Map<String, String> payLoad = new HashMap<>();

        if (null != testTag) {
            payLoad.put("projectKey", readProperty.apply("projectKey"));
            payLoad.put("testCaseKey", testTag);
            payLoad.put("testCycleKey", readProperty.apply("testCycleKey"));
            payLoad.put("statusName", testStatus);

            String zephyrPublishResult = new ZephyrScalePublish().toZephyrScale(new JSONObject(payLoad).toString());
            logger.info("Result uploaded to Zephyr Scale");
            logger.info(zephyrPublishResult);

        } else {
            logger.info("No suitable Test Tag Found: Zephyr Scale Result is not published.");
        }
    }

}
