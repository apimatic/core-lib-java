package io.apimatic.core.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * HeaderAuth is an implementation of {@link Authentication} that supports HTTP authentication
 * through HTTP Headers.
 *
 */
public class HeaderAuth implements Authentication {

    private Map<String, String> authParams = new HashMap<>();

    public HeaderAuth(Map<String, String> authParams) {
        this.authParams = authParams;
    }

    /**
     * Apply the header authentication
     */
    @Override
    public Request apply(Request httpRequest) {
        authParams.forEach((key, value) -> {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Auth key or value cannot be null.");
            }
            httpRequest.getHeaders().add(key, value);
        });
        return httpRequest;
    }
}
