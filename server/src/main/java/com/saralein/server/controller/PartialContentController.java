package com.saralein.server.controller;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;
import java.nio.file.*;

public class PartialContentController implements Controller {
    private final FileHelper fileHelper;
    private final FileIO fileIO;

    public PartialContentController(FileHelper fileHelper, FileIO fileIO) {
        this.fileHelper = fileHelper;
        this.fileIO = fileIO;
    }

    @Override
    public Response respond(Request request) {
        try {
            return determineRangeResponse(request);
        } catch (IOException e) {
            return new Response.Builder()
                    .status(500)
                    .build();
        }
    }

    private Response determineRangeResponse(Request request) throws IOException {
        Path file = fileHelper.createAbsolutePath(request.getUri());
        int fileLength = fileHelper.getFileLength(file);
        String range = request.getHeader("Range");

        if (!validRangeFormat(range)) {
            return buildInvalidRangeResponse(fileLength);
        }

        int[] parsedRange = parseRange(range, fileLength);
        int start = rangeStart(parsedRange);
        int end = rangeEnd(parsedRange);

        return requestInRange(fileLength, start, end)
                ? buildValidRangeResponse(request, fileLength, start, end)
                : buildInvalidRangeResponse(fileLength);
    }

    private Response buildInvalidRangeResponse(int fileLength) {
        return new Response.Builder()
                .status(416)
                .addHeader("Content-Range", "*/" + fileLength)
                .build();
    }

    private Response buildValidRangeResponse(Request request, int fileLength, int start, int end) throws IOException {
        Path file = fileHelper.createAbsolutePath(request.getUri());
        byte[] body = fileIO.readPartialFile(file, start, end);

        return new Response.Builder()
                .status(206)
                .addHeader("Content-Type", fileHelper.determineMimeType(request.getUri()))
                .addHeader("Content-Range", formatContentRange(start, end, fileLength))
                .body(body)
                .build();
    }

    private boolean requestInRange(int fileLength, int start, int end) {
        return (start >= 0) && (start < end) && (end < fileLength);
    }

    private int[] parseRange(String range, int fileLength) {
        if (specifiesRangeFromEnd(range)) {
            return parseRangeFromEnd(range, fileLength);
        }

        if (specifiesRangeFromStart(range)) {
            return parseRangeFromStart(range, fileLength);
        }

        return parseFullRange(range);
    }

    private boolean specifiesFullRange(String range) {
        return range.matches("bytes=\\d+-\\d+");
    }

    private boolean specifiesRangeFromStart(String range) {
        return range.matches("bytes=\\d+-");
    }

    private boolean specifiesRangeFromEnd(String range) {
        return range.matches("bytes=-\\d+");
    }

    private boolean validRangeFormat(String range) {
        return specifiesFullRange(range)
                || specifiesRangeFromStart(range)
                || specifiesRangeFromEnd(range);
    }

    private String removeRangeUnit(String range) {
        return range.replace("bytes=", "");
    }

    private int[] parseRangeFromEnd(String range, int fileLength) {
        String trimmedRange = removeRangeUnit(range);
        int value = Integer.parseInt(trimmedRange.substring(1));
        return new int[]{fileLength - value, fileLength - 1};
    }

    private int[] parseRangeFromStart(String range, int fileLength) {
        String trimmedRange = removeRangeUnit(range);
        int value = Integer.parseInt(trimmedRange.substring(0, trimmedRange.length() - 1));
        return new int[]{value, fileLength - 1};
    }

    private int[] parseFullRange(String range) {
        String trimmedRange = removeRangeUnit(range);
        String[] splitRange = trimmedRange.split("-");
        return new int[]{Integer.parseInt(splitRange[0]), Integer.parseInt(splitRange[1])};
    }

    private int rangeStart(int[] range) {
        return range[0];
    }

    private int rangeEnd(int[] range) {
        return range[1];
    }

    private String formatContentRange(int start, int end, int fileLength) {
        return String.format("bytes %d-%d/%d", start, end, fileLength);
    }
}
