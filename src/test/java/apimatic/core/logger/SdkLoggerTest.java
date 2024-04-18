package apimatic.core.logger;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import io.apimatic.core.logger.SdkLogger;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyRequestLogging;

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
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.GET),
				eq("http://example.com"), eq(""));
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
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.POST),
				eq("http://example.com"), eq("application/json"));
	}
	
	@Test
	public void testLogRequestWithBody() {
		when(config.getRequestLogOptions().shouldLogBody()).thenReturn(true);
		Request request = mock(Request.class);
		when(request.getUrl()).thenReturn("http://example.com");
		when(request.getQueryUrl()).thenReturn("http://example.com");
		when(request.getHttpMethod()).thenReturn(Method.POST);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(headers.value("content-type")).thenReturn(null);
		when(request.getHeaders()).thenReturn(headers);
		when(request.getBody()).thenReturn("Test request body");

		sdkLogger.logRequest(request);

		// Verify the log message with query parameters
		verify(logger).log(eq(Level.INFO), eq("Request {} {} {}"), eq(Method.POST),
				eq("http://example.com"), eq(""));
		verify(logger).log(eq(Level.INFO), eq("Request Body {}"), eq("Test request body"));
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
}
