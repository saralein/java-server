package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.IO;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class PartialFileHandler implements Handler {
    private final FileHelper fileHelper;
    private final IO fileIO;

    public PartialFileHandler(FileHelper fileHelper, IO fileIO) {
        this.fileHelper = fileHelper;
        this.fileIO = fileIO;
    }

    @Override
    public Response handle(Request request) throws IOException {
        Map<String, Integer> range = request.getRange();
        Path file = fileHelper.createAbsolutePath(request.getUri());
        int fileLength = fileHelper.getFileLength(file);

        if (isEmptyRange(range)) {
            return getInvalidRangeResponse(fileLength);
        }

        int start = rangeStart(range, fileLength);
        int end = rangeEnd(range, fileLength);

        if (isValidRange(start, end, fileLength)) {
            return getPartialResponse(request, file, start, end, fileLength);
        }

        return getInvalidRangeResponse(fileLength);
    }

    private Response getPartialResponse(Request request, Path file, int start, int end, int fileLength) throws IOException {
        return new Response.Builder()
                .status(206)
                .addHeader("Content-Type", fileHelper.determineMimeType(request.getUri()))
                .addHeader("Content-Range", formatContentRange(start, end, fileLength))
                .body(fileIO.partialRead(file, start, end))
                .build();
    }

    private Response getInvalidRangeResponse(int fileLength) {
        return new ErrorResponse(416)
                .respond("Content-Range", "*/" + fileLength);
    }

    private int rangeStart(Map<String, Integer> range, int fileLength) {
        if (!rangeHasStart(range) && rangeHasEnd(range)) {
            return fileLength - getEnd(range);
        }

        return getStart(range);
    }

    private int rangeEnd(Map<String, Integer> range, int fileLength) {
        if (rangeHasStart(range) && rangeHasEnd(range)) {
            return getEnd(range);
        }

        return fileLength - 1;
    }

    private Integer getStart(Map<String, Integer> range) {
        return range.get("start");
    }

    private Integer getEnd(Map<String, Integer> range) {
        return range.get("end");
    }

    private boolean rangeHasStart(Map<String, Integer> range) {
        return getStart(range) != null;
    }

    private boolean rangeHasEnd(Map<String, Integer> range) {
        return getEnd(range) != null;
    }

    private boolean isEmptyRange(Map<String, Integer> range) {
        return range.isEmpty();
    }

    private boolean isValidRange(int start, int end, int fileLength) {
        return (start >= 0) && (start < end) && (end < fileLength);
    }

    private String formatContentRange(int start, int end, int fileLength) {
        return String.format("bytes %d-%d/%d", start, end, fileLength);
    }
}
