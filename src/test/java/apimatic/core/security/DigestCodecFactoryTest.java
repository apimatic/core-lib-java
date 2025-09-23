package apimatic.core.security;

import io.apimatic.core.security.DigestCodec;
import io.apimatic.core.security.DigestCodecFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DigestCodecFactoryTest {

    /** Rule for expecting exceptions in tests. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final byte HEX_BYTE_1 = 0x0A;
    private static final byte HEX_BYTE_2 = 0x1B;
    private static final byte HEX_BYTE_3 = (byte) 0xFF;

    private static final byte[] BASE64_INPUT = {1, 2, 3, 4, 5};
    private static final byte BASE64_BYTE_1 = 1;
    private static final byte BASE64_BYTE_2 = 2;
    private static final byte BASE64_BYTE_3 = 3;
    private static final byte BASE64_BYTE_4 = 4;
    private static final byte BASE64_BYTE_5 = 5;

    private static final byte BASE64URL_BYTE_1 = 10;
    private static final byte BASE64URL_BYTE_2 = 20;
    private static final byte BASE64URL_BYTE_3 = 30;
    private static final byte BASE64URL_BYTE_4 = 40;
    private static final byte BASE64URL_BYTE_5 = 50;

    @Test
    public void testHexEncodeDecode() {
        DigestCodec codec = DigestCodecFactory.hex();
        byte[] input = {HEX_BYTE_1, HEX_BYTE_2, HEX_BYTE_3};
        String encoded = codec.encode(input);
        assertEquals("0a1bff", encoded);
        assertArrayEquals(input, codec.decode(encoded));
    }

    @Test
    public void testHexEncodeEmpty() {
        DigestCodec codec = DigestCodecFactory.hex();
        byte[] input = {};
        String encoded = codec.encode(input);
        assertEquals("", encoded);
        assertArrayEquals(input, codec.decode(encoded));
    }

    @Test
    public void testHexDecodeInvalidLength() {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        codec.decode("abc");
    }

    @Test
    public void testHexDecodeInvalidCharacter() {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        codec.decode("zzzzz");
    }

    @Test
    public void testBase64EncodeDecode() {
        DigestCodec codec = DigestCodecFactory.base64();
        byte[] input = {BASE64_BYTE_1, BASE64_BYTE_2, BASE64_BYTE_3, BASE64_BYTE_4, BASE64_BYTE_5};
        String encoded = codec.encode(input);
        assertEquals("AQIDBAU=", encoded);
        assertArrayEquals(input, codec.decode(encoded));
    }

    @Test
    public void testBase64EncodeEmpty() {
        DigestCodec codec = DigestCodecFactory.base64();
        byte[] input = {};
        String encoded = codec.encode(input);
        assertEquals("", encoded);
        assertArrayEquals(input, codec.decode(encoded));
    }

    @Test
    public void testBase64DecodeInvalid() {
        DigestCodec codec = DigestCodecFactory.base64();
        thrown.expect(IllegalArgumentException.class);
        codec.decode("!@#$");
    }

    @Test
    public void testBase64UrlEncodeDecode() {
        DigestCodec codec = DigestCodecFactory.base64Url();
        byte[] input = {BASE64URL_BYTE_1, BASE64URL_BYTE_2,
                BASE64URL_BYTE_3, BASE64URL_BYTE_4, BASE64URL_BYTE_5};
        String encoded = codec.encode(input);
        assertEquals("ChQeKDI", encoded); // without padding
        assertArrayEquals(input, codec.decode(encoded));
    }

    @Test
    public void testBase64UrlEncodeEmpty() {
        DigestCodec codec = DigestCodecFactory.base64Url();
        byte[] input = {};
        String encoded = codec.encode(input);
        assertEquals("", encoded);
        assertArrayEquals(input, codec.decode(encoded));
    }

    @Test
    public void testBase64UrlDecodeInvalid() {
        DigestCodec codec = DigestCodecFactory.base64Url();
        thrown.expect(IllegalArgumentException.class);
        codec.decode("!@#$");
    }
}
