package com.tidal.stream.encode;

import java.util.Base64;

public class Encoder {

    private Encoder() {
    }

    /**
     * Encodes a normal string to a base 64 string
     *
     * @param value string to encode
     * @return encoded string
     */
    public static String encodeToBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    /**
     * Decodes a base 64 encoded string
     *
     * @param encodedString value to decode
     * @return decoded string
     */
    public static String decodeBase64S(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}
