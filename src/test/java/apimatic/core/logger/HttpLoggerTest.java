package apimatic.core.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import io.apimatic.core.logger.HttpLogger;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingPolicy;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;

public class HttpLoggerTest {

    /**
     * Success code.
     */
    private static final int SUCCESS_CODE = 200;

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link Logger}.
     */
    @Mock
    private Logger logger;

    /**
     * Mock of {@link Request}.
     */
    @Mock
    private Request request;

    /**
     * Mock of {@link Response}.
     */
    @Mock
    private Response response;

    /**
     * Mock of {@link HttpHeaders}.
     */
    @Mock
    private HttpHeaders headers;

    /**
     * Mock of {@link ReadonlyLogging}.
     */
    @Mock
    private ReadonlyLogging readonlyLogging;

    /**
     * Mock of {@link HttpLogger}.
     */
    private HttpLogger httpLogger;

    /**
     * Test setup.
     */
    @Before
    public void setup() {
        httpLogger = new HttpLogger(logger, readonlyLogging);
    }

    @Test
    public void testLogRequestInfo() {
        when(request.getHttpMethod()).thenReturn(Method.GET);
        when(readonlyLogging.isLoggingRequestInfo()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());
        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        // In logged message a random string is being appended so, we cannot check the Equals
        // assertion.
        assertTrue(argumentCaptor.getValue().contains("GET"));
    }

    @Test
    public void testLogNullRequest() {
        httpLogger.logRequest(null, "https:\\localhost:3000\test");
        verify(logger, times(0)).info(anyString());
    }

    @Test
    public void testLogRequestInfoAdditionalMessage() {
        when(request.getHttpMethod()).thenReturn(Method.GET);
        when(readonlyLogging.isLoggingRequestInfo()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        String additionalMessage = "I am the logger";
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());
        httpLogger.logRequest(request, "https:\\localhost:3000\test", additionalMessage);
        // In logged message a random string is being appended so, we cannot check the Equals
        // assertion.
        assertTrue(argumentCaptor.getValue().contains(additionalMessage));
    }

    @Test
    public void testLogRequestHeaders() {
        when(request.getHeaders()).thenReturn(headers);
        when(readonlyLogging.isLoggingRequestHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertTrue(argumentCaptor.getValue().contains("Request"));
    }

    @Test
    public void testLogRequestIncludeHeaders() {
        Map<String, List<String>> filteredHeders = new HashMap<String, List<String>>();
        filteredHeders.put("accept", Arrays.asList("text/plain"));
        Set<String> setOfHeaderFilters = new HashSet<>();
        setOfHeaderFilters.add("accept");

        when(headers.asMultimap()).thenReturn(filteredHeders);
        when(headers.names()).thenReturn(setOfHeaderFilters);
        when(request.getHeaders()).thenReturn(headers);
        when(readonlyLogging.getHeaderFilters()).thenReturn(setOfHeaderFilters);
        when(readonlyLogging.getHeaderLoggingPolicy()).thenReturn(LoggingPolicy.INCLUDE);
        when(readonlyLogging.isLoggingRequestHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertTrue(argumentCaptor.getValue().contains("accept"));
    }

    @Test
    public void testNotExistInHeadersFiltersIncludePolicy() {
        Map<String, List<String>> filteredHeders = new HashMap<String, List<String>>();
        filteredHeders.put("accept", Arrays.asList("text/plain"));


        when(headers.asMultimap()).thenReturn(filteredHeders);

        when(request.getHeaders()).thenReturn(headers);
        Set<String> setOfHeaders = new HashSet<>();
        setOfHeaders.add("accept");
        when(headers.names()).thenReturn(setOfHeaders);

        Set<String> setOfHeaderFilters = new HashSet<>();
        when(readonlyLogging.getHeaderFilters()).thenReturn(setOfHeaderFilters);
        when(readonlyLogging.getHeaderLoggingPolicy()).thenReturn(LoggingPolicy.INCLUDE);
        when(readonlyLogging.isLoggingRequestHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertFalse(argumentCaptor.getValue().contains("accept"));
    }

    @Test
    public void testNotExistInHeadersFiltersExcludePolicy() {
        Map<String, List<String>> filteredHeders = new HashMap<String, List<String>>();
        filteredHeders.put("accept", Arrays.asList("text/plain"));


        when(headers.asMultimap()).thenReturn(filteredHeders);

        when(request.getHeaders()).thenReturn(headers);
        Set<String> setOfHeaders = new HashSet<>();
        setOfHeaders.add("accept");
        when(headers.names()).thenReturn(setOfHeaders);

        Set<String> setOfHeaderFilters = new HashSet<>();
        when(readonlyLogging.getHeaderFilters()).thenReturn(setOfHeaderFilters);
        when(readonlyLogging.getHeaderLoggingPolicy()).thenReturn(LoggingPolicy.EXCLUDE);
        when(readonlyLogging.isLoggingRequestHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertTrue(argumentCaptor.getValue().contains("accept"));
    }


    @Test
    public void testLogRequestExcludeHeaders() {
        Map<String, List<String>> filteredHeders = new HashMap<String, List<String>>();
        filteredHeders.put("accept", Arrays.asList("text/plain"));
        Set<String> setOfHeaderFilters = new HashSet<>();
        setOfHeaderFilters.add("accept");

        when(headers.asMultimap()).thenReturn(filteredHeders);
        when(headers.names()).thenReturn(setOfHeaderFilters);
        when(request.getHeaders()).thenReturn(headers);
        when(readonlyLogging.getHeaderFilters()).thenReturn(setOfHeaderFilters);
        when(readonlyLogging.getHeaderLoggingPolicy()).thenReturn(LoggingPolicy.EXCLUDE);
        when(readonlyLogging.isLoggingRequestHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.WARN);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).warn(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertFalse(argumentCaptor.getValue().contains("accept"));
    }


    @Test
    public void testLogRequestWithoutBody() {
        when(request.getHeaders()).thenReturn(headers);
        when(readonlyLogging.isLoggingRequestBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.TRACE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).trace(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertTrue(argumentCaptor.getValue().contains("Request"));
    }

    @Test
    public void testLogRequestBody() {
        when(request.getHeaders()).thenReturn(headers);
        when(request.getBody()).thenReturn("body");
        when(readonlyLogging.isLoggingRequestBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.TRACE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).trace(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertTrue(argumentCaptor.getValue().contains("body"));
    }

    @Test
    public void testLogRequestBodyFormParameters() {
        List<SimpleEntry<String, Object>> parameters = new ArrayList<SimpleEntry<String, Object>>();
        parameters.add(new SimpleEntry<String, Object>("key", "value"));


        when(request.getHeaders()).thenReturn(headers);
        when(request.getBody()).thenReturn(parameters);
        when(readonlyLogging.isLoggingRequestBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.DEBUG);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).debug(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertTrue(argumentCaptor.getValue().contains("key"));
        assertTrue(argumentCaptor.getValue().contains("value"));
    }

    @Test
    public void testLogRequestBodyEmptyFormParameters() {
        List<SimpleEntry<String, Object>> parameters = new ArrayList<SimpleEntry<String, Object>>();

        when(request.getHeaders()).thenReturn(headers);
        when(request.getBody()).thenReturn(parameters);
        when(readonlyLogging.isLoggingRequestBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.DEBUG);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).debug(argumentCaptor.capture());

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        assertFalse(argumentCaptor.getValue().contains("key"));
        assertFalse(argumentCaptor.getValue().contains("value"));
    }

    @Test
    public void testLogResponseInfoWithoutRequest() {
        when(response.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(readonlyLogging.isLoggingResponseInfo()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        httpLogger.logResponse(request, response);
        // In logged message a random string is being appended so, we cannot check the Equals
        // assertion.
        verify(logger, times(0)).info(anyString());
    }

    @Test
    public void testLogResponseInfo() {
        when(response.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(readonlyLogging.isLoggingResponseInfo()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());
        httpLogger.logResponse(request, response);
        // In logged message a random string is being appended so, we cannot check the Equals
        // assertion.
        assertTrue(argumentCaptor.getValue().contains("200"));
    }

    @Test
    public void testLogNullResponseInfo() {
        when(response.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(readonlyLogging.isLoggingResponseInfo()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());
        httpLogger.logResponse(request, null);
        // In logged message a random string is being appended so, we cannot check the Equals
        // assertion.
        assertTrue(argumentCaptor.getValue().contains("false"));
    }

    @Test
    public void testLogResponseIncludeHeaders() {
        when(readonlyLogging.isPrettyPrinting()).thenReturn(true);

        httpLogger = new HttpLogger(logger, readonlyLogging);
        Map<String, List<String>> filteredHeders = new HashMap<String, List<String>>();
        filteredHeders.put("accept", Arrays.asList("text/plain"));
        Set<String> setOfHeaderFilters = new HashSet<>();
        setOfHeaderFilters.add("accept");

        when(headers.asMultimap()).thenReturn(filteredHeders);
        when(headers.names()).thenReturn(setOfHeaderFilters);
        when(response.getHeaders()).thenReturn(headers);
        when(readonlyLogging.getHeaderFilters()).thenReturn(setOfHeaderFilters);
        when(readonlyLogging.getHeaderLoggingPolicy()).thenReturn(LoggingPolicy.INCLUDE);
        when(readonlyLogging.isLoggingResponseHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.INFO);

        httpLogger.logRequest(request, "https:\\localhost:3000\test");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).info(argumentCaptor.capture());

        httpLogger.logResponse(request, response);
        assertTrue(argumentCaptor.getValue().contains("accept"));
    }

    @Test
    public void testLogResponseExcludeHeaders() {
        Map<String, List<String>> filteredHeders = new HashMap<String, List<String>>();
        filteredHeders.put("accept", Arrays.asList("text/plain"));
        Set<String> setOfHeaderFilters = new HashSet<>();
        setOfHeaderFilters.add("accept");

        when(headers.asMultimap()).thenReturn(filteredHeders);
        when(headers.names()).thenReturn(setOfHeaderFilters);
        when(response.getHeaders()).thenReturn(headers);
        when(readonlyLogging.getHeaderFilters()).thenReturn(setOfHeaderFilters);
        when(readonlyLogging.getHeaderLoggingPolicy()).thenReturn(LoggingPolicy.EXCLUDE);
        when(readonlyLogging.isLoggingResponseHeaders()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.WARN);

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).warn(argumentCaptor.capture());

        httpLogger.logResponse(request, response);
        assertFalse(argumentCaptor.getValue().contains("accept"));
    }

    @Test
    public void testLogResponseBody() {
        when(response.getBody()).thenReturn("body");
        when(readonlyLogging.isLoggingResponseBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.TRACE);


        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).trace(argumentCaptor.capture());

        httpLogger.logResponse(request, response);
        assertTrue(argumentCaptor.getValue().contains("body"));
    }

    @Test
    public void testLogAdditionalMessage() {
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.TRACE);
        when(readonlyLogging.isLoggingResponseBody()).thenReturn(true);

        String additionalMessage = "I am the logger";
        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).trace(argumentCaptor.capture());

        httpLogger.logResponse(request, null, additionalMessage);
        assertTrue(argumentCaptor.getValue().contains(additionalMessage));
    }

    @Test
    public void testSetError() {
        when(response.getBody()).thenReturn("body");
        when(readonlyLogging.isLoggingResponseBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.ERROR);

        httpLogger.logRequest(request, "https:\\localhost:3000\test");
        httpLogger.setError(request, new CoreApiException("HTTP Resonse Not OK"));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).error(argumentCaptor.capture());

        httpLogger.logResponse(request, null);
        assertTrue(argumentCaptor.getValue().contains("HTTP"));
    }

    @Test
    public void testSetErrorWithoutRequest() {
        when(response.getBody()).thenReturn("body");
        when(readonlyLogging.isLoggingResponseBody()).thenReturn(true);
        when(readonlyLogging.getLevel()).thenReturn(LoggingLevel.ERROR);

        httpLogger.setError(request, new CoreApiException("HTTP Resonse Not OK"));

        httpLogger.logResponse(request, null);
        verify(logger, times(0)).error(anyString());
    }
}
