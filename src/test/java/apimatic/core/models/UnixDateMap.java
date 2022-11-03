package apimatic.core.models;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;

/**
 * This is a model class for UnixDateMap type.
 */
public class UnixDateMap {
    private OptionalNullable<Map<String, LocalDateTime>> dateTime;
    private Map<String, LocalDateTime> dateTime1;

    /**
     * Default constructor.
     */
    public UnixDateMap() {}

    /**
     * Initialization constructor.
     * @param dateTime1 Map of String, value for dateTime1.
     * @param dateTime Map of String, value for dateTime.
     */
    public UnixDateMap(final Map<String, LocalDateTime> dateTime1,
            final Map<String, LocalDateTime> dateTime) {
        this.dateTime = OptionalNullable.of(dateTime);
        this.dateTime1 = dateTime1;
    }

    /**
     * Internal initialization constructor.
     * @param dateTime1 Map of String, value for dateTime1.
     * @param dateTime Map of String, value for dateTime.
     */
    protected UnixDateMap(final Map<String, LocalDateTime> dateTime1,
            final OptionalNullable<Map<String, LocalDateTime>> dateTime) {
        this.dateTime = dateTime;
        this.dateTime1 = dateTime1;
    }

    /**
     * Internal Getter for DateTime.
     * @return Returns the Internal Map of String, LocalDateTime.
     */
    @JsonGetter("dateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.UnixTimestampSerializer.class)
    protected OptionalNullable<Map<String, LocalDateTime>> internalGetDateTime() {
        return this.dateTime;
    }

    /**
     * Getter for DateTime.
     * @return Returns the Map of String, LocalDateTime.
     */
    public Map<String, LocalDateTime> getDateTime() {
        return OptionalNullable.getFrom(dateTime);
    }

    /**
     * Setter for DateTime.
     * @param dateTime Value for Map of String, LocalDateTime.
     */
    @JsonSetter("dateTime")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.UnixTimestampDeserializer.class)
    public void setDateTime(Map<String, LocalDateTime> dateTime) {
        this.dateTime = OptionalNullable.of(dateTime);
    }

    /**
     * UnSetter for DateTime.
     */
    public void unsetDateTime() {
        dateTime = null;
    }

    /**
     * Getter for DateTime1.
     * @return Returns the Map of String, LocalDateTime.
     */
    @JsonGetter("dateTime1")
    @JsonSerialize(contentUsing = LocalDateTimeHelper.UnixTimestampSerializer.class)
    public Map<String, LocalDateTime> getDateTime1() {
        return dateTime1;
    }

    /**
     * Setter for DateTime1.
     * @param dateTime1 Value for Map of String, LocalDateTime.
     */
    @JsonSetter("dateTime1")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.UnixTimestampDeserializer.class)
    public void setDateTime1(Map<String, LocalDateTime> dateTime1) {
        this.dateTime1 = dateTime1;
    }

    /**
     * Converts this UnixDateMap into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "UnixDateMap [" + "dateTime1=" + dateTime1 + ", dateTime=" + dateTime + "]";
    }

    /**
     * Builds a new {@link UnixDateMap.Builder} object. Creates the instance with the state of the
     * current model.
     * @return a new {@link UnixDateMap.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(dateTime1);
        builder.dateTime = internalGetDateTime();
        return builder;
    }

    /**
     * Class to build instances of {@link UnixDateMap}.
     */
    public static class Builder {
        private Map<String, LocalDateTime> dateTime1;
        private OptionalNullable<Map<String, LocalDateTime>> dateTime;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param dateTime1 Map of String, value for dateTime1.
         */
        public Builder(final Map<String, LocalDateTime> dateTime1) {
            this.dateTime1 = dateTime1;
        }

        /**
         * Setter for dateTime1.
         * @param dateTime1 Map of String, value for dateTime1.
         * @return Builder.
         */
        public Builder dateTime1(Map<String, LocalDateTime> dateTime1) {
            this.dateTime1 = dateTime1;
            return this;
        }

        /**
         * Setter for dateTime.
         * @param dateTime Map of String, value for dateTime.
         * @return Builder.
         */
        public Builder dateTime(Map<String, LocalDateTime> dateTime) {
            this.dateTime = OptionalNullable.of(dateTime);
            return this;
        }

        /**
         * UnSetter for dateTime.
         * @return Builder.
         */
        public Builder unsetDateTime() {
            dateTime = null;
            return this;
        }

        /**
         * Builds a new {@link UnixDateMap} object using the set fields.
         * @return {@link UnixDateMap}.
         */
        public UnixDateMap build() {
            return new UnixDateMap(dateTime1, dateTime);
        }
    }
}
