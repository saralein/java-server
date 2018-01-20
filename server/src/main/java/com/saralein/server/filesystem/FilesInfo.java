package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FilesInfo {
    String determineMimeType(String file);
    int getFileLength(Path file) throws IOException;
    List<String> listFileNames(Path directory) throws IOException;
}
