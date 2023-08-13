package com.tidal.stream.zephyrscale;

import com.tidal.utils.propertieshandler.PropertiesFinder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZephyrScaleCukes {

    Logger logger  = LoggerFactory.getLogger(ZephyrScaleCukes.class);

    public static List<String> tags = new ArrayList<>();
    String testTag;
    String testStatus;
    Function<String, String> readProperty = PropertiesFinder::getProperty;
    /*
    Pattern matcher is to be used for identifying valid tags.
    The issues will be generated using the project key as the start
     */
    String patternMatcher = "@" + PropertiesFinder.getProperty("projectKey");

    Function<Collection<String>, Collection<String>> filteredCollection = l -> l.stream()
            .filter(e -> e.startsWith(patternMatcher))
            .map(e -> e.replace("@", ""))
            .collect(Collectors.toList());

    public ZephyrScaleCukes testTagProcessor(Collection<String> scenarioTags){
        List<String> filteredList = (List<String>) filteredCollection.apply(scenarioTags);

        for (String item : filteredList) {
            if (!tags.contains(item)) {
                testTag = item;
                tags.add(item);
                break;
            }
        }
        return this;
    }

    public ZephyrScaleCukes testStatus(boolean status){
        if(status){
            testStatus = "Pass";
        } else {
            testStatus = "Fail";
        }
        return this;
    }

    public void publish(){
        Map<String, String> payLoad = new HashMap<>();

        if(null != testTag) {
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
