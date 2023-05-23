package com.tidal.stream.json;


import com.jayway.jsonpath.JsonPath;

public class JsonBuilder {
    private String jsonPath;
    private String jsonFileContent;

    public JsonBuilder(String jsonFileContent){
        this.jsonFileContent = jsonFileContent;
    }

    public JsonBuilder(String jsonPath, String jsonFileContent) {
        this.jsonPath = jsonPath;
        this.jsonFileContent = jsonFileContent;
    }

    public String extract(){
        return jsonFileContent;
    }

    public JsonBuilder newPath(String newJsonPath){
        jsonPath = newJsonPath;
        return this;
    }

    public <T> JsonBuilder withNewValue(T newValue){
        if(newValue == null){
            return this;
        }

        jsonFileContent = JsonPath.parse(jsonFileContent).set(jsonPath, newValue).jsonString();
        return this;
    }
}
