package io.apimatic.core.types;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This is the base class for all exceptions that represent an error response from the server.
 */
public class OneOfValidationException extends IOException {

    /**
     * UID for serialization.
     */
    private static final long serialVersionUID = 6424174253911720228L;

    /**
     * Initialization constructor.
     * @param type1 The first type that was mapped on jsonNode
     * @param type2 The second type that also got mapped on jsonNode
     * @param json Value that was mapped by the above two types
     */
    public OneOfValidationException(final String type1, final String type2, final JsonNode json) {
        super("There are more than one matching types i.e. "
                + type1 + " and " + type2 + " on: " + json);
    }

    /**
     * Initialization constructor.
     * @param types List on unMapped types
     * @param json Value that was not mapped by the above types
     */
    public OneOfValidationException(final List<String> types, final JsonNode json) {
        super("We could not match any acceptable type from "
                + String.join(", ", types) + " on: " + json);
    }
}
