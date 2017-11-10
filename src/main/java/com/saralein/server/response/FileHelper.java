package com.saralein.server.response;

import java.io.File;
import java.util.List;

public interface FileHelper {
    String getRelativePath(String name);
    String getMimeType(String file);
    List<String> getFilenames(File directory);
}