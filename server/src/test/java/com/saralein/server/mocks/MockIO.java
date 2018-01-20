package com.saralein.server.mocks;

import com.saralein.server.filesystem.FileIO;
import java.io.IOException;
import java.nio.file.Path;

public class MockIO implements FileIO {
    private static final String FULL_FILE_CALLED = "readFullFile called";
    private static final String PARTIAL_FILE_CALLED = "readPartialFile called";
    private static final String EXCEPTION = "Reading file failed";

    private boolean throwError = false;
    private String appendCalledWith = null;

    @Override
    public byte[] readFullFile(Path file) throws IOException {
        throwErrorIfSelected();
         return FULL_FILE_CALLED.getBytes();
    }

    @Override
    public byte[] readPartialFile(Path file, int start, int end) throws IOException {
        throwErrorIfSelected();
        String substring = PARTIAL_FILE_CALLED.substring(start, end + 1);
        return substring.getBytes();
    }

    @Override
    public void appendFile(Path file, String append) throws IOException {
        throwErrorIfSelected();
        appendCalledWith = append;
    }

    public String appendCalledWith() {
        return appendCalledWith;
    }

    public void setToThrowError() {
        this.throwError = true;
    }

    private void throwErrorIfSelected() throws IOException {
        if (throwError) {
            throw new IOException(EXCEPTION);
        }
    }
}
