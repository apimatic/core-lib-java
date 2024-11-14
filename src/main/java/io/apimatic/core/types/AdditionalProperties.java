package io.apimatic.core.types;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * A generic class for managing additional properties in a model.
 *
 * @param <T> the type of the additional properties.
 */
public class AdditionalProperties<T> {

    /**
     * Map to store additional properties.
     */
    private final Map<String, T> additionalProperties = new LinkedHashMap<>();

    /**
     * Set to store model properties.
     */
    private final Set<String> modelProperties = new HashSet<>();

    /**
     * Default constructor.
     */
    public AdditionalProperties() {
        // Default constructor
    }

    /**
     * Parameterized constructor.
     * 
     * @param classInstance The instance of the class with additional properties.
     */
    public AdditionalProperties(Class<?> classInstance) {
        Method[] methods = classInstance.getMethods();
        for (Method method : methods) {
            JsonGetter jsonGetter = method.getAnnotation(JsonGetter.class);
            if (jsonGetter != null) {
                modelProperties.add(jsonGetter.value());
            }
        }
    }

    /**
     * Gets the additional properties.
     * 
     * @return the map of additional properties.
     */
    public Map<String, T> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Sets an additional property.
     * 
     * @param key   The key for the additional property.
     * @param value The value of the additional property.
     * @throws IllegalArgumentException if there is a conflict between the key and
     *                                  any model property.
     */
    public void setAdditionalProperty(String key, T value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty.");
        }

        if (modelProperties.contains(key)) {
            throw new IllegalArgumentException(
                    "Key '" + key + "' conflicts with a model property.");
        }
        additionalProperties.put(key, value);
    }

    /**
     * Sets an additional property with an option to skip null values.
     * 
     * @param key           The key for the additional property.
     * @param value         The value of the additional property.
     * @param skipNullValue If true, null values will be skipped.
     * @throws IllegalArgumentException if there is a conflict between the key and
     *                                  any model property.
     */
    public void setAdditionalProperty(String key, T value, boolean skipNullValue) {
        if (skipNullValue && value == null) {
            return;
        }
        setAdditionalProperty(key, value);
    }

    @Override
    public String toString() {
        if (additionalProperties.isEmpty()) {
            return "";
        }
        return additionalProperties.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", ", ", ", ""));
    }

    /**
     * Gets an additional property by key.
     * 
     * @param key The key of the additional property to retrieve.
     * @return the value of the additional property associated with the given key,
     *         or null if not found.
     */
    public T getAdditionalProperty(String key) {
        return additionalProperties.get(key);
    }
}