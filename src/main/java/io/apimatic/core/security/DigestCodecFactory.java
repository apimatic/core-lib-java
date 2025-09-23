package io.apimatic.core.security;

import java.util.Base64;

/**
 * Factory class for creating digest codecs based on encoding type.
 */
public final class DigestCodecFactory {

    private DigestCodecFactory() { } // Prevent instantiation

    /**
     * Creates a Hex codec.
     * @return a DigestCodec for Hex encoding/decoding
     */
    public static DigestCodec hex() {
        return new HexDigestCodec();
    }

    /**
     * Creates a Base64 codec.
     * @return a DigestCodec for Base64 encoding/decoding
     */
    public static DigestCodec base64() {
        return new Base64DigestCodec();
    }

    /**
     * Creates a Base64Url codec.
     * @return a DigestCodec for Base64Url encoding/decoding
     */
    public static DigestCodec base64Url() {
        return new Base64UrlDigestCodec();
    }

    private static final int HEX_RADIX = 16;
    private static final int HEX_BYTE_MASK = 0xff;
    private static final int HEX_BYTE_LENGTH = 2;
    private static final int HEX_SHIFT = 4;

    /**
     * Codec for Hex encoding/decoding.
     */
    private static class HexDigestCodec implements DigestCodec {
        @Override
        public String encode(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & HEX_BYTE_MASK));
            }
            return sb.toString();
        }

        @Override
        public byte[] decode(String encoded) {
            int len = encoded.length();
            if (len % HEX_BYTE_LENGTH != 0) {
                throw new IllegalArgumentException("Invalid hex string length.");
            }
            byte[] result = new byte[len / HEX_BYTE_LENGTH];
            for (int i = 0; i < len; i += HEX_BYTE_LENGTH) {
                result[i / HEX_BYTE_LENGTH] = (byte) ((Character.digit(encoded.charAt(i), HEX_RADIX) << HEX_SHIFT)
                        + Character.digit(encoded.charAt(i + 1), HEX_RADIX));
            }
            return result;
        }
    }

    /**
     * Codec for Base64 encoding/decoding.
     */
    private static class Base64DigestCodec implements DigestCodec {
        @Override
        public String encode(byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes);
        }

        @Override
        public byte[] decode(String encoded) {
            return Base64.getDecoder().decode(encoded);
        }
    }

    /**
     * Codec for Base64Url encoding/decoding.
     */
    private static class Base64UrlDigestCodec implements DigestCodec {
        @Override
        public String encode(byte[] bytes) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        }

        @Override
        public byte[] decode(String encoded) {
            return Base64.getUrlDecoder().decode(encoded);
        }
    }
}
