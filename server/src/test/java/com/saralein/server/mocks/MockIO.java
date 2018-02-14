package com.saralein.server.mocks;

import com.saralein.server.filesystem.IO;
import java.io.IOException;
import java.nio.file.Path;

public class MockIO implements IO {
    private final byte[] response;
    private Path readCalledWith;
    private Path writeCalledWithPath;
    private String writeCalledWithContent;

    public MockIO(byte[] response) {
        this.response = response;
        this.readCalledWith = null;
        this.writeCalledWithPath = null;
        this.writeCalledWithContent = null;
    }

    @Override
    public byte[] read(Path path) throws IOException {
        readCalledWith = path;
        return response;
    }

    @Override
    public void write(Path path, String content) throws IOException {
        writeCalledWithPath = path;
        writeCalledWithContent = content;
    }

    public boolean readCalledWith(Path path) {
        return path.equals(readCalledWith);
    }

    public boolean writeCalledWith(Path path, String content) {
        return path.equals(writeCalledWithPath)
                && content.equals(writeCalledWithContent);
    }
}
