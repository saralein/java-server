package com.saralein.server.mocks;

import com.saralein.server.filesystem.IO;
import java.io.IOException;
import java.nio.file.Path;

public class MockIO implements IO {
    private final byte[] response;
    private Path readPath;
    private Path writePath;
    private String writeContent;

    public MockIO(byte[] response) {
        this.response = response;
        this.readPath = null;
        this.writePath = null;
        this.writeContent = null;
    }

    @Override
    public byte[] readAllBytes(Path path) throws IOException {
        readPath = path;
        return response;
    }

    @Override
    public void write(Path path, String content) throws IOException {
        writePath = path;
        writeContent = content;
    }

    public boolean readCalledWithPath(Path path) {
        return path.equals(readPath);
    }

    public boolean writeCalledWith(Path path, String content) {
        return path.equals(writePath)
                && content.equals(writeContent);
    }
}
