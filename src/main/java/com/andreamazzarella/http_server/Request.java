package com.andreamazzarella.http_server;

class Request {

    private static final int INDEX_OF_REQUEST_LINE = 0;
    private static final int INDEX_OF_REQUEST_URI = 1;
    private static final String CARRIAGE_RETURN_LINE_FEED = "\n";
    private static final String SPACE = " ";

    private final String requestMessage;
    private final String[] tokenisedRequestMessage;
    private final String requestLine;

    Request(DataExchange socketConnection) {
        this.requestMessage = socketConnection.readLine();
        this.tokenisedRequestMessage = tokenise(requestMessage, CARRIAGE_RETURN_LINE_FEED);
        this.requestLine = tokenisedRequestMessage[INDEX_OF_REQUEST_LINE];
    }

    private String[] tokenise(String joinedTokens, String separator) {
        return joinedTokens.split(separator);
    }

    @Override
    public String toString() {
        return requestMessage;
    }

    String extractRequestURI() {
        String[] tokenisedRequestLine = tokenise(requestLine, SPACE);
        return tokenisedRequestLine[INDEX_OF_REQUEST_URI];
    }

}
