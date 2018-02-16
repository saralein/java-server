package com.saralein.server.filesystem;

import java.nio.file.Path;

public interface Resource {
    boolean exists(Path resource);
}
