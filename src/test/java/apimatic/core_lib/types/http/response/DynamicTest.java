package apimatic.core_lib.types.http.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.response.Response;
import io.apimatic.core_lib.types.http.response.Dynamic;

public class DynamicTest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    private Response coreHttpResponse;

    @Mock
    private HttpHeaders headers;

    @Before
    public void setup() {}

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

        Double expected = 1.5;
        Double actual = dynamic.parseAsDouble();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsByte() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        byte expected = 5;
        byte actual = dynamic.parseAsByte();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsDictionary() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("{\"parsingKey\":123}");

        Map<String, Object> expected = new HashMap<>();
        expected.put("parsingKey", 123);
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

        Float expected = 5.9f;
        Float actual = dynamic.parseAsFloat();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsInteger() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        Integer expected = 5;
        Integer actual = dynamic.parseAsInteger();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsLong() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        Long expected = 5l;
        Long actual = dynamic.parseAsLong();
        assertEquals(actual, expected);
    }

    @Test
    public void testParseAsShort() throws ParseException {
        Dynamic dynamic = new Dynamic(coreHttpResponse);

        when(coreHttpResponse.getBody()).thenReturn("5");

        short expected = 5;
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
