package apimatic.core.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.core.utilities.ZonedDateTimeHelper;

/**
 * This is a model class for Rfc1123DateMap type.
 */
public class Rfc1123DateMap {
    private Map<String, LocalDateTime> dateTime1;
    private OptionalNullable<Map<String, LocalDateTime>> dateTime;
    private OptionalNullable<Map<String, ZonedDateTime>> zonedDateTime;

    /**
     * Default constructor.
     */
    public Rfc1123DateMap() {}

    /**
     * Initialization constructor.
     * @param dateTime1 Map of String, value for dateTime1.
     * @param dateTime Map of String, value for dateTime.
     * @param zonedDateTime Map of String, value for zonedDateTime.
     */
    public Rfc1123DateMap(final Map<String, LocalDateTime> dateTime1,
            final Map<String, LocalDateTime> dateTime,
            final Map<String, ZonedDateTime> zonedDateTime) {
        this.dateTime1 = dateTime1;
        this.dateTime = OptionalNullable.of(dateTime);
        this.zonedDateTime = OptionalNullable.of(zonedDateTime);
    }

    /**
     * Internal initialization constructor.
     * @param dateTime1 Map of String, value for dateTime1.
     * @param dateTime Map of String, value for dateTime.
     * @param zonedDateTime Map of String, value for zonedDateTime.
     */
    protected Rfc1123DateMap(final Map<String, LocalDateTime> dateTime1,
            final OptionalNullable<Map<String, LocalDateTime>> dateTime,
            final OptionalNullable<Map<String, ZonedDateTime>> zonedDateTime) {
        this.dateTime1 = dateTime1;
        this.dateTime = dateTime;
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * Internal Getter for DateTime.
     * @return Returns the Internal Map of String, LocalDateTime.
     */
    @JsonGetter("dateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.Rfc1123DateTimeSerializer.class)
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
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeDeserializer.class)
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
     * Internal Getter for ZonedDateTime.
     * @return Returns the Internal Map of String, ZonedDateTime.
     */
    @JsonGetter("zonedDateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.ZonedRfc1123DateTimeSerializer.class)
    protected OptionalNullable<Map<String, ZonedDateTime>> internalGetZonedDateTime() {
        return this.zonedDateTime;
    }

    /**
     * Getter for ZonedDateTime.
     * @return Returns the Map of String, ZonedDateTime.
     */
    public Map<String, ZonedDateTime> getZonedDateTime() {
        return OptionalNullable.getFrom(zonedDateTime);
    }

    /**
     * Setter for ZonedDateTime.
     * @param dateTime Value for Map of String, ZonedDateTime.
     */
    @JsonSetter("zonedDateTime")
    @JsonDeserialize(contentUsing = ZonedDateTimeHelper.Rfc1123DateTimeDeserializer.class)
    public void setZonedDateTime(Map<String, ZonedDateTime> zonedDateTime) {
        this.zonedDateTime = OptionalNullable.of(zonedDateTime);
    }

    /**
     * UnSetter for ZonedDateTime.
     */
    public void unsetZonedDateTime() {
        zonedDateTime = null;
    }

    /**
     * Getter for DateTime1.
     * @return Returns the Map of String, LocalDateTime.
     */
    @JsonGetter("dateTime1")
    @JsonSerialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeSerializer.class)
    public Map<String, LocalDateTime> getDateTime1() {
        return dateTime1;
    }

    /**
     * Setter for DateTime1.
     * @param dateTime1 Value for Map of String, LocalDateTime.
     */
    @JsonSetter("dateTime1")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeDeserializer.class)
    public void setDateTime1(Map<String, LocalDateTime> dateTime1) {
        this.dateTime1 = dateTime1;
    }

    /**
     * Converts this Rfc1123DateMap into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Rfc1123DateMap [" + "dateTime1=" + dateTime1 +
                ", dateTime=" + dateTime +
                ", zonedDateTime=" + zonedDateTime + "]";
    }

    /**
     * Builds a new {@link Rfc1123DateMap.Builder} object. Creates the instance with the state of
     * the current model.
     * @return a new {@link Rfc1123DateMap.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(dateTime1);
        builder.dateTime = internalGetDateTime();
        builder.zonedDateTime = internalGetZonedDateTime();
        return builder;
    }

    /**
     * Class to build instances of {@link Rfc1123DateMap}.
     */
    public static class Builder {
        private Map<String, LocalDateTime> dateTime1;
        private OptionalNullable<Map<String, LocalDateTime>> dateTime;
        private OptionalNullable<Map<String, ZonedDateTime>> zonedDateTime;

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
         * Setter for zonedDateTime.
         * @param zonedDateTime Map of String, value for zonedDateTime.
         * @return Builder.
         */
        public Builder zonedDateTime(Map<String, ZonedDateTime> zonedDateTime) {
            this.zonedDateTime = OptionalNullable.of(zonedDateTime);
            return this;
        }

        /**
         * UnSetter for zonedDateTime.
         * @return Builder.
         */
        public Builder unsetZonedDateTime() {
            zonedDateTime = null;
            return this;
        }

        /**
         * Builds a new {@link Rfc1123DateMap} object using the set fields.
         * @return {@link Rfc1123DateMap}.
         */
        public Rfc1123DateMap build() {
            return new Rfc1123DateMap(dateTime1, dateTime, zonedDateTime);
        }
    }
}
