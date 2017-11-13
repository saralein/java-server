package com.saralein.server.response;

import static com.saralein.server.Constants.STATUS_CODES;
import com.saralein.server.request.Request;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileResponse implements Response {
    private final Request request;
    private final File resource;
    private final FileHelper fileHelper;

    public FileResponse(Request request, File resource, FileHelper fileHelper) {
        this.request = request;
        this.resource = resource;
        this.fileHelper = fileHelper;
    }

    public byte[] createResponse() {
        byte[] headers = createHeader().toString().getBytes();
        byte[] body = createBody();

        return combineResponseParts(headers, body);
    }

    private byte[] combineResponseParts(byte[] headers, byte[] body) {
        byte[] combined = new byte[headers.length + body.length];

        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < headers.length ? headers[i] : body[i - headers.length];
        }

        return combined;
    }

    private String createHeader() {
        String mimeType = fileHelper.getMimeType(request.getUri());

        return new Header(STATUS_CODES.get(200), mimeType).getContent();
    }

    private byte[] createBody() {
        byte[] fileByte = null;

        try {
            fileByte = Files.readAllBytes(resource.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileByte;
    }
}
