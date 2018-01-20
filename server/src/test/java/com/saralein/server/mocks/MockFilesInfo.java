package com.saralein.server.mocks;

import com.saralein.server.filesystem.FilesInfo;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MockFilesInfo implements FilesInfo {
    private static final int FILE_LENGTH = 22;
    private static final String MIME_TYPE = "image/jpeg";
    private static final String PDF = "cake.pdf";
    private static final String JPG = "cheetara.jpg";
    private static final String GIF = "marshmallow.gif";
    private static final String TXT = "recipe.txt";
    private static final String SUBDIRECTORY = "sloths/";
    private static final String EXCEPTION = "readFullFile failed";

    private boolean throwError = false;
    private String mimeType;

    public MockFilesInfo() {
        this.mimeType = MIME_TYPE;
    }

    @Override
    public String determineMimeType(String file) {
        return mimeType;
    }

    @Override
    public int getFileLength(Path file) throws IOException {
        throwErrorIfSelected();
        return FILE_LENGTH;
    }

    @Override
    public List<String> listFileNames(Path directory) throws IOException {
        throwErrorIfSelected();

        return new ArrayList<String>(){{
            add(PDF);
            add(JPG);
            add(GIF);
            add(TXT);
            add(SUBDIRECTORY);
        }};
    }

    public void setToThrowError() {
        this.throwError = true;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    private void throwErrorIfSelected() throws IOException {
        if (throwError) {
            throw new IOException(EXCEPTION);
        }
    }
}
