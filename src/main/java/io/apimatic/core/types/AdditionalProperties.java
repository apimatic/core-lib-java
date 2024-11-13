package io.apimatic.core.types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * A generic class for additional properties in a model.
 */
public class AdditionalProperties<T> {

    /**
     * Map to store additional properties.
     */
    private Map<String, T> additionalProperties = new LinkedHashMap<String, T>();

    /**
     * List to store model properties.
     */
    private Set<String> modelProperties = new HashSet<String>();

    /**
     * Default constructor.
     */
    public AdditionalProperties() {
    }

    /**
     * Parameterized constructor.
     * @param classInstance The instance of the class having additional properties.
     */
    public AdditionalProperties(Class<?> classInstance) {
        Method[] methods = classInstance.getMethods();
        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(JsonGetter.class);
            if (annotation != null) {
                modelProperties.add(((JsonGetter) annotation).value());
            }
        }
    }

    /**
     * The getter for provided additional properties.
     * @return Returns the map of <T> typed additional properties.
     */
    public Map<String, T> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * The setter for an additional property.
     * @param key   The additional property key.
     * @param value The <T> type additional property value.
     * @throws IllegalArgumentException if there is a conflict between key and any
     *                                  model property.
     */
    public void setAdditionalProperty(String key, T value) {
        if (key == null || key.isBlank())
            return;

        if (modelProperties.contains(key)) {
            // key is reserved for properties
            throw new IllegalArgumentException("Key '" + key + "' is conflicting with model property");
        }
        additionalProperties.put(key, value);
    }

    /**
     * The setter for an additional property.
     * @param key   The additional property key.
     * @param value The <T> type additional property value.
     * @param skipNullValue The flag to skip null values in the additional properties map.
     * @throws IllegalArgumentException if there is a conflict between key and any
     *                                  model property.
     */
    public void setAdditionalProperty(String key, T value, boolean skipNullValue) {
        if (skipNullValue && value == null)
            return;

        setAdditionalProperty(key, value);
    }

    @Override
    public String toString() {
        if (additionalProperties.isEmpty()) {
            return "";
        }

        return ", " + additionalProperties.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
    }

    /**
     * The getter for provided additional properties.
     * @param key The additional property key to search.
     * @return the <T> type additional property value associated with the provided
     *         key.
     */
    public T getAdditionalProperty(String key) {
        return additionalProperties.get(key);
    }
}
