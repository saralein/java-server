package com.saralein.server.partial_content;

public class RangeValidator {
    public boolean isValidRange(int start, int end, int fileLength) {
        return (start >= 0) && (start < end) && (end < fileLength);
    }

    public boolean matchesValidRangeFormat(String range) {
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
