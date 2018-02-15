package com.saralein.server.handler;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.filesystem.IO;
import com.saralein.server.range.Range;
import com.saralein.server.range.RangeParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;

public class PartialFileHandler implements Handler {
    private final File file;
    private final FilePath filePath;
    private final IO fileIO;
    private final RangeParser rangeParser;

    public PartialFileHandler(
            File file, FilePath filePath, IO fileIO, RangeParser rangeParser) {
        this.file = file;
        this.filePath = filePath;
        this.fileIO = fileIO;
        this.rangeParser = rangeParser;
    }

    @Override
    public Response handle(Request request) throws IOException {
        Path resource = filePath.absolute(request.getUri());
        int fileLength = file.length(resource);
        String rawRange = request.getHeader("Range");
        Range range;

        try {
            range = rangeParser.parse(rawRange, fileLength);
        } catch (Exception e) {
            return invalidRange(fileLength);
        }

        return getResponse(request, resource, range, fileLength);
    }

    private Response getResponse(Request request, Path resource, Range range, int fileLength) throws IOException {
        int start = range.getStart();
        int end = range.getEnd();

        return new Response.Builder()
                .status(206)
                .addHeader("Content-Type", file.mimeType(request.getUri()))
                .addHeader("Content-Range", formatContentRange(start, end, fileLength))
                .body(fileIO.readByteRange(resource, start, end))
                .build();
    }

    private Response invalidRange(int fileLength) {
        return new ErrorResponse(416).respond("Content-Range", "*/" + fileLength);
    }

    private String formatContentRange(int start, int end, int fileLength) {
        return String.format("bytes %d-%d/%d", start, end, fileLength);
    }
}
