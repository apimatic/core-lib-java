package io.apimatic.core_lib.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.core_interfaces.authentication.Authentication;
import io.apimatic.core_interfaces.http.request.Request;

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
            httpRequest.addQueryParameter(key, value);
        });
        return httpRequest;
    }
}
