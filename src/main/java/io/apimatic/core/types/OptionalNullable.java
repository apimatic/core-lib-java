package io.apimatic.core.types;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.core.utilities.ZonedDateTimeHelper;

/**
 * Class to encapsulate fields which are Optional as well as Nullable. It also provides helper.
 * methods to create OptionalNullable generic type, and to extract value from it.
 * @param <T> Type of the encapsulated field.
 */
public final class OptionalNullable<T> {

    /**
     * Private store for encapsulated object's value.
     */
    private T value;

    private OptionalNullable(final T value) {
        this.value = value;
    }

    /**
     * Converts this OptionalNullable into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "" + value;
    }

    /**
     * Creates an OptionalNullable instance with the provided value.
     * @param <T> Type of the provided object.
     * @param value Value of the provided object.
     * @return {@link OptionalNullable} instance encapsulating given value.
     */
    public static <T> OptionalNullable<T> of(T value) {
        return new OptionalNullable<T>(value);
    }

    /**
     * Extracts the encapsulated value from the given OptionalNullable.
     * @param <T> Type of the expected value.
     * @param optionalNullable OptionalNullable instance to get value.
     * @return Value of the extracted field.
     */
    public static <T> T getFrom(OptionalNullable<T> optionalNullable) {
        return (optionalNullable == null) ? null : optionalNullable.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof OptionalNullable<?>) {
            return Objects.equals(((OptionalNullable<?>) obj).value, value);
        }

        return Objects.equals(obj, value);
    }

    /**
     * JsonSerializer for the {@link OptionalNullable} instance. It is used to Serialize an
     * {@link OptionalNullable} as its encapsulated object.
     */
    public static class Serializer extends JsonSerializer<OptionalNullable<Object>> {

        /**
         * Override this method to extract the required data from given data instance
         * @param data Input data instance
         * @return Extracted/Converted data instance with required functionality
         */
        protected Object extractData(Object data) {
            return data;
        }

        @Override
        public void serialize(
                OptionalNullable<Object> object, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeObject(extractData(object.value));
        }
    }

    /**
     * A class to handle serialization of LocalDate objects to date strings.
     */
    private static abstract class DateSerializer<M> extends Serializer {

        @SuppressWarnings("unchecked")
        @Override
        protected Object extractData(Object data)
        {
            if (data instanceof List<?>) {
                return extractListData((List<Object>) data);
            }

            if (data instanceof Map<?, ?>) {
                return extractMapData((Map<String, Object>) data);
            }

            return extractSimpleData(data);
        }

        public abstract M extractSimpleData(Object data);

        public abstract List<M> extractListData(Object data);

        public abstract Map<String, M> extractMapData(Object data);
    }

    /**
     * A class to handle serialization of LocalDate objects to date strings.
     */
    public static class SimpleDateSerializer extends DateSerializer<String> {

        @Override
        public String extractSimpleData(Object data) {
            return LocalDateTimeHelper.toSimpleDate((LocalDate) data);
        }

        @Override
        @SuppressWarnings("unchecked")
        public List<String> extractListData(Object data) {
            return LocalDateTimeHelper.toSimpleDate((List<LocalDate>) data);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<String, String> extractMapData(Object data) {
            return LocalDateTimeHelper.toSimpleDate((Map<String, LocalDate>) data);
        }
    }

    /**
     * A class to handle serialization of Unix Timestamps to DateTime objects.
     */
    public static class UnixTimestampSerializer extends DateSerializer<Long> {

        @Override
        public Long extractSimpleData(Object data) {
            return LocalDateTimeHelper.toUnixTimestampLong((LocalDateTime) data);
        }

        @Override
        @SuppressWarnings("unchecked")
        public List<Long> extractListData(Object data) {
            return LocalDateTimeHelper.toUnixTimestampLong((List<LocalDateTime>) data);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<String, Long> extractMapData(Object data) {
            return LocalDateTimeHelper.toUnixTimestampLong((Map<String, LocalDateTime>) data);
        }
    }

    /**
     * A class to handle serialization of Rfc1123 format strings to DateTime objects.
     */
    public static class Rfc1123DateTimeSerializer extends DateSerializer<String> {

        @Override
        public String extractSimpleData(Object data) {
            return LocalDateTimeHelper.toRfc1123DateTime((LocalDateTime) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> extractListData(Object data) {
            return LocalDateTimeHelper.toRfc1123DateTime((List<LocalDateTime>) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, String> extractMapData(Object data) {
            return LocalDateTimeHelper.toRfc1123DateTime((Map<String, LocalDateTime>) data);
        }
    }

    /**
     * A class to handle serialization of Rfc8601(Rfc3339) format strings to DateTime objects.
     */
    public static class Rfc8601DateTimeSerializer extends DateSerializer<String> {

        @Override
        public String extractSimpleData(Object data) {
            return LocalDateTimeHelper.toRfc8601DateTime((LocalDateTime) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> extractListData(Object data) {
            return LocalDateTimeHelper.toRfc8601DateTime((List<LocalDateTime>) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, String> extractMapData(Object data) {
            return LocalDateTimeHelper.toRfc8601DateTime((Map<String, LocalDateTime>) data);
        }
    }

    /**
     * A class to handle serialization of Unix Timestamps to DateTime objects.
     */
    public static class ZonedUnixTimestampSerializer extends DateSerializer<Long> {

        @Override
        public Long extractSimpleData(Object data) {
            return ZonedDateTimeHelper.toUnixTimestampLong((ZonedDateTime) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<Long> extractListData(Object data) {
            return ZonedDateTimeHelper.toUnixTimestampLong((List<ZonedDateTime>) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, Long> extractMapData(Object data) {
            return ZonedDateTimeHelper.toUnixTimestampLong((Map<String, ZonedDateTime>) data);
        }
    }

    /**
     * A class to handle serialization of Rfc1123 format strings to DateTime objects.
     */
    public static class ZonedRfc1123DateTimeSerializer extends DateSerializer<String> {

        @Override
        public String extractSimpleData(Object data) {
            return ZonedDateTimeHelper.toRfc1123DateTime((ZonedDateTime) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> extractListData(Object data) {
            return ZonedDateTimeHelper.toRfc1123DateTime((List<ZonedDateTime>) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, String> extractMapData(Object data) {
            return ZonedDateTimeHelper.toRfc1123DateTime((Map<String, ZonedDateTime>) data);
        }
    }

    /**
     * A class to handle serialization of Rfc8601(Rfc3339) format strings to DateTime objects.
     */
    public static class ZonedRfc8601DateTimeSerializer extends DateSerializer<String> {

        @Override
        public String extractSimpleData(Object data) {
            return ZonedDateTimeHelper.toRfc8601DateTime((ZonedDateTime) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> extractListData(Object data) {
            return ZonedDateTimeHelper.toRfc8601DateTime((List<ZonedDateTime>) data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, String> extractMapData(Object data) {
            return ZonedDateTimeHelper.toRfc8601DateTime((Map<String, ZonedDateTime>) data);
        }
    }
}
