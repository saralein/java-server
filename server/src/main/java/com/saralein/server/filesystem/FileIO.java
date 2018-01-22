package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Path;

public interface FileIO {
    byte [] readPartialFile(Path file, int start, int end) throws IOException;
}
