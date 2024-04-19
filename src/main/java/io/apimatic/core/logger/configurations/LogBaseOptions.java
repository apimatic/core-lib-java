package io.apimatic.core.logger.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents base options for logging.
 */
public abstract class LogBaseOptions {
    private boolean logBody = false;
    private boolean logHeaders = false;
    private List<String> headersToInclude = new ArrayList<>();
    private List<String> headersToExclude = new ArrayList<>();
    private List<String> headersToUnmask = new ArrayList<>();

    /**
     * Checks if logging of request body is enabled.
     * @return True if logging of request body is enabled, otherwise false.
     */
    public boolean shouldLogBody() {
        return logBody;
    }

    /**
     * Sets whether to log the request body.
     * @param logBody True to enable logging of request body, otherwise false.
     */
    public void setLogBody(boolean logBody) {
        this.logBody = logBody;
    }

    /**
     * Checks if logging of request headers is enabled.
     * @return True if logging of request headers is enabled, otherwise false.
     */
    public boolean shouldLogHeaders() {
        return logHeaders;
    }

    /**
     * Sets whether to log the request headers.
     * @param logHeaders True to enable logging of request headers, otherwise false.
     */
    public void setLogHeaders(boolean logHeaders) {
        this.logHeaders = logHeaders;
    }

    /**
     * Gets the list of headers to include in logging.
     * @return An unmodifiable list of headers to include.
     */
    public List<String> getHeadersToInclude() {
        return Collections.unmodifiableList(headersToInclude);
    }

    /**
     * Gets the list of headers to exclude from logging.
     * @return An unmodifiable list of headers to exclude.
     */
    public List<String> getHeadersToExclude() {
        return Collections.unmodifiableList(headersToExclude);
    }

    /**
     * Retrieves the list of headers to unmask from sensitive headers. These
     * headers are excluded from masking.
     * @return An unmodifiable list of headers to unmasked.
     */
    public List<String> getHeadersToUnmask() {
        return Collections.unmodifiableList(headersToUnmask);
    }

    /**
     * Excludes specified headers from logging.
     * @param excludeHeaders The headers to exclude from logging.
     */
    public void excludeHeaders(String... excludeHeaders) {
        headersToExclude = new ArrayList<>(Arrays.asList(excludeHeaders));
    }

    /**
     * Includes specified headers in logging.
     * @param includeHeaders The headers to include in logging.
     */
    public void includeHeaders(String... includeHeaders) {
        headersToInclude = new ArrayList<>(Arrays.asList(includeHeaders));
    }

    /**
     * Unmask specified headers from logging.
     * @param unmaskHeaders The headers to unmask in logging.
     */
    public void unmaskHeaders(String... unmaskHeaders) {
        this.headersToUnmask = new ArrayList<>(Arrays.asList(unmaskHeaders));
    }
}
