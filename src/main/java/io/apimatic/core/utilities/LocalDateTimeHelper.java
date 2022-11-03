package io.apimatic.core.utilities;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This is a utility class for LocalDateTime operations.
 */
public class LocalDateTimeHelper extends DateHelper {

    /**
     * Match the pattern for a datetime string in Rfc1123 format.
     */
    private static final DateTimeFormatter RFC1123_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");

    /**
     * Parse a Unix Timestamp to a DateTime object.
     * @param date The Unix Timestamp.
     * @return The DateTime object.
     */
    public static LocalDateTime fromUnixTimestamp(Long date) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
    }

    /**
     * Parse a Unix Timestamp string to a DateTime object.
     * @param date The Unix Timestamp as a String.
     * @return The parsed DateTime object.
     */
    public static LocalDateTime fromUnixTimestamp(String date) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(date)),
                ZoneId.systemDefault());
    }

    /**
     * Convert a DateTime object to a Unix Timestamp string.
     * @param value The DateTime object to convert.
     * @return The converted String.
     */
    public static String toUnixTimestamp(LocalDateTime value) {
        return value == null ? null : Long.toString(toUnixTimestampLong(value));
    }

    /**
     * Convert a List of DateTime objects to Unix Timestamp strings.
     * @param values The List of DateTime objects to convert.
     * @return The list of converted Strings.
     */
    public static List<String> toUnixTimestamp(List<LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        List<String> valuesAsString = new ArrayList<>();
        for (LocalDateTime value : values) {
            valuesAsString.add(toUnixTimestamp(value));
        }
        return valuesAsString;
    }

    /**
     * Convert a Map of DateTime objects to Unix Timestamp strings.
     * @param values The Map of DateTime objects to convert.
     * @return The Map of converted Strings.
     */
    public static Map<String, String> toUnixTimestamp(Map<String, LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        Map<String, String> valuesAsString = new HashMap<>();
        for (Map.Entry<String, LocalDateTime> value : values.entrySet()) {
            valuesAsString.put(value.getKey(), toUnixTimestamp(value.getValue()));
        }
        return valuesAsString;
    }

    /**
     * Convert a List of Map of DateTime objects to Unix Timestamp strings.
     * @param values The List of Map of DateTime objects to convert.
     * @return The list of map of converted Strings.
     */
    public static List<Map<String, String>> toArrayOfMapOfUnixTimestamp(
            List<Map<String, LocalDateTime>> values) {
        if (values == null) {
            return null;
        }
        List<Map<String, String>> valuesAsString = new ArrayList<>();
        for (Map<String, LocalDateTime> value : values) {
            valuesAsString.add(toUnixTimestamp(value));
        }
        return valuesAsString;
    }

    /**
     * Convert a DateTime object to a Unix Timestamp Long.
     * @param value The DateTime object to convert.
     * @return The converted Long.
     */
    public static Long toUnixTimestampLong(LocalDateTime value) {
        return value == null ? null
                : LocalDateTime
                        .ofInstant(value.atZone(ZoneId.systemDefault()).toInstant(), ZoneOffset.UTC)
                        .toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * Convert a List of DateTime objects to Unix Timestamp Longs.
     * @param values The List of DateTime objects to convert.
     * @return The list of converted Longs.
     */
    public static List<Long> toUnixTimestampLong(List<LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        List<Long> valuesAsLong = new ArrayList<>();
        for (LocalDateTime value : values) {
            valuesAsLong.add(toUnixTimestampLong(value));
        }
        return valuesAsLong;
    }

    /**
     * Convert a Map of DateTime objects to Unix Timestamp Longs.
     * @param values The Map of DateTime objects to convert.
     * @return The Map of converted Longs.
     */
    public static Map<String, Long> toUnixTimestampLong(Map<String, LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        Map<String, Long> valuesAsLong = new HashMap<>();
        for (Map.Entry<String, LocalDateTime> value : values.entrySet()) {
            valuesAsLong.put(value.getKey(), toUnixTimestampLong(value.getValue()));
        }
        return valuesAsLong;
    }

    /**
     * Convert a List of Map of DateTime objects to Unix Timestamp Longs.
     * @param values The List of Map of DateTime objects to convert.
     * @return The list of map of converted Longs.
     */
    public static List<Map<String, Long>> toArrayOfMapOfUnixTimestampLong(
            List<Map<String, LocalDateTime>> values) {
        if (values == null) {
            return null;
        }
        List<Map<String, Long>> valuesAsLong = new ArrayList<>();
        for (Map<String, LocalDateTime> value : values) {
            valuesAsLong.add(toUnixTimestampLong(value));
        }
        return valuesAsLong;
    }

    /**
     * Parse a datetime string in Rfc1123 format to a DateTime object.
     * @param date The datetime string in Rfc1123 format.
     * @return The parsed DateTime object.
     */
    public static LocalDateTime fromRfc1123DateTime(String date) {
        return LocalDateTime.parse(date, RFC1123_DATE_TIME_FORMATTER);
    }

    /**
     * Convert a DateTime object to a Rfc1123 formatted string.
     * @param value The DateTime object to convert.
     * @return The converted String.
     */
    public static String toRfc1123DateTime(LocalDateTime value) {
        return value == null ? null
                : RFC1123_DATE_TIME_FORMATTER.format(value.atZone(ZoneId.of("GMT")));
    }

    /**
     * Convert a List of DateTime objects to Rfc1123 formatted strings.
     * @param values The List of DateTime objects to convert.
     * @return The List of converted Strings.
     */
    public static List<String> toRfc1123DateTime(List<LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        List<String> valuesAsString = new ArrayList<>();
        for (LocalDateTime value : values) {
            valuesAsString.add(toRfc1123DateTime(value));
        }
        return valuesAsString;
    }

    /**
     * Convert a Map of DateTime objects to Rfc1123 formatted strings.
     * @param values The Map of DateTime objects to convert.
     * @return The Map of converted Strings.
     */
    public static Map<String, String> toRfc1123DateTime(Map<String, LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        Map<String, String> valuesAsString = new HashMap<>();
        for (Map.Entry<String, LocalDateTime> value : values.entrySet()) {
            valuesAsString.put(value.getKey(), toRfc1123DateTime(value.getValue()));
        }
        return valuesAsString;
    }

    /**
     * Convert a List of Map of DateTime objects to Rfc1123 formatted strings.
     * @param values The List of Map of DateTime objects to convert.
     * @return The list of map of converted Strings.
     */
    public static List<Map<String, String>> toArrayOfMapOfRfc1123DateTime(
            List<Map<String, LocalDateTime>> values) {
        if (values == null) {
            return null;
        }
        List<Map<String, String>> valuesAsString = new ArrayList<>();
        for (Map<String, LocalDateTime> value : values) {
            valuesAsString.add(toRfc1123DateTime(value));
        }
        return valuesAsString;
    }

    /**
     * Parse a datetime string in Rfc8601(Rfc3339) format to a DateTime object.
     * @param date The datetime string in Rfc8601(Rfc3339) format.
     * @return The parsed DateTime object.
     */
    public static LocalDateTime fromRfc8601DateTime(String date) {
        Pattern pattern = Pattern.compile("(Z|([+-])(\\d{2}:\\d{2}))$");
        Matcher patternMatcher = pattern.matcher(date);
        if (patternMatcher.find()) {
            OffsetDateTime parsed = OffsetDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
            return LocalDateTime.from(parsed.withOffsetSameInstant(ZoneOffset.UTC));
        }
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Convert a DateTime object to a Rfc8601(Rfc3339) formatted string.
     * @param value The DateTime object to convert.
     * @return The converted String.
     */
    public static String toRfc8601DateTime(LocalDateTime value) {
        return value == null ? null : value.toString() + "Z";
    }

    /**
     * Convert a List of DateTime objects to Rfc8601(Rfc3339) formatted strings.
     * @param values The List of DateTime objects to convert.
     * @return The List of converted Strings.
     */
    public static List<String> toRfc8601DateTime(List<LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        List<String> valuesAsString = new ArrayList<>();
        for (LocalDateTime value : values) {
            valuesAsString.add(toRfc8601DateTime(value));
        }
        return valuesAsString;
    }

    /**
     * Convert a Map of DateTime objects to Rfc8601(Rfc3339) formatted strings.
     * @param values The Map of DateTime objects to convert.
     * @return The Map of converted Strings.
     */
    public static Map<String, String> toRfc8601DateTime(Map<String, LocalDateTime> values) {
        if (values == null) {
            return null;
        }
        Map<String, String> valuesAsString = new HashMap<>();
        for (Map.Entry<String, LocalDateTime> value : values.entrySet()) {
            valuesAsString.put(value.getKey(), toRfc8601DateTime(value.getValue()));
        }
        return valuesAsString;
    }

    /**
     * Convert a List of Map of DateTime objects to Rfc8601(Rfc3339) formatted strings.
     * @param values The List of Map of DateTime objects to convert.
     * @return The list of map of converted Strings.
     */
    public static List<Map<String, String>> toArrayOfMapOfRfc8601DateTime(
            List<Map<String, LocalDateTime>> values) {
        if (values == null) {
            return null;
        }
        List<Map<String, String>> valuesAsString = new ArrayList<>();
        for (Map<String, LocalDateTime> value : values) {
            valuesAsString.add(toRfc8601DateTime(value));
        }
        return valuesAsString;
    }



    /**
     * A class to handle deserialization of DateTime objects to Unix Timestamps.
     */
    public static class UnixTimestampDeserializer extends JsonDeserializer<LocalDateTime> {
        @SuppressWarnings("unused")
        @Override
        public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return fromUnixTimestamp(jp.getValueAsString());
        }
    }

    /**
     * A class to handle serialization of Unix Timestamps to DateTime objects.
     */
    public static class UnixTimestampSerializer extends JsonSerializer<LocalDateTime> {
        @SuppressWarnings("unused")
        @Override
        public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeObject(toUnixTimestampLong(value));
        }
    }

    /**
     * A class to handle deserialization of DateTime objects to Rfc1123 format strings.
     */
    public static class Rfc1123DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @SuppressWarnings("unused")
        @Override
        public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return fromRfc1123DateTime(jp.getValueAsString());
        }
    }

    /**
     * A class to handle serialization of Rfc1123 format strings to DateTime objects.
     */
    public static class Rfc1123DateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @SuppressWarnings("unused")
        @Override
        public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeString(toRfc1123DateTime(value));
        }
    }

    /**
     * A class to handle deserialization of DateTime objects to Rfc8601(Rfc3339) format strings.
     */
    public static class Rfc8601DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @SuppressWarnings("unused")
        @Override
        public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return fromRfc8601DateTime(jp.getValueAsString());
        }
    }

    /**
     * A class to handle serialization of Rfc8601(Rfc3339) format strings to DateTime objects.
     */
    public static class Rfc8601DateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @SuppressWarnings("unused")
        @Override
        public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeString(toRfc8601DateTime(value));
        }
    }

    /**
     * Rfc1123 Date Time adapter utility class.
     */
    public static class Rfc1123DateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public String marshal(LocalDateTime dateTime) {
            return LocalDateTimeHelper.toRfc1123DateTime(dateTime);
        }

        @Override
        public LocalDateTime unmarshal(String dateTime) {
            return LocalDateTimeHelper.fromRfc1123DateTime(dateTime);
        }
    }

    /**
     * Rfc8601 Date Time adapter utility class.
     */
    public static class Rfc8601DateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public String marshal(LocalDateTime dateTime) {
            return LocalDateTimeHelper.toRfc8601DateTime(dateTime);
        }

        @Override
        public LocalDateTime unmarshal(String dateTime) {
            return LocalDateTimeHelper.fromRfc8601DateTime(dateTime);
        }
    }

    /**
     * UnixTimestamp adapter utility class.
     */
    public static class UnixTimestampAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public String marshal(LocalDateTime dateTime) {
            return LocalDateTimeHelper.toUnixTimestamp(dateTime);
        }

        @Override
        public LocalDateTime unmarshal(String dateTime) {
            return LocalDateTimeHelper.fromUnixTimestamp(dateTime);
        }
    }
}
