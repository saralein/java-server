package com.saralein.server.filesystem;

import com.saralein.server.range.Range;
import java.io.IOException;
import java.nio.file.Path;

public interface IO {
    byte[] readAllBytes(Path path) throws IOException;

    byte[] readByteRange(Path file, Range range) throws IOException;

    void write(Path path, String content) throws IOException;
}
