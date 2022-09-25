package io.apimatic.core_lib.utilities;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This is a wrapper class for JSON object.
 */
public class JsonObject {
    @com.fasterxml.jackson.annotation.JsonValue
    private JsonNode jsonNode;

    /**
     * Initialization private constructor.
     * @param jsonNode The JSON of type JsonNode.
     */
    @JsonCreator
    private JsonObject(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    /**
     * Initializes JsonObject with provided JSON.
     * @param jsonString The JSON string.
     * @return The JsonObject instance.
     * @throws IOException signals that provided JSON string does not contain a valid JSON object.
     */
    public static JsonObject fromJsonString(String jsonString) throws IOException {
        if (jsonString == null) {
            return new JsonObject(null);
        }
        JsonNode node = CoreHelper.mapper.readTree(jsonString);
        if (node.isObject()) {
            return new JsonObject(node);
        }
        throw new IOException("Provided JSON string must contain a valid JSON object.");
    }

    /**
     * Getter for stored JSON object.
     * @return The stored JSON as Object.
     */
    public Object getStoredObject() {
        return CoreHelper.deserializeAsObject(toString());
    }

    /**
     * Converts the JSON into string.
     * @return String representation of JSON
     */
    @Override
    public String toString() {
        try {
            return CoreHelper.mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
