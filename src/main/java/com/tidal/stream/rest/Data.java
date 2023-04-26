package com.tidal.stream.rest;

public enum Data implements DataEnum{
    PAYLOAD("payload"), //Payload data
    UID("uuid"), //Unique identifier
    EID("eid"),  //Error identifier
    AID("applicationid"), //Application identifier
    RESPONSE("response"), //Response payload
    TIME("time"); //Timestamp


    String value;

    Data(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}

