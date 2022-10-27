package io.apimatic.core.types;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.Context;

/**
 * This is the base class for all exceptions that represent an error response from the server.
 */
public class CoreApiException extends Exception {
    /**
     * UID for serialization
     */
    private static final long serialVersionUID = 6424174253911720338L;

    /**
     * An instance of {@link Context}
     */
    private Context httpContext;

    /**
     * Initialization constructor.
     * @param reason The reason for throwing exception
     */
    public CoreApiException(final String reason) {
        super(reason);
    }

    /**
     * Initialization constructor.
     * @param reason The reason for throwing exception
     * @param context The http context of the API exception
     */
    public CoreApiException(final String reason, final Context context) {
        super(reason);
        this.httpContext = context;

        // if a derived exception class is used, then perform deserialization of response body
        if ((context == null) || (context.getResponse() == null)
                || (context.getResponse().getRawBody() == null)) {
            return;
        }

        try {
            // Can throw IOException if input has invalid content type.
            JsonNode jsonNode = CoreHelper.mapper.readTree(context.getResponse().getRawBody());
            if (!getClass().equals(CoreApiException.class)) {
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
     * @return Returns the response code for ApiException
     */
    public int getResponseCode() {
        return (httpContext != null) ? httpContext.getResponse().getStatusCode() : -1;
    }

    /**
     * The HTTP response body from the API request.
     * @return Returns the object of HttpContext for ApiException
     */
    public Context getHttpContext() {
        return httpContext;
    }
}
