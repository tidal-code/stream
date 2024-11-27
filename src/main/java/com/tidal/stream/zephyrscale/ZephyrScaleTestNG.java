package com.tidal.stream.zephyrscale;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tidal.utils.utils.CheckString.isNotNullOrEmpty;

public class ZephyrScaleTestNG extends ZephyrScaleTest {
    /*
    Pattern matcher is to be used for identifying valid tags.
    The issues will be generated using the project key as the start
    */
    String patternMatcher = readProperty.apply("projectKey");

    Function<String, String> filteredCollection = l -> {
        if (isNotNullOrEmpty(l) && l.startsWith(patternMatcher)){
            return l;
        }
       return null;
    };


    public ZephyrScaleTest testTagProcessor(String scenarioTags) {
        testTag = filteredCollection.apply(scenarioTags);
        return this;
    }

}
