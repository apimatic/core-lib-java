package apimatic.core.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.core.utilities.ZonedDateTimeHelper;

/**
 * This is a model class for UnixDate type.
 */
public class UnixDate {
    private LocalDateTime dateTime1;
    private OptionalNullable<LocalDateTime> dateTime;
    private OptionalNullable<ZonedDateTime> zonedDateTime;

    /**
     * Default constructor.
     */
    public UnixDate() {}

    /**
     * Initialization constructor.
     * @param dateTime1 LocalDateTime value for dateTime1.
     * @param dateTime LocalDateTime value for dateTime.
     * @param dateTime ZonedDateTime value for zonedDateTime.
     */
    public UnixDate(final LocalDateTime dateTime1,
            final LocalDateTime dateTime,
            final ZonedDateTime zonedDateTime) {
        this.dateTime1 = dateTime1;
        this.dateTime = OptionalNullable.of(dateTime);
        this.zonedDateTime = OptionalNullable.of(zonedDateTime);
    }

    /**
     * Internal initialization constructor.
     * @param dateTime1 LocalDateTime value for dateTime1.
     * @param dateTime LocalDateTime value for dateTime.
     * @param dateTime ZonedDateTime value for zonedDateTime.
     */
    protected UnixDate(final LocalDateTime dateTime1,
            final OptionalNullable<LocalDateTime> dateTime,
            final OptionalNullable<ZonedDateTime> zonedDateTime) {
        this.dateTime1 = dateTime1;
        this.dateTime = dateTime;
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * Internal Getter for DateTime.
     * @return Returns the Internal LocalDateTime.
     */
    @JsonGetter("dateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.UnixTimestampSerializer.class)
    protected OptionalNullable<LocalDateTime> internalGetDateTime() {
        return this.dateTime;
    }

    /**
     * Getter for DateTime.
     * @return Returns the LocalDateTime.
     */
    public LocalDateTime getDateTime() {
        return OptionalNullable.getFrom(dateTime);
    }

    /**
     * Setter for DateTime.
     * @param dateTime Value for LocalDateTime.
     */
    @JsonSetter("dateTime")
    @JsonDeserialize(using = LocalDateTimeHelper.UnixTimestampDeserializer.class)
    public void setDateTime(LocalDateTime dateTime) {
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
     * @return Returns the Internal ZonedDateTime.
     */
    @JsonGetter("zonedDateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.ZonedUnixTimestampSerializer.class)
    protected OptionalNullable<ZonedDateTime> internalGetZonedDateTime() {
        return this.zonedDateTime;
    }

    /**
     * Getter for ZonedDateTime.
     * @return Returns the ZonedDateTime.
     */
    public ZonedDateTime getZonedDateTime() {
        return OptionalNullable.getFrom(zonedDateTime);
    }

    /**
     * Setter for ZonedDateTime.
     * @param dateTime Value for ZonedDateTime.
     */
    @JsonSetter("zonedDateTime")
    @JsonDeserialize(using = ZonedDateTimeHelper.UnixTimestampDeserializer.class)
    public void setDateTime(ZonedDateTime zonedDateTime) {
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
     * @return Returns the LocalDateTime.
     */
    @JsonGetter("dateTime1")
    @JsonSerialize(using = LocalDateTimeHelper.UnixTimestampSerializer.class)
    public LocalDateTime getDateTime1() {
        return dateTime1;
    }

    /**
     * Setter for DateTime1.
     * @param dateTime1 Value for LocalDateTime.
     */
    @JsonSetter("dateTime1")
    @JsonDeserialize(using = LocalDateTimeHelper.UnixTimestampDeserializer.class)
    public void setDateTime1(LocalDateTime dateTime1) {
        this.dateTime1 = dateTime1;
    }

    /**
     * Converts this UnixDate into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "UnixDate [" +
                "dateTime1=" + dateTime1 +
                ", dateTime=" + dateTime +
                ", zonedDateTime=" + zonedDateTime + "]";
    }

    /**
     * Builds a new {@link UnixDate.Builder} object. Creates the instance with the state of the
     * current model.
     * @return a new {@link UnixDate.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(dateTime1);
        builder.dateTime = internalGetDateTime();
        return builder;
    }

    /**
     * Class to build instances of {@link UnixDate}.
     */
    public static class Builder {
        private LocalDateTime dateTime1;
        private OptionalNullable<LocalDateTime> dateTime;
        private OptionalNullable<ZonedDateTime> zonedDateTime;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param dateTime1 LocalDateTime value for dateTime1.
         */
        public Builder(final LocalDateTime dateTime1) {
            this.dateTime1 = dateTime1;
        }

        /**
         * Setter for dateTime1.
         * @param dateTime1 LocalDateTime value for dateTime1.
         * @return Builder.
         */
        public Builder dateTime1(LocalDateTime dateTime1) {
            this.dateTime1 = dateTime1;
            return this;
        }

        /**
         * Setter for dateTime.
         * @param dateTime LocalDateTime value for dateTime.
         * @return Builder.
         */
        public Builder dateTime(LocalDateTime dateTime) {
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
         * @param zonedDateTime ZonedDateTime value for zonedDateTime.
         * @return Builder.
         */
        public Builder zonedDateTime(ZonedDateTime zonedDateTime) {
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
         * Builds a new {@link UnixDate} object using the set fields.
         * @return {@link UnixDate}.
         */
        public UnixDate build() {
            return new UnixDate(dateTime1, dateTime, zonedDateTime);
        }
    }
}
