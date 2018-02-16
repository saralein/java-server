package com.saralein.server.mocks;

import com.saralein.server.filesystem.IO;
import com.saralein.server.range.Range;
import java.io.IOException;
import java.nio.file.Path;

public class MockIO implements IO {
    private final byte[] response;
    private Path readPath;
    private Integer readStart;
    private Integer readEnd;
    private Path writePath;
    private String writeContent;

    public MockIO(byte[] response) {
        this.response = response;
        this.readPath = null;
        this.readStart = null;
        this.readEnd = null;
        this.writePath = null;
        this.writeContent = null;
    }

    @Override
    public byte[] readAllBytes(Path path) throws IOException {
        readPath = path;
        return response;
    }

    @Override
    public byte[] readByteRange(Path file, Range range) throws IOException {
        readPath = file;
        readStart = range.getStart();
        readEnd = range.getEnd();
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

    public boolean readCalledWithStart(int start) {
        return readStart == start;
    }

    public boolean readCalledWithEnd(int end) {
        return readEnd == end;
    }

    public boolean writeCalledWith(Path path, String content) {
        return path.equals(writePath)
                && content.equals(writeContent);
    }
}
