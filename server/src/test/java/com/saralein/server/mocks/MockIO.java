package com.saralein.server.mocks;

import com.saralein.server.filesystem.IO;
import java.io.IOException;
import java.nio.file.Path;

public class MockIO implements IO {
    private final byte[] response;
    private Path readCalledWithPath;
    private Integer readCalledWithStart;
    private Integer readCalledWithEnd;

    public MockIO(byte[] response) {
        this.response = response;
        this.readCalledWithPath = null;
        this.readCalledWithStart = null;
        this.readCalledWithEnd = null;
    }

    @Override
    public byte[] partialRead(Path path, int start, int end) throws IOException {
        readCalledWithPath = path;
        readCalledWithStart = start;
        readCalledWithEnd = end;
        return response;
    }

    public boolean readCalledWithPath(Path path) {
        return path.equals(readCalledWithPath);
    }

    public boolean readCalledWithStart(int start) {
        return start == readCalledWithStart;
    }

    public boolean readCalledWithEnd(int end) {
        return end == readCalledWithEnd;
    }
}
