package apimatic.core.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.core.utilities.ZonedDateTimeHelper;

/**
 * This is a model class for Rfc1123DateArray type.
 */
public class Rfc1123DateArray {
    private List<LocalDateTime> dateTime1;
    private OptionalNullable<List<LocalDateTime>> dateTime;
    private OptionalNullable<List<ZonedDateTime>> zonedDateTime;

    /**
     * Default constructor.
     */
    public Rfc1123DateArray() {}

    /**
     * Initialization constructor.
     * @param dateTime1 List of LocalDateTime value for dateTime1.
     * @param dateTime List of LocalDateTime value for dateTime.
     * @param zonedDateTime List of ZonedDateTime value for zonedDateTime.
     */
    public Rfc1123DateArray(final List<LocalDateTime> dateTime1,
            final List<LocalDateTime> dateTime,
            final List<ZonedDateTime> zonedDateTime) {
        this.dateTime1 = dateTime1;
        this.dateTime = OptionalNullable.of(dateTime);
        this.zonedDateTime = OptionalNullable.of(zonedDateTime);
    }

    /**
     * Internal initialization constructor.
     * @param dateTime1 List of LocalDateTime value for dateTime1.
     * @param dateTime List of LocalDateTime value for dateTime.
     * @param zonedDateTime List of ZonedDateTime value for zonedDateTime.
     */
    protected Rfc1123DateArray(final List<LocalDateTime> dateTime1,
            final OptionalNullable<List<LocalDateTime>> dateTime,
            final OptionalNullable<List<ZonedDateTime>> zonedDateTime) {
        this.dateTime1 = dateTime1;
        this.dateTime = dateTime;
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * Internal Getter for DateTime.
     * @return Returns the Internal List of LocalDateTime.
     */
    @JsonGetter("dateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.Rfc1123DateTimeSerializer.class)
    protected OptionalNullable<List<LocalDateTime>> internalGetDateTime() {
        return this.dateTime;
    }

    /**
     * Getter for DateTime.
     * @return Returns the List of LocalDateTime.
     */
    public List<LocalDateTime> getDateTime() {
        return OptionalNullable.getFrom(dateTime);
    }

    /**
     * Setter for DateTime.
     * @param dateTime Value for List of LocalDateTime.
     */
    @JsonSetter("dateTime")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeDeserializer.class)
    public void setDateTime(List<LocalDateTime> dateTime) {
        this.dateTime = OptionalNullable.of(dateTime);
    }

    /**
     * UnSetter for DateTime.
     */
    public void unsetDateTime() {
        dateTime = null;
    }

    /**
     * Internal Getter for zonedDateTime.
     * @return Returns the Internal List of ZonedDateTime.
     */
    @JsonGetter("zonedDateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.ZonedRfc1123DateTimeSerializer.class)
    protected OptionalNullable<List<ZonedDateTime>> internalGetZonedDateTime() {
        return this.zonedDateTime;
    }

    /**
     * Getter for zonedDateTime.
     * @return Returns the List of ZonedDateTime.
     */
    public List<ZonedDateTime> getZonedDateTime() {
        return OptionalNullable.getFrom(zonedDateTime);
    }

    /**
     * Setter for zonedDateTime.
     * @param zonedDateTime Value for List of ZonedDateTime.
     */
    @JsonSetter("zonedDateTime")
    @JsonDeserialize(contentUsing = ZonedDateTimeHelper.Rfc1123DateTimeDeserializer.class)
    public void setZonedDateTime(List<ZonedDateTime> zonedDateTime) {
        this.zonedDateTime = OptionalNullable.of(zonedDateTime);
    }

    /**
     * UnSetter for zonedDateTime.
     */
    public void unsetZonedDateTime() {
        zonedDateTime = null;
    }

    /**
     * Getter for DateTime1.
     * @return Returns the List of LocalDateTime.
     */
    @JsonGetter("dateTime1")
    @JsonSerialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeSerializer.class)
    public List<LocalDateTime> getDateTime1() {
        return dateTime1;
    }

    /**
     * Setter for DateTime1.
     * @param dateTime1 Value for List of LocalDateTime.
     */
    @JsonSetter("dateTime1")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeDeserializer.class)
    public void setDateTime1(List<LocalDateTime> dateTime1) {
        this.dateTime1 = dateTime1;
    }

    /**
     * Converts this Rfc1123DateArray into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "Rfc1123DateArray [" + "dateTime1=" + dateTime1 +
                ", dateTime=" + dateTime +
                ", zonedDateTime=" + zonedDateTime + "]";
    }

    /**
     * Builds a new {@link Rfc1123DateArray.Builder} object. Creates the instance with the state of
     * the current model.
     * @return a new {@link Rfc1123DateArray.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(dateTime1);
        builder.dateTime = internalGetDateTime();
        return builder;
    }

    /**
     * Class to build instances of {@link Rfc1123DateArray}.
     */
    public static class Builder {
        private List<LocalDateTime> dateTime1;
        private OptionalNullable<List<LocalDateTime>> dateTime;
        private OptionalNullable<List<ZonedDateTime>> zonedDateTime;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param dateTime1 List of LocalDateTime value for dateTime1.
         */
        public Builder(final List<LocalDateTime> dateTime1) {
            this.dateTime1 = dateTime1;
        }

        /**
         * Setter for dateTime1.
         * @param dateTime1 List of LocalDateTime value for dateTime1.
         * @return Builder.
         */
        public Builder dateTime1(List<LocalDateTime> dateTime1) {
            this.dateTime1 = dateTime1;
            return this;
        }

        /**
         * Setter for dateTime.
         * @param dateTime List of LocalDateTime value for dateTime.
         * @return Builder.
         */
        public Builder dateTime(List<LocalDateTime> dateTime) {
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
         * @param zonedDateTime List of ZonedDateTime value for zonedDateTime.
         * @return Builder.
         */
        public Builder zonedDateTime(List<ZonedDateTime> zonedDateTime) {
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
         * Builds a new {@link Rfc1123DateArray} object using the set fields.
         * @return {@link Rfc1123DateArray}.
         */
        public Rfc1123DateArray build() {
            return new Rfc1123DateArray(dateTime1, dateTime, zonedDateTime);
        }
    }
}
