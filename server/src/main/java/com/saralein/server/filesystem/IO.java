package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;

public interface IO {
    byte[] readAllBytes(Path path) throws IOException;

    byte[] readByteRange(Path file, int start, int end) throws IOException;

    void write(Path path, String content) throws IOException;
}
