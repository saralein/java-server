package com.saralein.server.controller;

import com.saralein.server.FileHelper;
import com.saralein.server.filesystem.FileIO;
import com.saralein.server.partial_content.Range;
import com.saralein.server.partial_content.RangeParser;
import com.saralein.server.partial_content.RangeValidator;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

import java.io.IOException;
import java.nio.file.Path;

public class PartialContentController implements Controller {
    private final FileHelper fileHelper;
    private final FileIO fileIO;
    private final RangeValidator validator;
    private final RangeParser parser;
    private final ErrorController errorController;

    public PartialContentController(
            FileHelper fileHelper, FileIO fileIO,
            RangeValidator rangeValidator, RangeParser rangeParser, ErrorController errorController) {
        this.fileHelper = fileHelper;
        this.fileIO = fileIO;
        this.validator = rangeValidator;
        this.parser = rangeParser;
        this.errorController = errorController;
    }

    @Override
    public Response respond(Request request) {
        try {
            return getResponse(request);
        } catch (IOException e) {
            return errorController.updateStatus(500)
                    .respond(request);
        }
    }

    private Response getResponse(Request request) throws IOException {
        Path file = fileHelper.createAbsolutePath(request.getUri());
        int fileLength = fileHelper.getFileLength(file);
        String rawRange = request.getHeader("Range");

        if (!validator.matchesValidRangeFormat(rawRange)) {
            return getInvalidRangeResponse(fileLength);
        }

        Range range = parser.parse(rawRange, fileLength);

        return getResponseByNumericValidity(request, file, range, fileLength);
    }

    private Response getResponseByNumericValidity(Request request, Path file, Range range, int fileLength) throws IOException {
        if (validator.isValidRange(range.getStart(), range.getEnd(), fileLength)) {
            return getValidRangeResponse(request, file, range, fileLength);
        }

        return getInvalidRangeResponse(fileLength);
    }

    private Response getInvalidRangeResponse(int fileLength) {
        return new Response.Builder()
                .status(416)
                .addHeader("Content-Range", "*/" + fileLength)
                .build();
    }

    private Response getValidRangeResponse(Request request, Path file, Range range, int fileLength) throws IOException {
        int start = range.getStart();
        int end = range.getEnd();

        return new Response.Builder()
                .status(206)
                .addHeader("Content-Type", fileHelper.determineMimeType(request.getUri()))
                .addHeader("Content-Range", formatContentRange(start, end, fileLength))
                .body(fileIO.readPartialFile(file, start, end))
                .build();
    }

    private String formatContentRange(int start, int end, int fileLength) {
        return String.format("bytes %d-%d/%d", start, end, fileLength);
    }
}