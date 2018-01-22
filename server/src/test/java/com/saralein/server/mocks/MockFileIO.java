package com.saralein.server.mocks;

import com.saralein.server.filesystem.FileIO;

import java.io.IOException;
import java.nio.file.Path;

public class MockFileIO implements FileIO {
    @Override
    public byte[] readPartialFile(Path file, int start, int end) throws IOException {
        return "IO called".getBytes();
    }
}
