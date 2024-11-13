package io.apimatic.core.utilities;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a Helper class for the coversion of type for all structures supported
 * in the SDK.
 */
public class ConversionHelper {
    /**
     * Converts a single object to the specified type.
     * 
     * @param value              The object to convert.
     * @param conversionFunction The function to apply for conversion.
     * @return The converted object of type {@code S}, or null if conversion fails.
     */
    public static <S> S convertToSimpleType(Object value, Function<Object, S> conversionFunction) {
        try {
            return conversionFunction.apply(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a map of objects to a map of the specified type.
     * 
     * @param value              The map of objects to convert.
     * @param conversionFunction The function to apply for conversion of each value.
     * @return A map with values converted to type {@code S}, skipping unconvertible
     *         values.
     */
    @SuppressWarnings("unchecked")
    public static <S> Map<String, S> convertToMap(Object value, Function<Object, S> conversionFunction) {
        if (value == null)
            return null;

        try {
            Map<String, Object> valueMap = (Map<String, Object>) value;
            Map<String, S> filteredMap = valueMap.entrySet().stream().map(entry -> {
                S convertedValue = convertToSimpleType(entry.getValue(), conversionFunction);
                return new AbstractMap.SimpleEntry<>(entry.getKey(), convertedValue);
            }).filter(entry -> entry.getValue() != null) // Filter out null values
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return filteredMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a list of objects to a list of the specified type.
     * 
     * @param value              The list of objects to convert.
     * @param conversionFunction The function to apply for conversion of each item.
     * @return A list with elements converted to type {@code S}, skipping
     *         unconvertible items.
     */
    @SuppressWarnings("unchecked")
    public static <S> List<S> convertToArray(Object value, Function<Object, S> conversionFunction) {
        try {
            List<Object> valueList = (List<Object>) value;
            return valueList.stream().map(item -> convertToSimpleType(item, conversionFunction))
                    .filter(item -> item != null).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a list of maps to a list of maps with values of the specified type.
     * 
     * @param value              The list of maps to convert.
     * @param conversionFunction The function to apply for conversion of each map's
     *                           values.
     * @return A list of maps with converted values of type {@code S}, skipping
     *         unconvertible items.
     */
    @SuppressWarnings("unchecked")
    public static <S> List<Map<String, S>> convertToArrayOfMap(Object value, Function<Object, S> conversionFunction) {
        try {
            List<Object> valueList = (List<Object>) value;
            return valueList.stream().map(item -> convertToMap(item, conversionFunction))
                    .filter(map -> map != null && !map.isEmpty()).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a map of lists to a map with lists of the specified type.
     * 
     * @param value              The map of lists to convert.
     * @param conversionFunction The function to apply for conversion of each list's
     *                           elements.
     * @return A map with lists converted to type {@code S}, skipping unconvertible
     *         items.
     */
    @SuppressWarnings("unchecked")
    public static <S> Map<String, List<S>> convertToMapOfArray(Object value, Function<Object, S> conversionFunction) {
        try {
            Map<String, Object> valueMap = (Map<String, Object>) value;
            return valueMap.entrySet().stream()
                    .map(entry -> Map.entry(entry.getKey(), convertToArray(entry.getValue(), conversionFunction)))
                    .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts an n-dimensional array to a nested list with elements of the
     * specified type.
     * 
     * @param value              The n-dimensional array to convert.
     * @param conversionFunction The function to apply for conversion of each
     *                           element.
     * @param dimensionCount     The depth of the nested structure.
     * @return A nested list with elements converted to type {@code T}, skipping
     *         unconvertible items.
     */
    @SuppressWarnings("unchecked")
    public static <T, S> T convertToNDimensionalArray(Object value, Function<Object, S> conversionFunction,
            int dimensionCount) {
        try {
            return (T) convertToNDimensionalArrayInternal(value, conversionFunction, dimensionCount);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Applies the conversion function to the n-dimensional arrays recursively.
     * 
     * @param value              The n-dimensional array to convert.
     * @param conversionFunction The function to apply for conversion of each
     *                           element.
     * @param dimensionCount     The depth of the nested structure.
     * @return A nested list with elements converted to type {@code S}, skipping
     *         unconvertible items.
     */
    @SuppressWarnings("unchecked")
    private static <S> List<?> convertToNDimensionalArrayInternal(Object value, Function<Object, S> conversionFunction,
            int dimensionCount) {
        try {
            if (dimensionCount == 1) {
                return convertToArray(value, conversionFunction);
            } else if (dimensionCount > 1) {
                List<Object> valueList = (List<Object>) value;
                return valueList.stream()
                        .map(item -> convertToNDimensionalArray(item, conversionFunction, dimensionCount - 1))
                        .filter(item -> item != null && !((List<?>) item).isEmpty()).collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Ignoring in order to handle the exception silently.
        }
        return null;
    }
}
