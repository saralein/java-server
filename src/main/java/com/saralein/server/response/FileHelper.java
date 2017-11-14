package com.saralein.server.response;

import java.io.File;
import java.util.List;

public interface FileHelper {
    String createAbsolutePath(String name);
    String createRelativeFilePath(String name, File directory);
    String determineMimeType(String file);
    List<String> listFileNames(File directory);
}