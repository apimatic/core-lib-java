package io.apimatic.core.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * HeaderAuth is an implementation of {@link Authentication} that supports HTTP authentication
 * through HTTP Headers.
 */
public class HeaderAuth implements Authentication {

    /**
     * A map for authentication parameters
     */
    private Map<String, String> authParams = new HashMap<>();

    /**
     * @param authParams
     */
    public HeaderAuth(final Map<String, String> authParams) {
        this.authParams = authParams;
    }

    /**
     * @param httpRequest A request
     */
    @Override
    public Request apply(Request httpRequest) {
        authParams.forEach((key, value) -> {
            httpRequest.getHeaders().add(key, value);
        });
        return httpRequest;
    }

    /**
     * Validate the header authentication
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
