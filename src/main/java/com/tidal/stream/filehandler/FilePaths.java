package com.tidal.stream.filehandler;

import java.nio.file.FileSystems;

public enum FilePaths {

    RESOURCE_FOLDER_PATH("src/test/resources/"),
    ERROR_OUTPUT_FOLDER("src/test/resources/error/"),
    TARGET_FOLDER_PATH("target/");

    private String path;

    FilePaths(String path) {
        this.path = path;
    }

    public static String getAbsoluteFromRelativePath(String path) {
        return FileSystems.getDefault().getPath(path).normalize().toAbsolutePath().toString();
    }

    public static String getTransformedUrlPath(FilePaths filePaths) {
        String path = getAbsoluteFromRelativePath(filePaths.getPath());
        path = "file://" + path;
        return path;
    }

    public String getPath() {
        return path;
    }

}
