package io.apimatic.core_lib.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.core_interfaces.authentication.Authentication;
import io.apimatic.core_interfaces.http.request.Request;

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
            httpRequest.getHeaders().add(key, value);
        });
        return httpRequest;
    }

}
