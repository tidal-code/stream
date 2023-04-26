package com.tidal.stream.filehandler;

import com.tidal.stream.exceptions.RuntimeTestException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileOutWriter {

    private FileOutWriter() {
    }

    public static boolean createDirectory(String path){
        File file = new File(path);
        if(!file.exists()) return file.mkdir(); else return false;
    }

    public static boolean createTargetFolderDirectory(String folderName){
        Path path = Paths.get(FilePaths.TARGET_FOLDER_PATH.getPath(), folderName);
        return createDirectory(path.toString());
    }

    public static void deleteDirectory(String folderPath){
        Path path = Paths.get(folderPath);
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeTestException(e.getMessage());
        }
    }


    public static void deleteTargetFolderDirectory(String folderName){
        Path path = Paths.get(FilePaths.TARGET_FOLDER_PATH.getPath(), folderName);
        deleteDirectory(path.toString());
    }

    public static void writeFileTo(String content, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileToTargetFolder(String content, String folderName){
        Path path = Paths.get(FilePaths.TARGET_FOLDER_PATH.getPath(), folderName);
        writeFileTo(content, path.toString());
    }
}
