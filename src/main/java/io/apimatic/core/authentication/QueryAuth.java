package io.apimatic.core.authentication;

import java.util.Map;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * QueryAuth is an implementation of {@link Authentication} that supports HTTP authentication
 * through Query parameters.
 */
public class QueryAuth extends AuthCredential {

    /**
     * @param authParams Map of authentication parameters.
     */
    public QueryAuth(final Map<String, String> authParams) {
        super(authParams);
    }

    /**
     * Apply the Query authentication.
     * 
     * @param httpRequest A httpRequest
     */
    public Request apply(Request httpRequest) {
        getAuthParams().forEach((key, value) -> {
            httpRequest.addQueryParameter(key, value);
        });
        return httpRequest;
    }
}
