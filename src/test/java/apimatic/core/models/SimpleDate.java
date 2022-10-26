package apimatic.core.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;

/**
 * This is a model class for SimpleDate type.
 */
public class SimpleDate {
    private OptionalNullable<LocalDate> dateNullable;
    private LocalDate date;

    /**
     * Default constructor.
     */
    public SimpleDate() {
    }

    /**
     * Initialization constructor.
     * @param  dateNullable  LocalDate value for dateNullable.
     * @param  date  LocalDate value for date.
     */
    public SimpleDate(
            LocalDate dateNullable,
            LocalDate date) {
        this.dateNullable = OptionalNullable.of(dateNullable);
        this.date = date;
    }

    /**
     * Internal initialization constructor.
     */
    protected SimpleDate(OptionalNullable<LocalDate> dateNullable, LocalDate date) {
        this.dateNullable = dateNullable;
        this.date = date;
    }

    /**
     * Internal Getter for DateNullable.
     * @return Returns the Internal LocalDate
     */
    @JsonGetter("dateNullable")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.SimpleDateSerializer.class)
    protected OptionalNullable<LocalDate> internalGetDateNullable() {
        return this.dateNullable;
    }

    /**
     * Getter for DateNullable.
     * @return Returns the LocalDate
     */
    public LocalDate getDateNullable() {
        return OptionalNullable.getFrom(dateNullable);
    }

    /**
     * Setter for DateNullable.
     * @param dateNullable Value for LocalDate
     */
    @JsonSetter("dateNullable")
    @JsonDeserialize(using = LocalDateTimeHelper.SimpleDateDeserializer.class)
    public void setDateNullable(LocalDate dateNullable) {
        this.dateNullable = OptionalNullable.of(dateNullable);
    }

    /**
     * UnSetter for DateNullable.
     */
    public void unsetDateNullable() {
        dateNullable = null;
    }

    /**
     * Getter for Date.
     * @return Returns the LocalDate
     */
    @JsonGetter("date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = LocalDateTimeHelper.SimpleDateSerializer.class)
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setter for Date.
     * @param date Value for LocalDate
     */
    @JsonSetter("date")
    @JsonDeserialize(using = LocalDateTimeHelper.SimpleDateDeserializer.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Converts this SimpleDate into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "SimpleDate [" + "dateNullable=" + dateNullable + ", date=" + date + "]";
    }

    /**
     * Builds a new {@link SimpleDate.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link SimpleDate.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder()
                .date(getDate());
        builder.dateNullable = internalGetDateNullable();
        return builder;
    }

    /**
     * Class to build instances of {@link SimpleDate}.
     */
    public static class Builder {
        private OptionalNullable<LocalDate> dateNullable;
        private LocalDate date;



        /**
         * Setter for dateNullable.
         * @param  dateNullable  LocalDate value for dateNullable.
         * @return Builder
         */
        public Builder dateNullable(LocalDate dateNullable) {
            this.dateNullable = OptionalNullable.of(dateNullable);
            return this;
        }

        /**
         * UnSetter for dateNullable.
         * @return Builder
         */
        public Builder unsetDateNullable() {
            dateNullable = null;
            return this;
        }

        /**
         * Setter for date.
         * @param  date  LocalDate value for date.
         * @return Builder
         */
        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        /**
         * Builds a new {@link SimpleDate} object using the set fields.
         * @return {@link SimpleDate}
         */
        public SimpleDate build() {
            return new SimpleDate(dateNullable, date);
        }
    }
}
