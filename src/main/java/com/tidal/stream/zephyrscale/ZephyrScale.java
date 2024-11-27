package com.tidal.stream.zephyrscale;

public class ZephyrScale {

    public static class TestResults{
        public ZephyrScaleCukes updateCucumberResults(){
            return new ZephyrScaleCukes();
        }

        public ZephyrScaleTestNG updateTestNGResults(){
            return new ZephyrScaleTestNG();
        }

        public ZephyrScaleCSVFeeder updateTestResultsWithCSVFeeder(){
            return new ZephyrScaleCSVFeeder();
        }
    }
}
