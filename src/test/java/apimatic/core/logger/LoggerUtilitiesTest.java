package apimatic.core.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.apimatic.core.logger.LoggerUtilities;

public class LoggerUtilitiesTest {
    @Test
    public void testExtractHeadersToLogIncludeHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        headers.put("Header3", "Value3");

        List<String> headersToInclude = Arrays.asList("header1", "header2");

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

        List<String> headersToExclude = Arrays.asList("header2", "header3");

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
}
