package io.apimatic.core.authentication;

import java.util.Map;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * HeaderAuth is an implementation of Authentication that supports HTTP authentication
 * through HTTP Headers.
 */
public class HeaderAuth extends AuthCredential {

    /**
     * @param authParams Map of authentication parameters.
     */
    public HeaderAuth(final Map<String, String> authParams) {
        super(authParams);
    }

    /**
     * Apply the Header authentication.
     * @param httpRequest The HTTP request on which the auth is to be applied.
     * @return {@link Request} The HTTP request after applying auth.
     */
    public Request apply(Request httpRequest) {
        getAuthParams().forEach((key, value) -> {
        	httpRequest.getHeaders().remove(key);
            httpRequest.getHeaders().add(key, value);
        });

        return httpRequest;
    }
}
