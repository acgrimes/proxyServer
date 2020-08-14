package com.dd.vbc.network;

public class NettyMessage {
    /**
     * This is the byte[] message from the server to a mobile client or the mobile client to the server. Each message
     * will allocate the 1st 4 bytes for a int that represents the ordinal number of the Request/Response enum.
     * The next 4 bytes represents a int that represents the length in bytes of the request/response object that
     * the next sequence of bytes convert to. The Request/Response enum represents the type of message. For each
     * type of message there is a unique object or object graph associated with it.
     */
    private static byte[] message;

    public static byte[] getMessage() {
        return message;
    }

    public static void setMessage(byte[] aMessage) {
        message = aMessage;
    }
}
