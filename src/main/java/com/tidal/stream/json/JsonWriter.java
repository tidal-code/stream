package com.tidal.stream.json;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class JsonWriter {
    private String jsonString;
    private final String jsonPath;
    private final String jsonDocument;

    public JsonWriter(String jsonPath, String jsonDocument) {
        this.jsonPath = jsonPath;
        this.jsonDocument = jsonDocument;
    }

    public JsonWriter setValue(Object valueToUpdate){
        DocumentContext documentContext = JsonPath.parse(jsonDocument);
        documentContext = documentContext.set(jsonPath, valueToUpdate);
        jsonString =  documentContext.jsonString();
        return this;
    }

    public JsonWriter addToArray(String valueToUpdate){
        DocumentContext documentContext = JsonPath.parse(jsonDocument);
        documentContext = documentContext.add(jsonPath, valueToUpdate);
        jsonString =  documentContext.jsonString();
        return this;
    }

    public JsonWriter addValue(String key, String valueToUpdate){
        DocumentContext documentContext = JsonPath.parse(jsonDocument);
        documentContext = documentContext.put(jsonPath, key, valueToUpdate);
        jsonString =  documentContext.jsonString();
        return this;
    }

    public String extract(){
        return jsonString;
    }
}
