package apimatic.core.logger;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import io.apimatic.core.logger.SdkLogger;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyRequestLogging;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogging;

public class SdkLoggerTest {
	/**
	 * An instance of {@link ReadonlyLoggingConfiguration}.
	 */
	private ReadonlyLoggingConfiguration config;

	/**
	 * An instance of {@link SdkLogger}.
	 */
	private SdkLogger sdkLogger;

	/**
	 * An instance of {@link Logger}.
	 */
	private Logger logger;

	@Before
	public void setUp() {
		config = mock(ReadonlyLoggingConfiguration.class);
		logger = mock(Logger.class);
		when(config.getLogger()).thenReturn(logger);
		when(config.getLevel()).thenReturn(Level.INFO);
		when(config.getRequestLogOptions()).thenReturn(mock(ReadonlyRequestLogging.class));
		when(config.getResponseLogOptions()).thenReturn(mock(ReadonlyResponseLogging.class));
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

		// Verify the log message with query parameters
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.GET), eq("http://example.com"), eq(""));
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

		// Verify the log message with query parameters
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.POST), eq("http://example.com"),
				eq("application/json"));
	}

	@Test
	public void testLogRequestWithBody() {
		when(config.getRequestLogOptions().shouldLogBody()).thenReturn(true);
		Request request = mock(Request.class);
		when(request.getUrl()).thenReturn("http://example.com");
		when(request.getQueryUrl()).thenReturn("http://example.com");
		when(request.getHttpMethod()).thenReturn(Method.POST);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(headers.value("content-type")).thenReturn("application/json");
		when(request.getHeaders()).thenReturn(headers);
		when(request.getBody()).thenReturn("Test request body");

		sdkLogger.logRequest(request);

		// Verify the log message with query parameters
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.POST), eq("http://example.com"),
				eq("application/json"));
		verify(logger).log(eq(Level.INFO), eq("Request Body {}"), eq("Test request body"));
	}

	@Test
	public void testLogRequestWithHeader() {
		when(config.getRequestLogOptions().shouldLogHeaders()).thenReturn(true);
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

		// Verify the log message with query parameters
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.POST), eq("http://example.com"),
				eq("application/json"));
		verify(logger).log(eq(Level.INFO), eq("Request Headers {}"), eq(expectedHeaders));
	}

	@Test
	public void testLogRequestWithQueryParameters() {
		when(config.getRequestLogOptions().shouldIncludeQueryInPath()).thenReturn(true);

		Request request = mock(Request.class);
		when(request.getUrl()).thenReturn("http://example.com");
		when(request.getQueryUrl()).thenReturn("http://example.com?param=value");
		when(request.getHttpMethod()).thenReturn(Method.GET);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(headers.value("content-type")).thenReturn(null);
		when(request.getHeaders()).thenReturn(headers);

		sdkLogger.logRequest(request);

		// Verify the log message with query parameters
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {} queryParameters: {}"), eq(Method.GET),
				eq("http://example.com"), eq(""), eq("param=value"));
	}

	@Test
	public void testLogResponse() {
		Response response = mock(Response.class);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(response.getStatusCode()).thenReturn(200);
		when(headers.value("content-length")).thenReturn("100");
		when(headers.value("content-type")).thenReturn("application/json");
		when(response.getHeaders()).thenReturn(headers);

		when(config.getLevel()).thenReturn(Level.INFO);
		when(config.getMaskSensitiveHeaders()).thenReturn(true);

		sdkLogger.logResponse(response);

		verify(logger).log(eq(Level.INFO), eq("Response {} {} content-length: {}"), eq(200), eq("application/json"),
				eq("100"));
	}
	
	
	@Test
	public void testLogResponseWithBody() {
		Response response = mock(Response.class);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(response.getStatusCode()).thenReturn(200);
		when(headers.value("content-length")).thenReturn("100");
		when(headers.value("content-type")).thenReturn("application/json");
		when(response.getHeaders()).thenReturn(headers);
		when(response.getBody()).thenReturn("Test response body");
		
		when(config.getLevel()).thenReturn(Level.INFO);
		when(config.getResponseLogOptions().shouldLogBody()).thenReturn(true);

		sdkLogger.logResponse(response);

		verify(logger).log(eq(Level.INFO), eq("Response {} {} content-length: {}"), eq(200), eq("application/json"),
				eq("100"));
		
		verify(logger).log(eq(Level.INFO), eq("Response Body {}"), eq("Test response body"));
	}

	@Test
	public void testLogResponseWithHeaders() {
		Response response = mock(Response.class);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(response.getStatusCode()).thenReturn(200);
		when(headers.value("content-length")).thenReturn("100");
		when(headers.value("content-type")).thenReturn("application/json");
		when(response.getHeaders()).thenReturn(headers);

		when(config.getLevel()).thenReturn(Level.INFO);
		when(config.getResponseLogOptions().shouldLogHeaders()).thenReturn(true);
		when(config.getMaskSensitiveHeaders()).thenReturn(true);

		Map<String, String> mockHeaders = new HashMap<>();
		mockHeaders.put("Authorization", "Basic <credentials>");
		when(headers.asSimpleMap()).thenReturn(mockHeaders);

		sdkLogger.logResponse(response);
		
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put("Authorization", "**Redacted**");

		verify(logger).log(eq(Level.INFO), eq("Response {} {} content-length: {}"), eq(200), eq("application/json"),
				eq("100"));
		verify(logger).log(eq(Level.INFO), eq("Response Headers {}") ,eq(expectedHeaders));
	}
}
