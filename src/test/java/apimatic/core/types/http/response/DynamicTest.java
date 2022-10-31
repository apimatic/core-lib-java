package apimatic.core.types.http.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core.types.http.response.Dynamic;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.response.Response;

public class DynamicTest {

    private static final long TEST_LONG_NUMBER = 5L;

    private static final double TEST_DOUBLE_NUMBER = 1.5;

    /**
     * Float number
     */
    private static final float FLOAT_NUMBER = 5.9f;

    /**
     * Test number
     */
    private static final int TEST_NUMBER = 5;

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    /**
     * Mock of {@link Response}
     */
    @Mock
    private Response coreHttpResponse;

    /**
     * Mock of {@link HttpHeaders}
     */
    @Mock
    private HttpHeaders headers;

    @Test
    public void testParseAsBoolean() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("true");

        boolean expected = true;
        boolean actual = dynamic.parseAsBoolean();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsCharacter() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("65");

        char expected = 'A';
        char actual = dynamic.parseAsCharacter();
        assertEquals(actual, expected);
    }

    @Test(expected = ParseException.class)
    public void testParseAsCharacter1() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("\'A\'");

        dynamic.parseAsCharacter();
    }

    @Test
    public void testParseAsDouble() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("1.5");

        Double expected = TEST_DOUBLE_NUMBER;
        Double actual = dynamic.parseAsDouble();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsByte() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        byte expected = TEST_NUMBER;
        byte actual = dynamic.parseAsByte();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsDictionary() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("{\"parsingKey\":5}");

        Map<String, Object> expected = new HashMap<>();
        expected.put("parsingKey", TEST_NUMBER);
        Map<String, Object> actual = dynamic.parseAsDictionary();
        assertEquals(actual, expected);
    }

    @Test(expected = ParseException.class)
    public void testParseAsDictionary1() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("{\"parsingKey\": 'k}");
        dynamic.parseAsDictionary();
    }

    @Test
    public void testParseAsFloat() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5.9");

        Float expected = FLOAT_NUMBER;
        Float actual = dynamic.parseAsFloat();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsInteger() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        Integer expected = TEST_NUMBER;
        Integer actual = dynamic.parseAsInteger();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsLong() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        Long expected = TEST_LONG_NUMBER;
        Long actual = dynamic.parseAsLong();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsShort() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        short expected = TEST_NUMBER;
        short actual = dynamic.parseAsShort();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsString() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("parsing");

        String expected = "parsing";
        String actual = dynamic.parseAsString();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetHeaders() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getHeaders()).thenReturn(headers);

        HttpHeaders expected = headers;
        HttpHeaders actual = dynamic.getHeaders();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetRawBody() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);
        String response = "response string";
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());

        when(coreHttpResponse.getRawBody()).thenReturn(inputStream);
        InputStream actual = dynamic.getRawBody();
        assertNotNull(actual);
    }
}
