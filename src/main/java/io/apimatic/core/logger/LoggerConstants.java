package io.apimatic.core.logger;

/**
 * Interface defining constants related to logging. These constants are used for
 * logging various aspects of HTTP requests and responses.
 */
public interface LoggerConstants {
    /**
     * Key representing the method of the HTTP request.
     */
    String METHOD = "method";

    /**
     * Key representing the URL of the HTTP request.
     */
    String URL = "url";

    /**
     * Key representing the content type of the HTTP request or response.
     */
    String CONTENT_TYPE = "content-type";

    /**
     * Key representing the query parameters of the HTTP request.
     */
    String QUERY_PARAMETER = "queryParameter";

    /**
     * Key representing the headers of the HTTP request or response.
     */
    String HEADERS = "headers";

    /**
     * Key representing the body of the HTTP request or response.
     */
    String BODY = "body";

    /**
     * Key representing the content length of the HTTP request or response.
     */
    String CONTENT_LENGTH = "content-length";

    /**
     * Key representing the status code of the HTTP response.
     */
    String STATUS_CODE = "statuscode";
}