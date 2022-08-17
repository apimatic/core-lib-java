package io.apimatic.core_lib.types;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

import io.apimatic.core_interfaces.http.CoreHttpContext;
import io.apimatic.core_interfaces.type.CoreApiException;
import io.apimatic.core_lib.utilities.CoreHelper;

/**
 * This is the base class for all exceptions that represent an error response from the server.
 */
public class ApiException extends Exception implements CoreApiException {
    //UID for serialization
    private static final long serialVersionUID = 6424174253911720338L;

    //private fields
    private CoreHttpContext httpContext;

    /**
     * Initialization constructor.
     * @param reason The reason for throwing exception
     */
    public ApiException(String reason) {
        super(reason);
    }

    /**
     * Initialization constructor.
     * @param   reason  The reason for throwing exception
     * @param   context The http context of the API exception
     */
    public ApiException(String reason, CoreHttpContext context) {
        super(reason);
        this.httpContext = context;

        //if a derived exception class is used, then perform deserialization of response body
        if ((context == null) || (context.getResponse() == null)
            || (context.getResponse().getRawBody() == null)) {
            return;
        }

        try {
            // Can throw IOException if input has invalid content type.
            JsonNode jsonNode = CoreHelper.mapper.readTree(context.getResponse().getRawBody());
            if (!getClass().equals(ApiException.class)) {
                // In case of IOException JsonNode cannot be detected.
                CoreHelper.mapper.readerForUpdating(this).readValue(jsonNode);
            }
        } catch (IOException ioException) { 
            // Can throw exception while object mapper tries to:
            // Deserialize the content as JSON tree.
            // Convert results from JSON tree into given value type.
        }
    }

    /**
     * The HTTP response code from the API request.
     * @return   Returns the response code for ApiException
     */
	public int getResponseCode() {
        return (httpContext != null) ? httpContext.getResponse().getStatusCode() : -1;
    }

    /**
     * The HTTP response body from the API request.
     * @return   Returns the object of HttpContext for ApiException
     */
	public CoreHttpContext getHttpContext() {
        return httpContext;
    }
}