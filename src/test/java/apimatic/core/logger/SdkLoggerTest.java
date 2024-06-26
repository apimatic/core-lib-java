package apimatic.core.logger;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.apimatic.coreinterfaces.logger.configuration.RequestLoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ResponseLoggingConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import io.apimatic.core.logger.LoggerConstants;
import io.apimatic.core.logger.SdkLogger;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.LoggingConfiguration;

public class SdkLoggerTest {
    /**
     * An instance of {@link LoggingConfiguration}.
     */
    private LoggingConfiguration config;

    /**
     * An instance of {@link SdkLogger}.
     */
    private SdkLogger sdkLogger;

    /**
     * An instance of {@link Logger}.
     */
    private Logger logger;

    /**
     * Setup method for Sdk logger test class
     */
    @Before
    public void setUp() {
        config = mock(LoggingConfiguration.class);
        logger = mock(Logger.class);
        when(config.getLogger()).thenReturn(logger);
        when(config.getLevel()).thenReturn(Level.INFO);
        when(config.getRequestConfig()).thenReturn(mock(RequestLoggingConfiguration.class));
        when(config.getResponseConfig()).thenReturn(mock(ResponseLoggingConfiguration.class));
        sdkLogger = new SdkLogger(config);
    }

    @Test
    public void testLogRequest() {
        Request request = mock(Request.class);
        when(request.getUrl()).thenReturn("http://example.com");
        when(request.getQueryUrl()).thenReturn("http://example.com");
        when(request.getHttpMethod()).thenReturn(Method.GET);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.value("content-type")).thenReturn(null);
        when(request.getHeaders()).thenReturn(headers);

        sdkLogger.logRequest(request);

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, Method.GET);
        requestArguments.put(LoggerConstants.URL, "http://example.com");
        requestArguments.put(LoggerConstants.CONTENT_TYPE, null);

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(requestArguments));
    }

    @Test
    public void testLogPostRequest() {
        Request request = mock(Request.class);
        when(request.getUrl()).thenReturn("http://example.com");
        when(request.getQueryUrl()).thenReturn("http://example.com");
        when(request.getHttpMethod()).thenReturn(Method.POST);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.value("content-type")).thenReturn("application/json");
        when(request.getHeaders()).thenReturn(headers);

        sdkLogger.logRequest(request);

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, Method.POST);
        requestArguments.put(LoggerConstants.URL, "http://example.com");
        requestArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(requestArguments));
    }

    @Test
    public void testLogRequestWithBody() {
        when(config.getRequestConfig().shouldLogBody()).thenReturn(true);
        Request request = mock(Request.class);
        when(request.getUrl()).thenReturn("http://example.com");
        when(request.getQueryUrl()).thenReturn("http://example.com");
        when(request.getHttpMethod()).thenReturn(Method.POST);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.value("content-type")).thenReturn("application/json");
        when(request.getHeaders()).thenReturn(headers);
        when(request.getBody()).thenReturn("Test request body");

        sdkLogger.logRequest(request);

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, Method.POST);
        requestArguments.put(LoggerConstants.URL, "http://example.com");
        requestArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");

        Map<String, Object> requestBodyArguments = new LinkedHashMap<String, Object>();
        requestBodyArguments.put(LoggerConstants.BODY, "Test request body");

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(requestArguments));
        verify(logger).log(eq(Level.INFO), eq("Request Body {}"), eq(requestBodyArguments));
    }

    @Test
    public void testLogRequestWithHeader() {
        when(config.getRequestConfig().shouldLogHeaders()).thenReturn(true);
        Request request = mock(Request.class);
        when(request.getUrl()).thenReturn("http://example.com");
        when(request.getQueryUrl()).thenReturn("http://example.com");
        when(request.getHttpMethod()).thenReturn(Method.POST);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.value("content-type")).thenReturn("application/json");
        when(request.getHeaders()).thenReturn(headers);

        Map<String, String> mockHeaders = new HashMap<>();
        mockHeaders.put("Authorization", "Basic <credentials>");
        when(request.getHeaders().asSimpleMap()).thenReturn(mockHeaders);

        sdkLogger.logRequest(request);

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Authorization", "Basic <credentials>");

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, Method.POST);
        requestArguments.put(LoggerConstants.URL, "http://example.com");
        requestArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");

        Map<String, Object> requestBodyArguments = new LinkedHashMap<String, Object>();
        requestBodyArguments.put(LoggerConstants.HEADERS, expectedHeaders);

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(requestArguments));
        verify(logger).log(eq(Level.INFO), eq("Request Headers {}"), eq(requestBodyArguments));
    }

    @Test
    public void testLogRequestWithQueryParameters() {
        when(config.getRequestConfig().shouldIncludeQueryInPath()).thenReturn(true);

        Request request = mock(Request.class);
        when(request.getUrl()).thenReturn("http://example.com");
        when(request.getQueryUrl()).thenReturn("http://example.com?param=value");
        when(request.getHttpMethod()).thenReturn(Method.GET);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.value("content-type")).thenReturn(null);
        when(request.getHeaders()).thenReturn(headers);

        sdkLogger.logRequest(request);

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, Method.GET);
        requestArguments.put(LoggerConstants.URL, "http://example.com?param=value");
        requestArguments.put(LoggerConstants.CONTENT_TYPE, null);

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"),
                eq(requestArguments));
    }

    @Test
    public void testLogResponse() {
        final int statusCode = 200;
        Response response = mock(Response.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(response.getStatusCode()).thenReturn(statusCode);
        when(headers.value("content-length")).thenReturn("100");
        when(headers.value("content-type")).thenReturn("application/json");
        when(response.getHeaders()).thenReturn(headers);

        when(config.getLevel()).thenReturn(Level.INFO);
        when(config.getMaskSensitiveHeaders()).thenReturn(true);

        sdkLogger.logResponse(response);

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, statusCode);
        responseArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, "100");

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Response {} {} {}"),
                eq(responseArguments));
    }

    @Test
    public void testLogResponseWithBody() {
        final int statusCode = 200;
        Response response = mock(Response.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(response.getStatusCode()).thenReturn(statusCode);
        when(headers.value("content-length")).thenReturn("100");
        when(headers.value("content-type")).thenReturn("application/json");
        when(response.getHeaders()).thenReturn(headers);
        when(response.getBody()).thenReturn("Test response body");

        when(config.getLevel()).thenReturn(Level.INFO);
        when(config.getResponseConfig().shouldLogBody()).thenReturn(true);

        sdkLogger.logResponse(response);

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, statusCode);
        responseArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, "100");

        Map<String, Object> responseBodyArguments = new LinkedHashMap<String, Object>();
        responseBodyArguments.put(LoggerConstants.BODY, "Test response body");

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Response {} {} {}"),
                eq(responseArguments));
        verify(logger).log(eq(Level.INFO), eq("Response Body {}"), eq(responseBodyArguments));
    }

    @Test
    public void testLogResponseWithHeaders() {
        final int statusCode = 200;

        Response response = mock(Response.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(response.getStatusCode()).thenReturn(statusCode);
        when(headers.value("content-length")).thenReturn("100");
        when(headers.value("content-type")).thenReturn("application/json");
        when(response.getHeaders()).thenReturn(headers);

        when(config.getLevel()).thenReturn(Level.INFO);
        when(config.getResponseConfig().shouldLogHeaders()).thenReturn(true);
        when(config.getMaskSensitiveHeaders()).thenReturn(true);

        Map<String, String> mockHeaders = new HashMap<>();
        mockHeaders.put("Authorization", "Basic <credentials>");
        mockHeaders.put("Content-Encoding", "gzip");
        when(headers.asSimpleMap()).thenReturn(mockHeaders);

        sdkLogger.logResponse(response);

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Authorization", "**Redacted**");
        expectedHeaders.put("Content-Encoding", "gzip");

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, statusCode);
        responseArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, "100");

        Map<String, Object> responseHeaderArguments = new LinkedHashMap<String, Object>();
        responseHeaderArguments.put(LoggerConstants.HEADERS, expectedHeaders);

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Response {} {} {}"),
                eq(responseArguments));
        verify(logger).log(eq(Level.INFO), eq("Response Headers {}"), eq(responseHeaderArguments));
    }

    @Test
    public void testLogResponseWithExcludeHeaders() {
        final int statusCode = 200;
        final List<String> excludeHeaders = Arrays.asList("content-encoding");

        Response response = mock(Response.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(response.getStatusCode()).thenReturn(statusCode);
        when(headers.value("content-length")).thenReturn("100");
        when(headers.value("content-type")).thenReturn("application/json");
        when(response.getHeaders()).thenReturn(headers);

        when(config.getLevel()).thenReturn(Level.INFO);
        when(config.getResponseConfig().shouldLogHeaders()).thenReturn(true);
        when(config.getResponseConfig().getHeadersToExclude()).thenReturn(excludeHeaders);
        when(config.getMaskSensitiveHeaders()).thenReturn(true);

        Map<String, String> mockHeaders = new HashMap<>();
        mockHeaders.put("Authorization", "Basic <credentials>");
        mockHeaders.put("Content-Encoding", "gzip");
        when(headers.asSimpleMap()).thenReturn(mockHeaders);

        sdkLogger.logResponse(response);

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Authorization", "**Redacted**");

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, statusCode);
        responseArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, "100");

        Map<String, Object> responseHeaderArguments = new LinkedHashMap<String, Object>();
        responseHeaderArguments.put(LoggerConstants.HEADERS, expectedHeaders);

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Response {} {} {}"),
                eq(responseArguments));
        verify(logger).log(eq(Level.INFO), eq("Response Headers {}"), eq(responseHeaderArguments));
    }

    @Test
    public void testLogResponseWithWhiteListHeaders() {
        final int statusCode = 200;
        final List<String> unmaskHeaders = Arrays.asList("masked-header");

        Response response = mock(Response.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        when(response.getStatusCode()).thenReturn(statusCode);
        when(headers.value("content-length")).thenReturn("100");
        when(headers.value("content-type")).thenReturn("application/json");
        when(response.getHeaders()).thenReturn(headers);

        when(config.getLevel()).thenReturn(Level.INFO);
        when(config.getResponseConfig().shouldLogHeaders()).thenReturn(true);
        when(config.getResponseConfig().getHeadersToUnmask()).thenReturn(unmaskHeaders);
        when(config.getMaskSensitiveHeaders()).thenReturn(true);

        Map<String, String> mockHeaders = new HashMap<>();
        mockHeaders.put("Authorization", "Basic <credentials>");
        mockHeaders.put("Masked-Header", "MaskedValue");
        when(headers.asSimpleMap()).thenReturn(mockHeaders);

        sdkLogger.logResponse(response);

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Authorization", "**Redacted**");
        expectedHeaders.put("Masked-Header", "MaskedValue");

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, statusCode);
        responseArguments.put(LoggerConstants.CONTENT_TYPE, "application/json");
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, "100");

        Map<String, Object> responseHeaderArguments = new LinkedHashMap<String, Object>();
        responseHeaderArguments.put(LoggerConstants.HEADERS, expectedHeaders);

        // Verify the log message with query parameters
        verify(logger).log(eq(Level.INFO), eq("Response {} {} {}"),
                eq(responseArguments));
        verify(logger).log(eq(Level.INFO), eq("Response Headers {}"), eq(responseHeaderArguments));
    }
}
