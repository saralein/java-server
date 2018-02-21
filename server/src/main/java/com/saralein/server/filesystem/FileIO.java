package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIO implements IO {
    @Override
    public byte[] readAllBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    @Override
    public void write(Path path, String content) throws IOException {
        Files.write(path, content.getBytes());
    }
}
