package com.saralein.server.filesystem;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class File implements Resource {
    private final MessageDigest messageDigest;

    public File(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    @Override
    public boolean exists(Path file) {
        return Files.exists(file) && !Files.isDirectory(file);
    }

    public String mimeType(String file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    public int length(Path file) throws IOException {
        return (int) Files.size(file);
    }

    public String computeHash(byte[] body) {
        byte[] hashedBody = messageDigest.digest(body);
        return DatatypeConverter.printHexBinary(hashedBody).toLowerCase();
    }
}
