package com.dd.vbc.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class SerialUtil {

    public static final byte[] serializeString(String str) {
        int length = str.length();
        byte[] lengthBytes = serializeInt(length);
        byte[] valueBytes = str.getBytes();
        return concatenateBytes(lengthBytes, valueBytes);
    }
    public static final String deserializeString(byte[] bytes) {
        int length = deserializeInt(Arrays.copyOfRange(bytes, 0, 4));
        return new String(Arrays.copyOfRange(bytes, 4, length+4));
    }

    public static final byte[] serializeInt(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    public static final int deserializeInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8 ) |
                ((bytes[3] & 0xFF) << 0 );
    }

    public final static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public final static int objectByteArrayToInt(Byte[] bytes) {
        return ByteBuffer.wrap(ArrayUtils.toPrimitive(bytes)).getInt();
    }

    public final static byte[] concatenateBytes(byte[]... bytes) {

        List<List<Byte>> byteArray = new ArrayList<>();
        for(int i=0;i<bytes.length;i++) {
            byteArray.add(Arrays.asList(ArrayUtils.toObject(bytes[i])));
        }
        List<Byte> byteList = byteArray.stream().flatMap((ba -> ba.stream())).collect(toList());
        Byte[] aByteArray = byteList.toArray(new Byte[byteList.size()]);
        return ArrayUtils.toPrimitive(aByteArray);
    }

    public final static byte[] concatenateBytes(byte[] lengthBytes, byte[] valueBytes) {
        byte[] bytes = new byte[lengthBytes.length + valueBytes.length];
        System.arraycopy(lengthBytes, 0, bytes, 0, lengthBytes.length);
        System.arraycopy(valueBytes, 0, bytes, lengthBytes.length, valueBytes.length);
        return bytes;
    }


}
