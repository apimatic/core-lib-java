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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testHexEncodeDecode() {
        DigestCodec codec = DigestCodecFactory.hex();
        byte[] input = {0x0A, 0x1B, (byte)0xFF};
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
        byte[] input = {1, 2, 3, 4, 5};
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
        byte[] input = {10, 20, 30, 40, 50};
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
