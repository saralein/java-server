package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileIO implements IO {
    @Override
    public byte[] partialRead(Path file, int start, int end) throws IOException {
        FileChannel fileChannel = FileChannel.open(file, StandardOpenOption.READ);
        fileChannel.position(start);
        ByteBuffer buffer = ByteBuffer.allocate(end + 1 - start);

        while (buffer.hasRemaining()) {
            fileChannel.read(buffer);
        }

        return buffer.array();
    }
}
