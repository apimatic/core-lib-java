package io.apimatic.core.logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggerUtilities {
	/**
	 * List of sensitive headers that need to be filtered.
	 */
	private static final List<String> sensitiveHeaders = Arrays.asList("authorization", "www-authenticate",
			"proxy-authorization", "set-cookie");
	
	/**
	 * Extracts headers to log based on inclusion and exclusion criteria.
	 * 
	 * @param headers          The map of headers.
	 * @param headersToInclude The set of headers to include.
	 * @param headersToExclude The set of headers to exclude.
	 * @return The extracted headers to log.
	 */
	public static Map<String, String> extractHeadersToLog(Map<String, String> headers, List<String> headersToInclude,
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
	 * @param headers       The list of headers to filter.
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
	public static Map<String, String> extractIncludedHeaders(Map<String, String> headers, List<String> headersToInclude) {
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
	public static Map<String, String> extractExcludedHeaders(Map<String, String> headers, List<String> headersToExclude) {
		Map<String, String> extractedHeaders = new HashMap<>();
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (!headersToExclude.contains(entry.getKey().toLowerCase())) {
				extractedHeaders.put(entry.getKey(), entry.getValue());
			}
		}
		return extractedHeaders;
	}
}
