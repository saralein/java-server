package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;

public interface FileIO {
    byte[] readFullFile(Path file) throws IOException;
    byte[] readPartialFile(Path file, int start, int end) throws IOException;
    void appendFile(Path file, String append) throws IOException;
}
