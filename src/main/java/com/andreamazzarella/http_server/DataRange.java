package com.andreamazzarella.http_server;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataRange {
    private final Optional<Integer> declaredDataStart;
    private final Optional<Integer> declaredDataEnd;

    private DataRange(Optional<Integer> dataStart, Optional<Integer> dataEnd) {
        this.declaredDataStart = dataStart;
        this.declaredDataEnd = dataEnd;
    }

    public DataRange() {
        this.declaredDataStart = Optional.empty();
        this.declaredDataEnd = Optional.empty();
    }

    DataRange(int declaredStart, int declaredEnd) {
        this.declaredDataStart = Optional.of(declaredStart);
        this.declaredDataEnd = Optional.of(declaredEnd);
    }

    public int getStart(int totalDataLength) {
        if (declaredDataStart.isPresent()) {
            return declaredDataStart.get();
        } else {
            return declaredDataEnd.map(declaredEnd -> totalDataLength - declaredEnd).orElse(0);
        }
    }

    public int getEnd(int totalDataLength) {
        if (declaredDataStart.isPresent()) {
            return declaredDataEnd.orElse(totalDataLength - 1);
        } else {
            return totalDataLength - 1;
        }
    }

    public static DataRange parseFromString(Header dataRangeHeader) {
        Optional<Integer> dataStart = parseDataStart(dataRangeHeader.getValue());
        Optional<Integer> dataEnd = parseDataEnd(dataRangeHeader.getValue());
        return new DataRange(dataStart, dataEnd);
    }

    private static Optional<Integer> parseDataStart(String dataRange) {
        Pattern pattern = Pattern.compile(".*=(?<startByte>\\d+)-.*");
        Matcher matcher = pattern.matcher(dataRange);
        if (matcher.matches()) {
            int startPosition = Integer.parseInt(matcher.group("startByte"));
            return Optional.of(startPosition);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> parseDataEnd(String dataRange) {
        Pattern pattern = Pattern.compile(".*-(?<endByte>\\d+).*");
        Matcher matcher = pattern.matcher(dataRange);
        if (matcher.matches()) {
            int endPosition = Integer.parseInt(matcher.group("endByte"));
            return Optional.of(endPosition);
        } else {
            return Optional.empty();
        }
    }
}
