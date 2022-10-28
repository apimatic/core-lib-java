package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for Vehicle type.
 */
public class Vehicle {
    private String numberOfTyres;

    /**
     * Default constructor.
     */
    public Vehicle() {}

    /**
     * Initialization constructor.
     * @param numberOfTyres String value for numberOfTyres.
     */
    @JsonCreator
    public Vehicle(@JsonProperty("NumberOfTyres") String numberOfTyres) {
        this.numberOfTyres = numberOfTyres;
    }

    /**
     * Getter for NumberOfTyres.
     * @return Returns the String
     */
    @JsonGetter("NumberOfTyres")
    public String getNumberOfTyres() {
        return numberOfTyres;
    }

    /**
     * Setter for NumberOfTyres.
     * @param numberOfTyres Value for String
     */
    @JsonSetter("NumberOfTyres")
    public void setNumberOfTyres(String numberOfTyres) {
        this.numberOfTyres = numberOfTyres;
    }

    /**
     * Converts this Vehicle into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Vehicle [" + "numberOfTyres=" + numberOfTyres + "]";
    }

    /**
     * Builds a new {@link Vehicle.Builder} object. Creates the instance with the state of the
     * current model.
     * @return a new {@link Vehicle.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(numberOfTyres);
        return builder;
    }

    /**
     * Class to build instances of {@link Vehicle}.
     */
    public static class Builder {
        private String numberOfTyres;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param numberOfTyres String value for numberOfTyres.
         */
        public Builder(String numberOfTyres) {
            this.numberOfTyres = numberOfTyres;
        }

        /**
         * Setter for numberOfTyres.
         * @param numberOfTyres String value for numberOfTyres.
         * @return Builder
         */
        public Builder numberOfTyres(String numberOfTyres) {
            this.numberOfTyres = numberOfTyres;
            return this;
        }

        /**
         * Builds a new {@link Vehicle} object using the set fields.
         * @return {@link Vehicle}
         */
        public Vehicle build() {
            return new Vehicle(numberOfTyres);
        }
    }
}
