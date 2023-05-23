package com.tidal.stream.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.Optional;

public class JsonReader {

    public static <T> T readValue(String path, String jsonString) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        return JsonPath.read(document, path);
    }

    public static <T> Optional<T> tryReadValue(String path, String jsonString) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        try {
            return Optional.of(JsonPath.read(document, path));
        } catch (PathNotFoundException e) {
            return Optional.empty();
        }
    }
}
