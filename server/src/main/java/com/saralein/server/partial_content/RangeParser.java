package com.saralein.server.partial_content;

public class RangeParser {
    public Range parse(String range, int fileLength) {
        String modifiedRange = removeRangeUnit(range);
        int hyphenIndex = modifiedRange.lastIndexOf("-");

        if (hyphenIndex == 0) {
            return parseRangeFromEnd(modifiedRange, fileLength);
        }

        if (hyphenIndex == modifiedRange.length() - 1) {
            return parseRangeFromStart(modifiedRange, fileLength);
        }

        return parseFullRange(modifiedRange);
    }

    private String removeRangeUnit(String range) {
        return range.replace("bytes=", "");
    }

    private Range parseRangeFromEnd(String range, int fileLength) {
        int value = Integer.parseInt(range.substring(1));
        return new Range(fileLength - value, fileLength - 1);
    }

    private Range parseRangeFromStart(String range, int fileLength) {
        int value = Integer.parseInt(range.substring(0, range.length() - 1));
        return new Range(value, fileLength - 1);
    }

    private Range parseFullRange(String range) {
        String[] splitRange = range.split("-");
        return new Range(Integer.parseInt(splitRange[0]), Integer.parseInt(splitRange[1]));
    }
}
