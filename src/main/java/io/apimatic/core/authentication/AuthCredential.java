package io.apimatic.core.authentication;

import java.util.HashMap;
import java.util.Map;
import io.apimatic.coreinterfaces.authentication.Authentication;

/**
 * Handles and validates the Authentication parameters.
 */
public abstract class AuthCredential extends Authentication {

    /**
     * A map of authentication parameters.
     */
    private Map<String, String> authParams = new HashMap<>();

    /**
     * @param authParams Map of authentication parameters.
     */
    public AuthCredential(final Map<String, String> authParams) {
        this.authParams = authParams;
    }

    /**
     * Getter for the map of authentication parameters.
     * @return Map&lt;String, String&gt; The map of authentication parameters.
     */
    public Map<String, String> getAuthParams() {
        return authParams;
    }

    /**
     * Validates the credentials for authentication.
     */
    public void validate() {
        // Check for null keys or values
        boolean hasNullKeyOrValue = authParams.entrySet().stream()
                .anyMatch(entry -> entry.getKey() == null || entry.getValue() == null);

        if (hasNullKeyOrValue) {
            setErrorMessage("[Auth key and value cannot be null]");
            setValidity(false);
            return;
        }

        setValidity(true);
    }

}
