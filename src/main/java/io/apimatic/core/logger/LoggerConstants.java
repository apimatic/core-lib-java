package io.apimatic.core.logger;

/**
 * Class defining constants related to logging. These constants are used for
 * logging various aspects of HTTP requests and responses.
 */
public final class LoggerConstants {
    /**
     * Key representing the method of the HTTP request.
     */
    public static final String METHOD = "method";

    /**
     * Key representing the URL of the HTTP request.
     */
    public static final String URL = "url";

    /**
     * Key representing the query parameters of the HTTP request.
     */
    public static final String QUERY_PARAMETER = "queryParameter";

    /**
     * Key representing the headers of the HTTP request or response.
     */
    public static final String HEADERS = "headers";

    /**
     * Key representing the body of the HTTP request or response.
     */
    public static final String BODY = "body";

    /**
     * Key representing the status code of the HTTP response.
     */
    public static final String STATUS_CODE = "statusCode";

    /**
     * Key representing the content length of the HTTP response.
     */
    public static final String CONTENT_LENGTH = "contentLength";

    /**
     * Key representing the content type of the HTTP response.
     */
    public static final String CONTENT_TYPE = "contentType";

    /**
     * Key representing the content length header.
     */
    public static final String CONTENT_LENGTH_HEADER = "content-length";

    /**
     * Key representing the content type header.
     */
    public static final String CONTENT_TYPE_HEADER = "content-type";

    private LoggerConstants() {}
}
