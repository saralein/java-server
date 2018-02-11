package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;

public interface IO {
    byte[] read(Path path) throws IOException;

    void write(Path path, String content) throws IOException;
}
