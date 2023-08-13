package com.tidal.stream.zephyr;

import com.tidal.stream.zephyrscale.ZephyrScalePublish;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PublishTest {

    @Test
    public void publishResult(){
        Map<String, String> data = new HashMap<>();
        data.put("projectKey", "JD");
        data.put("testCaseKey", "JD-T1");
        data.put("testCycleKey", "JD-R1");
        data.put("statusName", "Fail");
        String zephyrScaleResult = new ZephyrScalePublish().toZephyrScale(new JSONObject(data).toString());
        System.out.println(zephyrScaleResult);
    }
}
