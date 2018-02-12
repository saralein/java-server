package com.saralein.server.filesystem;

import javax.xml.bind.DatatypeConverter;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class File {
    private final MessageDigest messageDigest;

    public File(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    public boolean exists(Path file) {
        return Files.exists(file) && !Files.isDirectory(file);
    }

    public String mimeType(String file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    public String computeHash(byte[] body) {
        byte[] hashedBody = messageDigest.digest(body);
        return DatatypeConverter.printHexBinary(hashedBody).toLowerCase();
    }
}
