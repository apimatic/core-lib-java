package apimatic.core.models;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.LocalDateTimeHelper;

/**
 * This is a model class for SimpleDateArray type.
 */
public class SimpleDateArray {
    private OptionalNullable<List<LocalDate>> date;
    private List<LocalDate> date1;

    /**
     * Default constructor.
     */
    public SimpleDateArray() {}

    /**
     * Initialization constructor.
     * @param date List of LocalDate value for date.
     * @param date1 List of LocalDate value for date1.
     */
    public SimpleDateArray(final List<LocalDate> date, final List<LocalDate> date1) {
        this.date = OptionalNullable.of(date);
        this.date1 = date1;
    }

    /**
     * Internal initialization constructor.
     */
    protected SimpleDateArray(final OptionalNullable<List<LocalDate>> date,
            final List<LocalDate> date1) {
        this.date = date;
        this.date1 = date1;
    }

    /**
     * Internal Getter for Date.
     * @return Returns the Internal List of LocalDate
     */
    @JsonGetter("date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.SimpleDateSerializer.class)
    protected OptionalNullable<List<LocalDate>> internalGetDate() {
        return this.date;
    }

    /**
     * Getter for Date.
     * @return Returns the List of LocalDate
     */
    public List<LocalDate> getDate() {
        return OptionalNullable.getFrom(date);
    }

    /**
     * Setter for Date.
     * @param date Value for List of LocalDate
     */
    @JsonSetter("date")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.SimpleDateDeserializer.class)
    public void setDate(List<LocalDate> date) {
        this.date = OptionalNullable.of(date);
    }

    /**
     * UnSetter for Date.
     */
    public void unsetDate() {
        date = null;
    }

    /**
     * Getter for Date1.
     * @return Returns the List of LocalDate
     */
    @JsonGetter("date1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(contentUsing = LocalDateTimeHelper.SimpleDateSerializer.class)
    public List<LocalDate> getDate1() {
        return date1;
    }

    /**
     * Setter for Date1.
     * @param date1 Value for List of LocalDate
     */
    @JsonSetter("date1")
    @JsonDeserialize(contentUsing = LocalDateTimeHelper.SimpleDateDeserializer.class)
    public void setDate1(List<LocalDate> date1) {
        this.date1 = date1;
    }

    /**
     * Converts this SimpleDateArray into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "SimpleDateArray [" + "date=" + date + ", date1=" + date1 + "]";
    }

    /**
     * Builds a new {@link SimpleDateArray.Builder} object. Creates the instance with the state of
     * the current model.
     * @return a new {@link SimpleDateArray.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder().date1(getDate1());
        builder.date = internalGetDate();
        return builder;
    }

    /**
     * Class to build instances of {@link SimpleDateArray}.
     */
    public static class Builder {
        private OptionalNullable<List<LocalDate>> date;
        private List<LocalDate> date1;



        /**
         * Setter for date.
         * @param date List of LocalDate value for date.
         * @return Builder
         */
        public Builder date(List<LocalDate> date) {
            this.date = OptionalNullable.of(date);
            return this;
        }

        /**
         * UnSetter for date.
         * @return Builder
         */
        public Builder unsetDate() {
            date = null;
            return this;
        }

        /**
         * Setter for date1.
         * @param date1 List of LocalDate value for date1.
         * @return Builder
         */
        public Builder date1(List<LocalDate> date1) {
            this.date1 = date1;
            return this;
        }

        /**
         * Builds a new {@link SimpleDateArray} object using the set fields.
         * @return {@link SimpleDateArray}
         */
        public SimpleDateArray build() {
            return new SimpleDateArray(date, date1);
        }
    }
}
