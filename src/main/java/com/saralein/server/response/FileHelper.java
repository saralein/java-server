package com.saralein.server.response;

import java.io.File;
import java.util.List;

public interface FileHelper {
    String createRelativePath(String name);
    String determineMimeType(String file);
    List<String> listFileNames(File directory);
}