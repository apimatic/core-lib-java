package io.apimatic.core.security;

import java.util.Base64;

/**
 * Factory class for creating digest codecs based on encoding type.
 */
public final class DigestCodecFactory {

    private DigestCodecFactory() { } // Prevent instantiation

    /**
     * Creates a Hex codec.
     */
    public static DigestCodec hex() {
        return new HexDigestCodec();
    }

    /**
     * Creates a Base64 codec.
     */
    public static DigestCodec base64() {
        return new Base64DigestCodec();
    }

    /**
     * Creates a Base64Url codec.
     */
    public static DigestCodec base64Url() {
        return new Base64UrlDigestCodec();
    }

    /**
     * Codec for Hex encoding/decoding.
     */
    private static class HexDigestCodec implements DigestCodec {
        @Override
        public String encode(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        }

        @Override
        public byte[] decode(String encoded) {
            int len = encoded.length();
            if (len % 2 != 0) {
                throw new IllegalArgumentException("Invalid hex string length.");
            }
            byte[] result = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                result[i / 2] = (byte) ((Character.digit(encoded.charAt(i), 16) << 4)
                        + Character.digit(encoded.charAt(i + 1), 16));
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
