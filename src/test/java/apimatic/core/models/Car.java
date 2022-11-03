package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for Car type.
 */
public class Car extends Vehicle {
    private boolean haveTrunk;

    /**
     * Default constructor.
     */
    public Car() {
        super();
    }

    /**
     * Initialization constructor.
     * @param numberOfTyres String value for numberOfTyres.
     * @param haveTrunk boolean value for haveTrunk.
     */
    @JsonCreator
    public Car(@JsonProperty("NumberOfTyres") final String numberOfTyres,
            @JsonProperty("HaveTrunk") boolean haveTrunk) {
        super(numberOfTyres);
        this.haveTrunk = haveTrunk;
    }

    /**
     * Getter for HaveTrunk.
     * @return Returns the boolean.
     */
    @JsonGetter("HaveTrunk")
    public boolean getHaveTrunk() {
        return haveTrunk;
    }

    /**
     * Setter for HaveTrunk.
     * @param haveTrunk Value for boolean.
     */
    @JsonSetter("HaveTrunk")
    public void setHaveTrunk(boolean haveTrunk) {
        this.haveTrunk = haveTrunk;
    }

    /**
     * Converts this Car into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "Car [" + "haveTrunk=" + haveTrunk + ", numberOfTyres=" + getNumberOfTyres() + "]";
    }

    /**
     * Builds a new {@link Car.Builder} object. Creates the instance with the state of the current
     * model.
     * @return a new {@link Car.Builder} object.
     */
    public Builder toCarBuilder() {
        Builder builder = new Builder(getNumberOfTyres(), haveTrunk);
        return builder;
    }

    /**
     * Class to build instances of {@link Car}.
     */
    public static class Builder {
        private String numberOfTyres;
        private boolean haveTrunk;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param numberOfTyres String value for numberOfTyres.
         * @param haveTrunk boolean value for haveTrunk.
         */
        public Builder(final String numberOfTyres, boolean haveTrunk) {
            this.numberOfTyres = numberOfTyres;
            this.haveTrunk = haveTrunk;
        }

        /**
         * Setter for numberOfTyres.
         * @param numberOfTyres String value for numberOfTyres.
         * @return Builder.
         */
        public Builder numberOfTyres(String numberOfTyres) {
            this.numberOfTyres = numberOfTyres;
            return this;
        }

        /**
         * Setter for haveTrunk.
         * @param haveTrunk boolean value for haveTrunk.
         * @return Builder.
         */
        public Builder haveTrunk(boolean haveTrunk) {
            this.haveTrunk = haveTrunk;
            return this;
        }

        /**
         * Builds a new {@link Car} object using the set fields.
         * @return {@link Car}.
         */
        public Car build() {
            return new Car(numberOfTyres, haveTrunk);
        }
    }
}
