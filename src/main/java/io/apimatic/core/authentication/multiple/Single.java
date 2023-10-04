package io.apimatic.core.authentication.multiple;

import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * Handles a single authentication scheme.
 */
public class Single extends Authentication {

    /**
     * The provided authentication scheme to be validated and applied.
     */
    private Authentication authentication;

    public Single(final Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Applies the authentication on the httpRequest.
     * @param httpRequest the request on which authentication is being applied.
     * @return {@link Request} The authenticated request.
     */
    public Request apply(Request httpRequest) {
        return authentication.apply(httpRequest);
    }

    /**
     * Validates the single/leaf authentication.
     * @return true if the auth is valid, false otherwise.
     */
    public boolean validate() {
        boolean isValid = authentication.validate();
        if (!isValid) {
            setErrorMessage(authentication.getErrorMessage());
        }

        return isValid;
    }
}
