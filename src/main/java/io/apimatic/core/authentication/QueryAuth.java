package io.apimatic.core.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * QueryAuth is an implementation of {@link Authentication} that supports HTTP authentication
 * through Query parameters.
 */
public class QueryAuth implements Authentication {

    /**
     * A map of authentication parameters.
     */
    private Map<String, String> authParams = new HashMap<>();

    /**
     * @param authParams Map of authentication parameters.
     */
    public QueryAuth(final Map<String, String> authParams) {
        this.authParams = authParams;
    }

    /**
     * Apply the Query authentication.
     * @param httpRequest A httpRequest
     */
    @Override
    public Request apply(Request httpRequest) {
        authParams.forEach((key, value) -> {
            httpRequest.addQueryParameter(key, value);
        });
        return httpRequest;
    }

    /**
     * Validate the query authentication.
     */
    @Override
    public void validate() {
        authParams.forEach((key, value) -> {
            if (key == null || value == null) {
                throw new IllegalStateException("Auth key or value cannot be null.");
            }
        });
    }
}
