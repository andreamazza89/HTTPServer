package com.andreamazzarella.http_server.utilities;

import com.andreamazzarella.http_server.utilities.DataRange;
import com.andreamazzarella.http_server.request_response.Header;
import org.junit.Test;

import static com.andreamazzarella.http_server.request_response.Header.RANGE_HEADER_NAME;
import static org.junit.Assert.assertEquals;

public class DataRangeShould {

    @Test
    public void defaultToFullRange() {
        DataRange dataRange = new DataRange();
        int dataLength = 42;

        assertEquals(0, dataRange.getStart(dataLength));
        assertEquals(41, dataRange.getEnd(dataLength));
    }

    @Test
    public void parseADataRangeFromARangeHeaderWithFullRangeExampleOne() {
        Header dataRangeHeader = new Header(RANGE_HEADER_NAME, "byte=0-4");
        DataRange dataRange = DataRange.parseFromString(dataRangeHeader);
        int dataLength = 42;

        assertEquals(0, dataRange.getStart(dataLength));
        assertEquals(4, dataRange.getEnd(dataLength));
    }

    @Test
    public void parseADataRangeFromARangeHeaderWithFullRangeExampleTwo() {
        Header dataRangeHeader = new Header(RANGE_HEADER_NAME, "byte=1-4");
        DataRange dataRange = DataRange.parseFromString(dataRangeHeader);
        int dataLength = 42;

        assertEquals(1, dataRange.getStart(dataLength));
        assertEquals(4, dataRange.getEnd(dataLength));
    }
    @Test
    public void parseADataRangeFromARangeHeaderWithMissingStart() {
        Header dataRangeHeader = new Header(RANGE_HEADER_NAME, "byte=-6");
        DataRange dataRange = DataRange.parseFromString(dataRangeHeader);
        int dataLength = 77;

        assertEquals(71, dataRange.getStart(dataLength));
        assertEquals(76, dataRange.getEnd(dataLength));
    }

    @Test
    public void parseADataRangeFromARangeHeaderWithMissingEnd() {
        Header dataRangeHeader = new Header(RANGE_HEADER_NAME, "byte=4-");
        DataRange dataRange = DataRange.parseFromString(dataRangeHeader);
        int dataLength = 77;

        assertEquals(4, dataRange.getStart(dataLength));
        assertEquals(76, dataRange.getEnd(dataLength));
    }
}
