package io.apimatic.core_lib.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.core_interfaces.authentication.Authentication;
import io.apimatic.core_interfaces.http.request.Request;

/**
 * QueryAuth is an implementation of {@link Authentication} that supports HTTP authentication
 * through Query parameters
 *
 */
public class QueryAuth implements Authentication {

    private Map<String, String> authParams = new HashMap<>();

    public QueryAuth(Map<String, String> authParams) {
        this.authParams = authParams;
    }

    /**
     * Apply the Query authentication
     */
    @Override
    public Request apply(Request httpRequest) {
        authParams.forEach((key, value) -> {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Auth key or value cannot be null.");
            }
            httpRequest.addQueryParameter(key, value);
        });
        return httpRequest;
    }
}
