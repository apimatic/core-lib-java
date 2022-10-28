package io.apimatic.core.utilities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Wrapper class for JSON value.
 */
public class CoreJsonValue {
    /**
     * A json value
     */
    @com.fasterxml.jackson.annotation.JsonValue
    private final JsonNode value;

    /**
     * Initialization constructor.
     * @param value The JSON of type JsonNode.
     */
    @JsonCreator
    protected CoreJsonValue(final JsonNode value) {
        this.value = value;
    }

    /**
     * Initializes JsonValue instance with provided value.
     * @param value The string value to initialize with.
     * @return The {@link CoreJsonValue} instance.
     */
    public static CoreJsonValue fromString(String value) {
        if (value == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(TextNode.valueOf(value));
    }

    /**
     * Initializes JsonValue instance with provided value.
     * @param value The boolean value to initialize with.
     * @return The {@link CoreJsonValue} instance.
     */
    public static CoreJsonValue fromBoolean(Boolean value) {
        if (value == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(BooleanNode.valueOf(value));
    }

    /**
     * Initializes JsonValue instance with provided value.
     * @param value The integer value to initialize with.
     * @return The {@link CoreJsonValue} instance.
     */
    public static CoreJsonValue fromInteger(Integer value) {
        if (value == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(IntNode.valueOf(value));
    }

    /**
     * Initializes JsonValue instance with provided value.
     * @param value The long value to initialize with.
     * @return The {@link CoreJsonValue} instance.
     */
    public static CoreJsonValue fromLong(Long value) {
        if (value == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(LongNode.valueOf(value));
    }

    /**
     * Initializes JsonValue instance with provided value.
     * @param value The double value to initialize with.
     * @return The {@link CoreJsonValue} instance.
     */
    public static CoreJsonValue fromDouble(Double value) {
        if (value == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(DoubleNode.valueOf(value));
    }

    /**
     * Initializes JsonValue instance with provided value.
     * @param value The double value to initialize with.
     * @return The {@link CoreJsonValue} instance.
     */
    public static CoreJsonValue fromObject(Object value) {
        if (value == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(CoreHelper.mapper.valueToTree(value));
    }

    /**
     * Initializes JsonValue instance with provided list of values.
     * @param <T> The list type
     * @param values The list of values of given type.
     * @return The {@link CoreJsonValue} instance.
     */
    public static <T> CoreJsonValue fromArray(List<T> values) {
        if (values == null) {
            return new CoreJsonValue(null);
        }
        return new CoreJsonValue(CoreHelper.mapper.valueToTree(values));
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
            return CoreHelper.mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
