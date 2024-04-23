package apimatic.core.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import io.apimatic.core.logger.LoggerUtilities;

public class LoggerUtilitiesTest {
    @Test
    public void testExtractHeadersToLogIncludeHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        headers.put("Header3", "Value3");

        List<String> headersToInclude = getLowerCaseList("Header1", "Header2");

        Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(headers,
                headersToInclude, Collections.emptyList());

        final int expectedHeaderSize = 2;
        assertEquals(expectedHeaderSize, extractedHeaders.size());
        assertTrue(extractedHeaders.containsKey("Header1"));
        assertTrue(extractedHeaders.containsKey("Header2"));
    }

    @Test
    public void testExtractHeadersToLogExcludeHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        headers.put("Header3", "Value3");

        List<String> headersToExclude = getLowerCaseList("Header2", "Header3");

        Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(headers,
                Collections.emptyList(), headersToExclude);

        final int expectedHeaderSize = 1;
        assertEquals(expectedHeaderSize, extractedHeaders.size());
        assertTrue(extractedHeaders.containsKey("Header1"));
    }

    @Test
    public void testExtractHeadersToLogNoFilter() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        headers.put("Header3", "Value3");

        Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(headers,
                Collections.emptyList(), Collections.emptyList());

        final int expectedHeaderSize = 3;
        assertEquals(expectedHeaderSize, extractedHeaders.size());
        assertTrue(extractedHeaders.containsKey("Header1"));
        assertTrue(extractedHeaders.containsKey("Header2"));
        assertTrue(extractedHeaders.containsKey("Header3"));
    }

    @Test
    public void testExtractHeadersToLogWithFilter() {
        final boolean enableMasking = true;
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "Value1");
        headers.put("Masked-Header", "MaskedValue");

        List<String> headersToWhiteList = getLowerCaseList("Accept");

        Map<String, String> extractedHeaders = LoggerUtilities.filterSensitiveHeaders(headers,
                headersToWhiteList, enableMasking);

        final int expectedHeaderSize = 2;
        final String expectedAcceptHeaderValue = "Value1";
        final String expectedMaskedHeaderValue = "**Redacted**";

        assertEquals(expectedHeaderSize, extractedHeaders.size());

        assertTrue(extractedHeaders.containsKey("Accept"));
        assertTrue(expectedAcceptHeaderValue.equals(extractedHeaders.get("Accept")));

        assertTrue(extractedHeaders.containsKey("Masked-Header"));
        assertTrue(expectedMaskedHeaderValue.equals(extractedHeaders.get("Masked-Header")));
    }

    @Test
    public void testExtractHeadersToLogWithFilterUnmasked() {
        final boolean enableMasking = false;
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "Value1");
        headers.put("Masked-Header", "MaskedValue");

        List<String> headersToWhiteList = getLowerCaseList("Accept");

        Map<String, String> extractedHeaders = LoggerUtilities.filterSensitiveHeaders(headers,
                headersToWhiteList, enableMasking);

        final int expectedHeaderSize = 2;
        final String expectedAcceptHeaderValue = "Value1";
        final String expectedMaskedHeaderValue = "MaskedValue";

        assertEquals(expectedHeaderSize, extractedHeaders.size());

        assertTrue(extractedHeaders.containsKey("Accept"));
        assertTrue(expectedAcceptHeaderValue.equals(extractedHeaders.get("Accept")));

        assertTrue(extractedHeaders.containsKey("Masked-Header"));
        assertTrue(expectedMaskedHeaderValue.equals(extractedHeaders.get("Masked-Header")));
    }

    /**
     * Converts an array of strings to a list of strings, with each string converted
     * to lowercase.
     * @param strings The strings to convert to lowercase.
     * @return A list containing the lowercase versions of the input strings.
     */
    private List<String> getLowerCaseList(String... strings) {
        return Arrays.stream(strings).map(String::toLowerCase).collect(Collectors.toList());
    }
}
