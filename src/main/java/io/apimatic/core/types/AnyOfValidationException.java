package io.apimatic.core.types;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This is the base class for all exceptions that represent an error response from the server.
 */
public class AnyOfValidationException extends IOException {

	/**
	 * UID for serialization.
	 */
	private static final long serialVersionUID = 1214174253911720228L;

	/**
     * Initialization constructor.
	 * @param types List on unMapped types
	 * @param json Value that was not mapped by the above types
	 */
	public AnyOfValidationException(List<String> types, JsonNode json) {
        super("We could not match any acceptable type from " + String.join(", ", types) + " on: " + json );
    }
}
