package io.apimatic.core.configurations.http.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingPolicy;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;

/**
 * To hold logging configuration.
 */
public final class ApiLoggingConfiguration implements ReadonlyLogging {

    /**
     * Level enum to use with level in {@link ApiLoggingConfiguration.Builder}.
     */
    public enum Level {
        /**
         * To log with logging level 'INFO'.
         */
        INFO,

        /**
         * To log with logging level 'Error'.
         */
        ERROR,

        /**
         * To log with logging level 'WARN'.
         */
        WARN,

        /**
         * To log with logging level 'DEBUG'.
         */
        DEBUG,

        /**
         * To log with logging level 'TRACE'.
         */
        TRACE
    }

    /**
     * HeaderLoggingPolicy enum to use with headerLoggingPolicy in
     * {@link ApiLoggingConfiguration.Builder}. It is either used to exclude or include headers
     * provided in header filters, for various security reasons.
     */
    public enum HeaderLoggingPolicy {
        /**
         * Policy that only include the headerFilters as headers. So all the headers that are not
         * provided in header filters will be excluded.
         */
        INCLUDE,

        /**
         * Policy that only exclude the headerFilters from headers. So all the headers that are not
         * provided in header filters will be included.
         */
        EXCLUDE
    }

    /**
     * A boolean variable to log the request info
     */
    private boolean logRequestInfo;

    /**
     * A boolean variable to log the Response info
     */
    private boolean logResponseInfo;

    /**
     * A boolean variable to log request headers
     */
    private boolean logRequestHeaders;

    /**
     * A boolean variable to log response headers
     */
    private boolean logResponseHeaders;

    /**
     * A boolean variable to log request body
     */
    private boolean logRequestBody;

    /**
     * A boolean variable to log the response body
     */
    private boolean logResponseBody;

    /**
     * A boolean variable for pretty printing
     */
    private boolean prettyPrintLogs;

    /**
     * An instance of {@link LoggingLevel}
     */
    private LoggingLevel level;

    /**
     * An instance of {@link LoggingPolicy}
     */
    private LoggingPolicy headerLoggingPolicy;

    /**
     * A set of header filters
     */
    private Set<String> headerFilters;

    /**
     * @param logRequestInfo
     * @param logResponseInfo
     * @param logRequestHeaders
     * @param logResponseHeaders
     * @param logRequestBody
     * @param logResponseBody
     * @param prettyPrintLogs
     * @param level
     * @param headerLoggingPolicy
     * @param headerFilters
     */
    private ApiLoggingConfiguration(boolean logRequestInfo, boolean logResponseInfo,
            boolean logRequestHeaders, boolean logResponseHeaders, boolean logRequestBody,
            boolean logResponseBody, boolean prettyPrintLogs, LoggingLevel level,
            LoggingPolicy headerLoggingPolicy, TreeSet<String> headerFilters) {
        this.logRequestInfo = logRequestInfo;
        this.logResponseInfo = logResponseInfo;
        this.logRequestHeaders = logRequestHeaders;
        this.logResponseHeaders = logResponseHeaders;
        this.logRequestBody = logRequestBody;
        this.logResponseBody = logResponseBody;
        this.prettyPrintLogs = prettyPrintLogs;
        this.level = level;
        this.headerLoggingPolicy = headerLoggingPolicy;
        this.headerFilters = Collections.unmodifiableSet(headerFilters);
    }

    /**
     * Is logging request info enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isLoggingRequestInfo() {
        return logRequestInfo;
    }

    /**
     * Is logging response info enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isLoggingResponseInfo() {
        return logResponseInfo;
    }

    /**
     * Is logging request headers enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isLoggingRequestHeaders() {
        return logRequestHeaders;
    }

    /**
     * Is logging response headers enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isLoggingResponseHeaders() {
        return logResponseHeaders;
    }

    /**
     * Is logging request body enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isLoggingRequestBody() {
        return logRequestBody;
    }

    /**
     * Is logging response body enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isLoggingResponseBody() {
        return logResponseBody;
    }

    /**
     * Is pretty printing log message enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isPrettyPrinting() {
        return prettyPrintLogs;
    }

    /**
     * Getter for level.
     * @return Level of logging.
     */
    public LoggingLevel getLevel() {
        return level;
    }

    /**
     * Getter for header logging policy.
     * @return Logging policy for headers.
     */
    public LoggingPolicy getHeaderLoggingPolicy() {
        return headerLoggingPolicy;
    }

    /**
     * Getter for headers' filters.
     * @return Set of string headers to filter.
     */
    public Set<String> getHeaderFilters() {
        return headerFilters;
    }

    /**
     * Converts this LoggingConfiguration into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "LoggingConfiguration [logRequestInfo=" + logRequestInfo + ", logResponseInfo="
                + logResponseInfo + ", logRequestHeaders=" + logRequestHeaders
                + ", logResponseHeaders=" + logResponseHeaders + ", logRequestBody="
                + logRequestBody + ", logResponseBody=" + logResponseBody + ", prettyPrintLogs="
                + prettyPrintLogs + ", level=" + level + ", headerLoggingPolicy="
                + headerLoggingPolicy + ", headerFilters=" + headerFilters + "]";
    }

    /**
     * Builds a new {@link ApiLoggingConfiguration.Builder} object. Creates the instance with the
     * current state.
     * @return a new {@link ApiLoggingConfiguration.Builder} object
     */
    public Builder newBuilder() {
        return new Builder().logRequestInfo(logRequestInfo).logRequestHeaders(logRequestHeaders)
                .logRequestBody(logRequestBody).logResponseInfo(logResponseInfo)
                .logResponseHeaders(logResponseHeaders).logResponseBody(logResponseBody)
                .prettyPrintLogs(prettyPrintLogs).level(level)
                .headerLoggingPolicy(headerLoggingPolicy).headerFilters(headerFilters);
    }

    /**
     * Class to build instances of {@link ApiLoggingConfiguration}.
     */
    public static class Builder {

        /**
         * A boolean variable to log the request info
         */
        private boolean logRequestInfo;

        /**
         * A boolean variable to log the Response info
         */
        private boolean logResponseInfo;

        /**
         * A boolean variable to log request headers
         */
        private boolean logRequestHeaders;

        /**
         * A boolean variable to log response headers
         */
        private boolean logResponseHeaders;

        /**
         * A boolean variable to log request body
         */
        private boolean logRequestBody;

        /**
         * A boolean variable to log the response body
         */
        private boolean logResponseBody;

        /**
         * A boolean variable for pretty printing
         */
        private boolean prettyPrintLogs;

        /**
         * An instance of {@link LoggingLevel}
         */
        private LoggingLevel level = LoggingLevel.INFO;

        /**
         * An instance of {@link LoggingPolicy}
         */
        private LoggingPolicy headerLoggingPolicy = LoggingPolicy.EXCLUDE;

        /**
         * A set of header filters
         */
        private TreeSet<String> headerFilters = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

        /**
         * Default Constructor to initiate builder with default properties.
         */
        public Builder() {
            // Adding default header filters, which will be
            // excluded/included from/into headers logging.
            headerFilters.addAll(Arrays.asList("authorization"));
        }

        /**
         * Enable or disable logging of request info. Request info contains "url" and "method"
         * @param logRequestInfo Whether to enable logging.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logRequestInfo(boolean logRequestInfo) {
            this.logRequestInfo = logRequestInfo;
            return this;
        }

        /**
         * Enable or disable logging of response info. Response info contains "statusCode", "url",
         * and "timeTaken"
         * @param logResponseInfo Whether to enable logging.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logResponseInfo(boolean logResponseInfo) {
            this.logResponseInfo = logResponseInfo;
            return this;
        }

        /**
         * Enable or disable logging of headers for requests.
         * @param logRequestHeaders Whether to enable logging.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logRequestHeaders(boolean logRequestHeaders) {
            this.logRequestHeaders = logRequestHeaders;
            return this;
        }

        /**
         * Enable or disable logging of headers for responses.
         * @param logResponseHeaders Whether to enable logging.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logResponseHeaders(boolean logResponseHeaders) {
            this.logResponseHeaders = logResponseHeaders;
            return this;
        }

        /**
         * Enable or disable logging of request body or form parameters.
         * @param logRequestBody Whether to enable logging.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logRequestBody(boolean logRequestBody) {
            this.logRequestBody = logRequestBody;
            return this;
        }

        /**
         * Enable or disable logging of response body.
         * @param logResponseBody Whether to enable logging.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logResponseBody(boolean logResponseBody) {
            this.logResponseBody = logResponseBody;
            return this;
        }

        /**
         * Enable all logs.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logEverything() {
            logRequestInfo = true;
            logResponseInfo = true;
            logRequestHeaders = true;
            logResponseHeaders = true;
            logRequestBody = true;
            logResponseBody = true;
            return this;
        }

        /**
         * Disable all logs.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder logNothing() {
            logRequestInfo = false;
            logResponseInfo = false;
            logRequestHeaders = false;
            logResponseHeaders = false;
            logRequestBody = false;
            logResponseBody = false;
            return this;
        }

        /**
         * Enable or disable pretty printing for logging.
         * @param prettyPrintLogs Boolean value to enable or disable.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder prettyPrintLogs(boolean prettyPrintLogs) {
            this.prettyPrintLogs = prettyPrintLogs;
            return this;
        }

        /**
         * Set level for logging.
         * @param level specify level of all logs.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder level(LoggingLevel level) {
            this.level = level;
            return this;
        }

        /**
         * Set logging policy for headers.
         * @param headerLoggingPolicy specify logging policy for headers.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder headerLoggingPolicy(LoggingPolicy headerLoggingPolicy) {
            this.headerLoggingPolicy = headerLoggingPolicy;
            return this;
        }

        /**
         * Copy all from given collection to replace and set filters for headers.
         * @param headerFilters Set of string headers.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder headerFilters(Set<String> headerFilters) {
            this.headerFilters.clear();
            this.headerFilters.addAll(headerFilters);
            return this;
        }

        /**
         * Clear all filters for headers.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder clearHeaderFilter() {
            headerFilters.clear();
            return this;
        }

        /**
         * Add given header as a filter for logging headers.
         * @param header String header to be added as filter.
         * @return {@link ApiLoggingConfiguration.Builder}
         */
        public Builder addHeaderFilter(String header) {
            headerFilters.add(header);
            return this;
        }

        /**
         * Builds a new LoggingConfiguration object using the set fields.
         * @return {@link ApiLoggingConfiguration}
         */
        public ApiLoggingConfiguration build() {
            return new ApiLoggingConfiguration(logRequestInfo, logResponseInfo, logRequestHeaders,
                    logResponseHeaders, logRequestBody, logResponseBody, prettyPrintLogs, level,
                    headerLoggingPolicy, headerFilters);
        }
    }
}
