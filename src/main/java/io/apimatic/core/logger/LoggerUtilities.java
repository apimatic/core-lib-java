package io.apimatic.core.logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.apimatic.coreinterfaces.logger.configuration.LoggingOptions;

public final class LoggerUtilities {
    /**
     * List of sensitive headers that need to be filtered.
     */
    private static final List<String> NON_SENSITIVE_HEADERS = Arrays.asList("Accept",
            "Accept-Charset", "Accept-Encoding", "Accept-Language", "Access-Control-Allow-Origin",
            "Cache-Control", "Connection", "Content-Encoding", "Content-Language", "Content-Length",
            "Content-Location", "Content-MD5", "Content-Range", "Content-Type", "Date", "ETag",
            "Expect", "Expires", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match",
            "If-Range", "If-Unmodified-Since", "Keep-Alive", "Last-Modified", "Location",
            "Max-Forwards", "Pragma", "Range", "Referer", "Retry-After", "Server", "Trailer",
            "Transfer-Encoding", "Upgrade", "User-Agent", "Vary", "Via", "Warning").stream()
            .map(String::toLowerCase).collect(Collectors.toList());;

    /**
     * Private constructor to prevent instantiation
     */
    private LoggerUtilities() {
    }

    /**
     * Retrieves the headers to be logged based on the provided logging options,
     * headers, and sensitivity masking configuration.
     * @param loggingOptions       The logging options containing configurations for
     *                             header logging.
     * @param headers              The headers to be evaluated for logging.
     * @param maskSensitiveHeaders Determines whether sensitive headers should be
     *                             masked in the log.
     * @return A map containing the headers to be logged, considering the provided
     *         options and sensitivity masking.
     */
    public static Map<String, String> getHeadersToLog(LoggingOptions loggingOptions,
            Map<String, String> headers, boolean maskSensitiveHeaders) {
        Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(headers,
                loggingOptions.getHeadersToInclude(), loggingOptions.getHeadersToExclude());

        Map<String, String> headersToLog = LoggerUtilities.filterSensitiveHeaders(extractedHeaders,
                loggingOptions.getHeadersToUnmask(), maskSensitiveHeaders);

        return headersToLog;
    }

    /**
     * Extracts headers to log based on inclusion and exclusion criteria.
     *
     * @param headers          The map of headers.
     * @param headersToInclude The set of headers to include.
     * @param headersToExclude The set of headers to exclude.
     * @return The extracted headers to log.
     */
    public static Map<String, String> extractHeadersToLog(Map<String, String> headers,
            List<String> headersToInclude, List<String> headersToExclude) {
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
     * @param headers              The list of headers to filter.
     * @param headersToUnmask      The list of headers to unmask.
     * @param maskSensitiveHeaders Whether to mask sensitive headers or not.
     * @return A map containing filtered headers.
     */
    public static Map<String, String> filterSensitiveHeaders(Map<String, String> headers,
            List<String> headersToUnmask, boolean maskSensitiveHeaders) {
        if (maskSensitiveHeaders) {
            Map<String, String> filteredHeaders = new HashMap<>();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey().toLowerCase();
                if (NON_SENSITIVE_HEADERS.contains(key) || headersToUnmask.contains(key)) {
                    filteredHeaders.put(entry.getKey(), entry.getValue());
                } else {
                    filteredHeaders.put(entry.getKey(), "**Redacted**");
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
    public static Map<String, String> extractIncludedHeaders(Map<String, String> headers,
            List<String> headersToInclude) {
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
    public static Map<String, String> extractExcludedHeaders(Map<String, String> headers,
            List<String> headersToExclude) {
        Map<String, String> extractedHeaders = new HashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (!headersToExclude.contains(entry.getKey().toLowerCase())) {
                extractedHeaders.put(entry.getKey(), entry.getValue());
            }
        }
        return extractedHeaders;
    }
}
