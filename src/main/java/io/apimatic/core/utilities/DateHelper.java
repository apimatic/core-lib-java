package io.apimatic.core.utilities;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This is a utility class for Date operations.
 */
public class DateHelper {

    protected DateHelper() {}

    /**
     * Parse a simple date string to a LocalDate object.
     * @param date The date string.
     * @return The parsed LocalDate object.
     */
    public static LocalDate fromSimpleDate(String date) {
        return LocalDate.parse(date);
    }

    /**
     * Convert a LocalDate object to a string.
     * @param value The LocalDate object to convert.
     * @return The converted Strings.
     */
    public static String toSimpleDate(LocalDate value) {
        return value == null ? null : value.toString();
    }

    /**
     * Convert a List of LocalDate objects to strings.
     * @param values The List of LocalDate objects to convert.
     * @return The List of converted Strings.
     */
    public static List<String> toSimpleDate(List<LocalDate> values) {
        if (values == null) {
            return null;
        }
        List<String> valuesAsString = new ArrayList<>();
        for (LocalDate value : values) {
            valuesAsString.add(toSimpleDate(value));
        }
        return valuesAsString;
    }

    /**
     * Convert a Map of LocalDate objects to strings.
     * @param values The Map of LocalDate objects to convert.
     * @return The Map of converted Strings.
     */
    public static Map<String, String> toSimpleDate(Map<String, LocalDate> values) {
        if (values == null) {
            return null;
        }
        Map<String, String> valuesAsString = new HashMap<>();
        for (Map.Entry<String, LocalDate> value : values.entrySet()) {
            valuesAsString.put(value.getKey(), toSimpleDate(value.getValue()));
        }
        return valuesAsString;
    }

    /**
     * Convert a List of Map of LocalDate objects to strings.
     * @param values The List of Map of LocalDate objects to convert.
     * @return The list of map of converted Strings.
     */
    public static List<Map<String, String>> toArrayOfMapOfSimpleDate(
            List<Map<String, LocalDate>> values) {
        if (values == null) {
            return null;
        }
        List<Map<String, String>> valuesAsString = new ArrayList<>();
        for (Map<String, LocalDate> value : values) {
            valuesAsString.add(toSimpleDate(value));
        }
        return valuesAsString;
    }

    /**
     * A class to handle deserialization of date strings to LocalDate objects.
     */
    public static class SimpleDateDeserializer extends JsonDeserializer<LocalDate> {
        @SuppressWarnings("unused")
        @Override
        public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return fromSimpleDate(jp.getValueAsString());
        }
    }

    /**
     * A class to handle serialization of LocalDate objects to date strings.
     */
    public static class SimpleDateSerializer extends JsonSerializer<LocalDate> {
        @SuppressWarnings("unused")
        @Override
        public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeString(toSimpleDate(value));
        }
    }

    /**
     * Simple Adapter utility class.
     */
    public static class SimpleAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public String marshal(LocalDate date) {
            return date.toString();
        }

        @Override
        public LocalDate unmarshal(String date) {
            return LocalDate.parse(date);
        }
    }
}
