package com.saralein.server.response;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileHelper {
    String createAbsolutePath(String name);
    String createRelativeFilePath(String name, Path directory);
    String determineMimeType(String file);
    List<String> listFileNames(Path directory) throws IOException;
}