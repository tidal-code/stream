package com.tidal.stream.zephyrscale;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tidal.utils.utils.CheckString.isNotNullOrEmpty;

public class ZephyrScaleCukes extends ZephyrScaleTest {
    /*
    Pattern matcher is to be used for identifying valid tags.
    The issues will be generated using the project key as the start
     */
    String patternMatcher = "@" + readProperty.apply("projectKey");

    Function<Collection<String>, Collection<String>> filteredCollection = l -> l.stream()
            .filter(e -> isNotNullOrEmpty(e) && e.startsWith(patternMatcher))
            .map(e -> e.replace("@", ""))
            .collect(Collectors.toList());

    /**
     *
     * @param scenarioTags For handling the cucumber scenario tags
     * @return A self reference
     */
    public ZephyrScaleTest testTagProcessor(Collection<String> scenarioTags){
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

}
