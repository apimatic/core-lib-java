package apimatic.core.models;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;

/**
 * This is a model class for Rfc8601DateArray type.
 */
public class Rfc8601DateArray {
    private OptionalNullable<List<LocalDateTime>> dateTime;
    private List<LocalDateTime> dateTime1;

    /**
     * Default constructor.
     */
    public Rfc8601DateArray() {}

    /**
     * Initialization constructor.
     * 
     * @param dateTime1 List of LocalDateTime value for dateTime1.
     * @param dateTime List of LocalDateTime value for dateTime.
     */
    public Rfc8601DateArray(List<LocalDateTime> dateTime1, List<LocalDateTime> dateTime) {
        this.dateTime = OptionalNullable.of(dateTime);
        this.dateTime1 = dateTime1;
    }

    /**
     * Internal initialization constructor.
     */
    protected Rfc8601DateArray(List<LocalDateTime> dateTime1,
            OptionalNullable<List<LocalDateTime>> dateTime) {
        this.dateTime = dateTime;
        this.dateTime1 = dateTime1;
    }

    /**
     * Internal Getter for DateTime.
     * 
     * @return Returns the Internal List of LocalDateTime
     */
    @JsonGetter("dateTime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.Rfc8601DateTimeSerializer.class)
    protected OptionalNullable<List<LocalDateTime>> internalGetDateTime() {
        return this.dateTime;
    }

    /**
     * Getter for DateTime.
     * 
     * @return Returns the List of LocalDateTime
     */
    public List<LocalDateTime> getDateTime() {
        return OptionalNullable.getFrom(dateTime);
    }

    /**
     * Setter for DateTime.
     * 
     * @param dateTime Value for List of LocalDateTime
     */
    @JsonSetter("dateTime")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.Rfc8601DateTimeDeserializer.class)
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
     * Getter for DateTime1.
     * 
     * @return Returns the List of LocalDateTime
     */
    @JsonGetter("dateTime1")
    @JsonSerialize(contentUsing = LocalDateTimeHelper.Rfc8601DateTimeSerializer.class)
    public List<LocalDateTime> getDateTime1() {
        return dateTime1;
    }

    /**
     * Setter for DateTime1.
     * 
     * @param dateTime1 Value for List of LocalDateTime
     */
    @JsonSetter("dateTime1")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.Rfc8601DateTimeDeserializer.class)
    public void setDateTime1(List<LocalDateTime> dateTime1) {
        this.dateTime1 = dateTime1;
    }

    /**
     * Converts this Rfc8601DateArray into string format.
     * 
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Rfc8601DateArray [" + "dateTime1=" + dateTime1 + ", dateTime=" + dateTime + "]";
    }

    /**
     * Builds a new {@link Rfc8601DateArray.Builder} object. Creates the instance with the state of
     * the current model.
     * 
     * @return a new {@link Rfc8601DateArray.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(dateTime1);
        builder.dateTime = internalGetDateTime();
        return builder;
    }

    /**
     * Class to build instances of {@link Rfc8601DateArray}.
     */
    public static class Builder {
        private List<LocalDateTime> dateTime1;
        private OptionalNullable<List<LocalDateTime>> dateTime;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * 
         * @param dateTime1 List of LocalDateTime value for dateTime1.
         */
        public Builder(List<LocalDateTime> dateTime1) {
            this.dateTime1 = dateTime1;
        }

        /**
         * Setter for dateTime1.
         * 
         * @param dateTime1 List of LocalDateTime value for dateTime1.
         * @return Builder
         */
        public Builder dateTime1(List<LocalDateTime> dateTime1) {
            this.dateTime1 = dateTime1;
            return this;
        }

        /**
         * Setter for dateTime.
         * 
         * @param dateTime List of LocalDateTime value for dateTime.
         * @return Builder
         */
        public Builder dateTime(List<LocalDateTime> dateTime) {
            this.dateTime = OptionalNullable.of(dateTime);
            return this;
        }

        /**
         * UnSetter for dateTime.
         * 
         * @return Builder
         */
        public Builder unsetDateTime() {
            dateTime = null;
            return this;
        }

        /**
         * Builds a new {@link Rfc8601DateArray} object using the set fields.
         * 
         * @return {@link Rfc8601DateArray}
         */
        public Rfc8601DateArray build() {
            return new Rfc8601DateArray(dateTime1, dateTime);
        }
    }
}
