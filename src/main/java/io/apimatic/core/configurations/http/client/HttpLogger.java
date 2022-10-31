package io.apimatic.core.configurations.http.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.apimatic.core.types.http.request.MultipartFileWrapper;
import io.apimatic.core.types.http.request.MultipartWrapper;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.HeaderLoggingPolicyLevel;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingLevelType;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;
import io.apimatic.coreinterfaces.type.CoreFileWrapper;

public class HttpLogger implements ApiLogger {

    /**
     * A request queue
     */
    private final ConcurrentHashMap<Request, RequestEntry> requestQueue;

    /**
     * An instance of {@link Logger}
     */
    private Logger logger;

    /**
     * An instance of {@link ReadonlyLogging}
     */
    private ReadonlyLogging config;

    /**
     * An instance of {@link ObjectWriter}
     */
    private ObjectWriter writer;

    /**
     * Default Constructor.
     * @param logger Logger instance for logging.
     * @param config {@link ReadonlyLogging} as logging properties.
     */
    public HttpLogger(final Logger logger, final ReadonlyLogging config) {
        this.requestQueue = new ConcurrentHashMap<Request, RequestEntry>();
        this.logger = logger;
        this.config = config;
        @SuppressWarnings("serial")
        ObjectMapper mapper = new ObjectMapper(CoreHelper.mapper) {};
        mapper.addMixIn(CoreFileWrapper.class, LoggingMixIn.class);
        mapper.addMixIn(MultipartWrapper.class, LoggingMixIn.class);
        mapper.addMixIn(MultipartFileWrapper.class, LoggingMixIn.class);
        mapper.addMixIn(HttpHeaders.class, LoggingMixIn.class);
        this.writer =
                !config.isPrettyPrinting() ? mapper.writer()
                        : mapper.writerWithDefaultPrettyPrinter();
    }

    /**
     * Log requests.
     * @param request HttpRequest to be logged.
     * @param url String request URL.
     */
    public void logRequest(Request request, String url) {
        this.logRequest(request, url, null);
    }

    /**
     * Log requests.
     * @param request HttpRequest to be logged.
     * @param url String request URL.
     * @param additionalMessage Any additional message to be logged.
     */
    public void logRequest(Request request, String url, String additionalMessage) {
        if (request == null) {
            return;
        }


        String requestId = UUID.randomUUID().toString();
        RequestEntry requestEntry = new RequestEntry(requestId, System.nanoTime(), url);
        requestQueue.put(request, requestEntry);

        if (!config.isLoggingRequestInfo() && !config.isLoggingRequestHeaders()
                && !config.isLoggingRequestBody()) {
            // if log request disabled
            return;
        }

        RequestMessage message = new RequestMessage();
        message.setType("Request");
        message.setRequestId(requestId);
        if (config.isLoggingRequestInfo()) {
            message.method = (Method) request.getHttpMethod();
            message.setUrl(url);
            message.setAdditionalMessage(additionalMessage);
        }

        if (config.isLoggingRequestHeaders()) {
            message.setHeaders(getFilteredHeaders((HttpHeaders) request.getHeaders()));
        }

        if (config.isLoggingRequestBody()) {
            if (request.getBody() != null) {
                // As request.getBody() is always a non null serialized string.
                // Hence we are calling getBody().toString().
                message.setBody(CoreHelper.deserializeAsObject(request.getBody().toString()));
            } else if (request.getParameters() != null && !request.getParameters().isEmpty()) {
                message.setBody(request.getParameters());
            }
        }

        log(message, config.getLevel(), true);
    }

    /**
     * Set error for failed requests.
     * @param request HttpRequest that failed.
     * @param error Throwable occurred.
     */
    public void setError(Request request, Throwable error) {
        RequestEntry requestEntry = requestQueue.get(request);
        if (requestEntry != null) {
            requestEntry.error = error;
            requestQueue.put(request, requestEntry);
        }
    }

    /**
     * Log Responses.
     * @param request HttpRequest that completed.
     * @param response HttpResponse to be logged.
     */
    public void logResponse(Request request, Response response) {
        this.logResponse(request, response, null);
    }

    /**
     * Log Responses.
     * @param request HttpRequest that completed.
     * @param response HttpResponse to be logged.
     * @param additionalMessage Any additional message to be logged.
     */
    public void logResponse(Request request, Response response, String additionalMessage) {
        RequestEntry requestEntry = requestQueue.get(request);
        if (requestEntry == null) {
            return;
        }
        requestQueue.remove(request);
        if (!config.isLoggingResponseInfo() && !config.isLoggingResponseHeaders()
                && !config.isLoggingResponseBody()) {
            // if log response disabled
            return;
        }
        ResponseMessage message = new ResponseMessage();
        message.setType("Response");
        message.setRequestId(requestEntry.requestId);
        long timeTaken = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - requestEntry.startTime);
        if (response == null) {
            message.success = false;
            message.failureReason = "HTTP REQUEST FAILED: " + requestEntry.error;
            message.setUrl(requestEntry.url);
            message.timeTakenMillis = timeTaken;
            message.setAdditionalMessage(additionalMessage);
        } else {
            if (config.isLoggingResponseInfo()) {
                message.statusCode = response.getStatusCode();
                message.setUrl(requestEntry.url);
                message.timeTakenMillis = timeTaken;
                message.setAdditionalMessage(additionalMessage);
            }

            if (config.isLoggingResponseHeaders()) {
                message.setHeaders(getFilteredHeaders((HttpHeaders) response.getHeaders()));
            }

            if (config.isLoggingResponseBody()) {
                message.setBody(CoreHelper.deserializeAsObject(response.getBody()));
            }
        }

        log(message, config.getLevel(), true);
    }

    private Map<String, List<String>> getFilteredHeaders(HttpHeaders headers) {
        Map<String, List<String>> filteredHeders = headers.asMultimap();
        headers.names().forEach(name -> {
            boolean isNameInFilter = config.getHeaderFilters().contains(name);
            switch (HeaderLoggingPolicyLevel.valueOf(config.getHeaderLoggingPolicy().toString())) {
                case EXCLUDE:
                    if (isNameInFilter) {
                        filteredHeders.remove(name);
                    }
                    break;
                case INCLUDE:
                    if (!isNameInFilter) {
                        filteredHeders.remove(name);
                    }
                    break;
                default:
                    break;
            }
        });
        return filteredHeders.isEmpty() ? null : filteredHeders;
    }

    /**
     * Log provided message according to logging level.
     * @param message Message instance to be logged as JSON.
     * @param level To provide the LoggingLevelType conversion
     * @param logException Need to log the exception?
     */
    private void log(Message message, LoggingLevel level, boolean logException) {
        try {
            String structuredMessage = writer.writeValueAsString(message);

            switch (LoggingLevelType.valueOf(level.toString())) {
                case ERROR:
                    logger.error(structuredMessage);
                    break;
                case WARN:
                    logger.warn(structuredMessage);
                    break;
                case INFO:
                    logger.info(structuredMessage);
                    break;
                case DEBUG:
                    logger.debug(structuredMessage);
                    break;
                case TRACE:
                    logger.trace(structuredMessage);
                    break;
                default:
                    break;
            }
        } catch (JsonProcessingException e) {
            if (logException) {
                // Log as error if an exception occurs while serialization
                // i.e. converting body of message into a JSON string.
                message.body = "Unable to log body: " + e.toString();
                log(message, LoggingLevel.ERROR, false);
            }
        }
    }

    /**
     * MixIn interface to update how certain fields are shown in the logs.
     */
    private interface LoggingMixIn {

        /**
         * @return A header string
         */
        @JsonUnwrapped
        String getHeaders();

        /**
         * @return Header string
         */
        @JsonGetter("headers")
        String asMultimap();

        /**
         * @return File string
         */
        @JsonIgnore
        String getFile();

        @JsonGetter("file")
        String getLoggableFile();

        @JsonIgnore
        String getByteArray();

        @JsonGetter("object")
        String getLoggableObject();
    }

    /**
     * Class to hold and write request message.
     */
    private class RequestMessage extends Message {
        /**
         * An instance of {@link Method}
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private Method method;
    }

    /**
     * Class to hold and write response message.
     */
    private class ResponseMessage extends Message {
        /**
         * A success boolean variable
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private boolean success = true;
        /**
         * A failure reason string
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private String failureReason;
        /**
         * A status code integer
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private Integer statusCode;
        /**
         * A timeTakenInMilis long variable
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private Long timeTakenMillis;
    }

    /**
     * Base class to hold and write message.
     */
    private class Message {
        /**
         * A logging error string
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private String loggingError;

        /**
         * A string of message type
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private String type;

        /**
         * A string of requestId message
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private String requestId;

        /**
         * A string of url
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private String url;

        /**
         * A map for headers values
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private Map<String, List<String>> headers;

        /**
         * A body object
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private Object body;

        /**
         * A string additional message
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty
        private String additionalMessage;

        /**
         * @param type Message type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @param requestId Message RequestId
         */
        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        /**
         * @param url Message Url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @param headers Message headers
         */
        public void setHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
        }

        /**
         * @param body Message body
         */
        public void setBody(Object body) {
            this.body = body;
        }

        /**
         * @param additionalMessage Additional Message
         */
        public void setAdditionalMessage(String additionalMessage) {
            this.additionalMessage = additionalMessage;
        }
    }

    /**
     * Class to hold the request info until request completes.
     */
    private class RequestEntry {
        /**
         * A requestId String
         */
        private String requestId;

        /**
         * A start time
         */
        private long startTime;

        /**
         * A request url
         */
        private String url;
        /**
         * A throwable error
         */
        private Throwable error;

        /**
         * Default Constructor.
         * @param requestId String id assigned to the request.
         * @param startTime long start time of the request.
         * @param url String request URI.
         */
        RequestEntry(final String requestId, long startTime, final String url) {
            this.requestId = requestId;
            this.startTime = startTime;
            this.url = url;
        }
    }
}
