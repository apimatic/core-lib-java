package apimatic.core.configurations.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import io.apimatic.core.configurations.http.client.ApiLoggingConfiguration;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingPolicy;

public class ApiLoggingConfigurationTest {

    @Test
    public void testLogEverything() {
        ApiLoggingConfiguration apiLoggingConfiguration = new ApiLoggingConfiguration.Builder()
                .logEverything().headerLoggingPolicy(LoggingPolicy.EXCLUDE).build();
        assertTrue(apiLoggingConfiguration.isLoggingRequestBody());
        assertTrue(apiLoggingConfiguration.isLoggingRequestHeaders());
        assertTrue(apiLoggingConfiguration.isLoggingRequestInfo());
        assertTrue(apiLoggingConfiguration.isLoggingResponseBody());
        assertTrue(apiLoggingConfiguration.isLoggingResponseHeaders());
        assertTrue(apiLoggingConfiguration.isLoggingResponseInfo());
        assertEquals(apiLoggingConfiguration.getHeaderLoggingPolicy(), LoggingPolicy.EXCLUDE);
    }

    @Test
    public void testLogNothing() {
        ApiLoggingConfiguration apiLoggingConfiguration = new ApiLoggingConfiguration.Builder()
                .logNothing().headerLoggingPolicy(LoggingPolicy.INCLUDE).level(LoggingLevel.INFO)
                .build();
        assertFalse(apiLoggingConfiguration.isLoggingRequestBody());
        assertFalse(apiLoggingConfiguration.isLoggingRequestHeaders());
        assertFalse(apiLoggingConfiguration.isLoggingRequestInfo());
        assertFalse(apiLoggingConfiguration.isLoggingResponseBody());
        assertFalse(apiLoggingConfiguration.isLoggingResponseHeaders());
        assertFalse(apiLoggingConfiguration.isLoggingResponseInfo());
        assertEquals(apiLoggingConfiguration.getHeaderLoggingPolicy(), LoggingPolicy.INCLUDE);
        assertEquals(apiLoggingConfiguration.getLevel(), LoggingLevel.INFO);
    }


    @Test
    public void testLogsExplicity() {
        TreeSet<String> headerFilters = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        headerFilters.add("accept");
        ApiLoggingConfiguration apiLoggingConfiguration = new ApiLoggingConfiguration.Builder()
                .logRequestBody(false).logRequestHeaders(true).logRequestInfo(true)
                .logResponseBody(false).logResponseInfo(true).logResponseHeaders(true)
                .prettyPrintLogs(true).clearHeaderFilter().addHeaderFilter("accept")
                .headerLoggingPolicy(LoggingPolicy.INCLUDE).level(LoggingLevel.ERROR).build();
        assertFalse(apiLoggingConfiguration.isLoggingRequestBody());
        assertFalse(apiLoggingConfiguration.isLoggingResponseBody());
        assertTrue(apiLoggingConfiguration.isLoggingRequestHeaders());
        assertTrue(apiLoggingConfiguration.isLoggingRequestInfo());
        assertTrue(apiLoggingConfiguration.isLoggingResponseHeaders());
        assertTrue(apiLoggingConfiguration.isLoggingResponseInfo());
        assertTrue(apiLoggingConfiguration.isPrettyPrinting());
        assertEquals(apiLoggingConfiguration.getHeaderLoggingPolicy(), LoggingPolicy.INCLUDE);
        assertEquals(apiLoggingConfiguration.getHeaderFilters(), headerFilters);
        assertEquals(apiLoggingConfiguration.getLevel(), LoggingLevel.ERROR);
    }


    @Test
    public void testHeaderFilters() {
        TreeSet<String> headerFilters = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        headerFilters.add("accept");
        ApiLoggingConfiguration apiLoggingConfiguration = new ApiLoggingConfiguration.Builder()
                .headerFilters(headerFilters).headerLoggingPolicy(LoggingPolicy.INCLUDE)
                .level(LoggingLevel.ERROR).build();
        assertEquals(apiLoggingConfiguration.getHeaderFilters(), headerFilters);
        assertEquals(apiLoggingConfiguration.getLevel(), LoggingLevel.ERROR);
    }


}
