package io.apimatic.core.logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.event.Level;

import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;

/**
 * Class to log the Http api messages.
 */
public class SdkLogger implements ApiLogger {
	/**
	 * An instance of {@link Logger}.
	 */
	private Logger logger;

	/**
	 * An instance of {@link ReadonlyLogging}.
	 */
	private ReadonlyLogging config;

	/**
	 * List of sensitive headers that need to be filtered.
	 */
	private static final List<String> sensitiveHeaders = Arrays.asList("authorization", "www-authenticate",
			"proxy-authorization", "set-cookie");

	/**
	 * Default Constructor.
	 * 
	 * @param config {@link ReadonlyLogging} as logging properties.
	 */
	public SdkLogger(final ReadonlyLogging config) {
		this.config = config;
		this.logger = config.getLogger();
	}

	/**
	 * Log requests.
	 * 
	 * @param request HttpRequest to be logged.
	 */
	public void logRequest(Request request) {
		Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

		String url = request.getUrl();
		String queryParameter = CoreHelper.getQueryParametersFromUrl(request.getQueryUrl());
		String contentType = request.getHeaders().value("content-type") != null
				? request.getHeaders().value("content-type")
				: "";

		if (config.getRequestLogOptions().shouldIncludeQueryInPath() && !queryParameter.isEmpty()) {
			logger.log(level, "Request {} {} {} queryParameters: {}", request.getHttpMethod(), url, contentType,
					queryParameter);
		} else {
			logger.log(level, "Request {} {} {}", request.getHttpMethod(), url, contentType);
		}

		if (config.getRequestLogOptions().shouldLogHeaders()) {
			Map<String, String> extractedHeaders = extractHeadersToLog(request.getHeaders().asSimpleMap(),
					config.getRequestLogOptions().getHeadersToInclude(),
					config.getRequestLogOptions().getHeadersToExclude());

			Map<String, String> headersToLog = filterSensitiveHeaders(extractedHeaders,
					config.getMaskSensitiveHeaders());

			logger.log(level, "Request Headers {}", headersToLog);
		}

		if (config.getRequestLogOptions().shouldLogBody()) {
			if (request.getBody() != null) {
				logger.log(level, "Request Body {}", request.getBody());
			} else if (request.getParameters() != null && !request.getParameters().isEmpty()) {
				logger.log(level, "Request Body {}", request.getParameters());
			}
		}
	}

	/**
	 * Log Responses.
	 * 
	 * @param response HttpResponse to be logged.
	 */
	public void logResponse(Response response) {
		Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

		String contentLength = response.getHeaders().value("content-length");
		String contentType = response.getHeaders().value("content-type") != null
				? response.getHeaders().value("content-type")
				: "";

		if (contentLength == null) {
			logger.log(level, "Response {} {}", response.getStatusCode(), contentType);
		} else {
			logger.log(level, "Response {} {} content-length: {}", response.getStatusCode(), contentType,
					contentLength);
		}

		if (config.getResponseLogOptions().shouldLogHeaders()) {
			Map<String, String> extractedHeaders = extractHeadersToLog(response.getHeaders().asSimpleMap(),
					config.getResponseLogOptions().getHeadersToInclude(),
					config.getResponseLogOptions().getHeadersToExclude());

			Map<String, String> headersToLog = filterSensitiveHeaders(extractedHeaders,
					config.getMaskSensitiveHeaders());

			logger.log(level, "Response Headers {}", headersToLog);
		}

		if (config.getResponseLogOptions().shouldLogBody()) {
			logger.log(level, "Response Body {}", response.getBody());
		}
	}

	/**
	 * Extracts headers to log based on inclusion and exclusion criteria.
	 * 
	 * @param headers          The map of headers.
	 * @param headersToInclude The set of headers to include.
	 * @param headersToExclude The set of headers to exclude.
	 * @return The extracted headers to log.
	 */
	public Map<String, String> extractHeadersToLog(Map<String, String> headers, List<String> headersToInclude,
			List<String> headersToExclude) {
		if (!headersToInclude.isEmpty()) {
			return extractIncludedHeaders(headers, headersToInclude);
		}

		if (!headersToExclude.isEmpty()) {
			return extractExcludedHeaders(headers, headersToExclude);
		}

		return headers;
	}

	/**
	 * Filter sensitive headers from the given list of request headers.
	 *
	 * @param requestHeaders       The list of headers to filter.
	 * @param maskSensitiveHeaders Whether to mask sensitive headers or not.
	 * @return A map containing filtered headers.
	 */
	public static Map<String, String> filterSensitiveHeaders(Map<String, String> headers,
			boolean maskSensitiveHeaders) {
		if (maskSensitiveHeaders) {
			Map<String, String> filteredHeaders = new HashMap<>();
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey().toLowerCase();
				if (sensitiveHeaders.contains(key)) {
					filteredHeaders.put(entry.getKey(), "**Redacted**");
				} else {
					filteredHeaders.put(entry.getKey(), entry.getValue());
				}
			}
			return filteredHeaders;
		}

		return headers;
	}

	/**
	 * Extracts headers to log based on inclusion criteria.
	 * 
	 * @param headers          The map of headers.
	 * @param headersToInclude The set of headers to include.
	 * @return The extracted headers to log.
	 */
	private Map<String, String> extractIncludedHeaders(Map<String, String> headers, List<String> headersToInclude) {
		Map<String, String> extractedHeaders = new HashMap<>();
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (headersToInclude.contains(entry.getKey().toLowerCase())) {
				extractedHeaders.put(entry.getKey(), entry.getValue());
			}
		}
		return extractedHeaders;
	}

	/**
	 * Extracts headers to log based on exclusion criteria.
	 * 
	 * @param headers          The map of headers.
	 * @param headersToExclude The set of headers to exclude.
	 * @return The extracted headers to log.
	 */
	private Map<String, String> extractExcludedHeaders(Map<String, String> headers, List<String> headersToExclude) {
		Map<String, String> extractedHeaders = new HashMap<>();
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (!headersToExclude.contains(entry.getKey().toLowerCase())) {
				extractedHeaders.put(entry.getKey(), entry.getValue());
			}
		}
		return extractedHeaders;
	}
}
