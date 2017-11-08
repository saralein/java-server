package com.saralein.server.response;

public class DirectoryResponse implements Response {
    private final String contentType = "text/html";

    public byte[] createResponse() {
        StringBuilder response = new StringBuilder();
        response.append(createHeader());
        response.append(createBody());

        return response.toString().getBytes();
    }

    private String createHeader() {
        return new Header("200 OK", contentType).getContent();
    }

    private String createBody() {
        FileHelper fileHelper = new FileHelper("public");
        StringBuilder filesHTML = new StringBuilder();

        for (String filename: fileHelper.getFilenames()) {
            String filepath = fileHelper.getRelativePath(filename);
            filesHTML.append(getHTML(filename, filepath));
        }

        return filesHTML.toString();
    }

    private String getHTML(String filename, String filepath) {
        return "<li><a href=" + filepath + ">" + filename + "</a></li>";
    }
}
