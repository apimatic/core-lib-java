package io.apimatic.core.logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            "Transfer-Encoding", "Upgrade", "User-Agent", "Vary", "Via", "Warning",
            "X-Forwarded-For", "X-Requested-With", "X-Powered-By");

    /**
     * Private constructor to prevent instantiation
     */
    private LoggerUtilities() {
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
     * @param headersToWhiteList   The list of headers to white list from masking.
     * @param maskSensitiveHeaders Whether to mask sensitive headers or not.
     * @return A map containing filtered headers.
     */
    public static Map<String, String> filterSensitiveHeaders(Map<String, String> headers,
            List<String> headersToWhiteList, boolean maskSensitiveHeaders) {
        if (maskSensitiveHeaders) {
            Map<String, String> filteredHeaders = new HashMap<>();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey().toLowerCase();
                if (NON_SENSITIVE_HEADERS.contains(key) || headersToWhiteList.contains(key)) {
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
