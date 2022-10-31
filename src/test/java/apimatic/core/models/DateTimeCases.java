package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import apimatic.core.models.containers.DateTimeCasesMapvsArray;

/**
 * This is a model class for DateTimeCases type.
 */
public class DateTimeCases {

    private DateTimeCasesMapvsArray mapvsArray;

    /**
     * Default constructor.
     */
    public DateTimeCases() {}

    /**
     * @param mapvsArray DateTimeCasesMapvsArray value for mapvsArray.
     */
    public DateTimeCases(final DateTimeCasesMapvsArray mapvsArray) {
        this.mapvsArray = mapvsArray;
    }


    /**
     * Getter for MapvsArray.
     * @return Returns the DateTimeCasesMapvsArray
     */
    @JsonGetter("mapvsArray")
    public DateTimeCasesMapvsArray getMapvsArray() {
        return mapvsArray;
    }

    /**
     * Setter for MapvsArray.
     * @param mapvsArray Value for DateTimeCasesMapvsArray
     */
    @JsonSetter("mapvsArray")
    public void setMapvsArray(DateTimeCasesMapvsArray mapvsArray) {
        this.mapvsArray = mapvsArray;
    }

    /**
     * Converts this DateTimeCases into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "DateTimeCases [" + ", mapvsArray=" + mapvsArray + "]";
    }

    /**
     * Builds a new {@link DateTimeCases.Builder} object. Creates the instance with the state of the
     * current model.
     * @return a new {@link DateTimeCases.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(mapvsArray);
        return builder;
    }

    /**
     * Class to build instances of {@link DateTimeCases}.
     */
    public static class Builder {
        private DateTimeCasesMapvsArray mapvsArray;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param mapvsArray DateTimeCasesMapvsArray value for mapvsArray.
         */
        public Builder(final DateTimeCasesMapvsArray mapvsArray) {
            this.mapvsArray = mapvsArray;
        }

        /**
         * Setter for mapvsArray.
         * @param mapvsArray DateTimeCasesMapvsArray value for mapvsArray.
         * @return Builder
         */
        public Builder mapvsArray(DateTimeCasesMapvsArray mapvsArray) {
            this.mapvsArray = mapvsArray;
            return this;
        }

        /**
         * Builds a new {@link DateTimeCases} object using the set fields.
         * @return {@link DateTimeCases}
         */
        public DateTimeCases build() {
            return new DateTimeCases(mapvsArray);
        }
    }
}
