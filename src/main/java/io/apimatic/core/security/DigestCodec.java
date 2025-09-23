package io.apimatic.core.security;

/**
 * Interface for encoding and decoding digest values.
 */
public interface DigestCodec {
    /**
     * Encodes a byte array digest into a string representation.
     *
     * @param bytes The byte array to encode.
     * @return The encoded string representation.
     */
    String encode(byte[] bytes);

    /**
     * Decodes a string representation back into a byte array.
     *
     * @param encoded The encoded string to decode.
     * @return The decoded byte array.
     */
    byte[] decode(String encoded);
}
