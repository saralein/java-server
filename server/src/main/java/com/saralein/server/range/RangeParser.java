package com.saralein.server.range;

public class RangeParser {
    public Range parse(String range, int fileLength) throws Exception {
        if (!matchesValidRangeFormat(range)) {
            throw new Exception("Invalid range.");
        }

        Range parsedRange = parseRange(range, fileLength);

        if (!isValidRange(parsedRange, fileLength)) {
            throw new Exception("Invalid range.");
        }

        return parsedRange;
    }

    private Range parseRange(String range, int fileLength) {
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

    private boolean isValidRange(Range range, int fileLength) {
        int start = range.getStart();
        int end = range.getEnd();
        return (start >= 0) && (start < end) && (end < fileLength);
    }

    private boolean matchesValidRangeFormat(String range) {
        return matchesFullRangeFormat(range)
                || matchesFromStartFormat(range)
                || matchesFromEndFormat(range);
    }

    private boolean matchesFullRangeFormat(String range) {
        return range.matches("bytes=\\d+-\\d+");
    }

    private boolean matchesFromStartFormat(String range) {
        return range.matches("bytes=\\d+-");
    }

    private boolean matchesFromEndFormat(String range) {
        return range.matches("bytes=-\\d+");
    }
}
