package com.tidal.stream.filehandler;


import com.tidal.stream.exceptions.RuntimeTestException;

import java.io.*;
import java.nio.file.Path;

public class FileReader {

    private FileReader() {
    }

    public static synchronized String readFileToString(String fileName, Path baseFolderPath) {
        FileInputStream fileInputStream = (FileInputStream) readFileToStream(Finder.findFile(fileName, baseFolderPath));
        return readStreamToString(fileInputStream);
    }

    public static synchronized String readFileToString(String fileName) {
        FileInputStream fileInputStream = (FileInputStream) readFileToStream(Finder.findFile(fileName));
        return readStreamToString(fileInputStream);
    }

    public static synchronized InputStream readFileToStream(File file) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeTestException(e.getMessage());
        }
        return fileInputStream;
    }

    public static synchronized String readStreamToString(InputStream inputStream) {
        byte[] byteArray = new byte[0];

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Copy.bufferStreamCopy(inputStream, outputStream);
            byteArray = outputStream.toByteArray();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(byteArray);
    }

    public static InputStream getFileContentsAsStream(String resource) {
        return ClassLoader.getSystemResourceAsStream(resource);
    }
}
