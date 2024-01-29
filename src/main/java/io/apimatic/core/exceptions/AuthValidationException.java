package io.apimatic.core.exceptions;

/**
 * This is an exception that will be thrown when the auth validation fails.
 */
public class AuthValidationException extends RuntimeException {

    /**
     * UID for serialization.
     */
    private static final long serialVersionUID = 834982232110112456L;

    /**
     * Initialization constructor.
     * @param message The exception message.
     */
    public AuthValidationException(final String message) {
        super(message);
    }
}
