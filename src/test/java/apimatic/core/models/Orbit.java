package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for Orbit type.
 */
public class Orbit {
    private int numberOfElectrons;

    /**
     * Default constructor.
     */
    public Orbit() {}

    /**
     * Initialization constructor.
     * @param numberOfElectrons int value for numberOfElectrons.
     */
    @JsonCreator
    public Orbit(@JsonProperty("NumberOfElectrons") int numberOfElectrons) {
        this.numberOfElectrons = numberOfElectrons;
    }

    /**
     * Getter for NumberOfElectrons.
     * @return Returns the int.
     */
    @JsonGetter("NumberOfElectrons")
    public int getNumberOfElectrons() {
        return numberOfElectrons;
    }

    /**
     * Setter for NumberOfElectrons.
     * @param numberOfElectrons Value for int.
     */
    @JsonSetter("NumberOfElectrons")
    public void setNumberOfElectrons(int numberOfElectrons) {
        this.numberOfElectrons = numberOfElectrons;
    }

    /**
     * Converts this Orbit into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "Orbit [" + "numberOfElectrons=" + numberOfElectrons + "]";
    }

    /**
     * Builds a new {@link Orbit.Builder} object. Creates the instance with the state of the current
     * model.
     * @return a new {@link Orbit.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(numberOfElectrons);
        return builder;
    }

    /**
     * Class to build instances of {@link Orbit}.
     */
    public static class Builder {
        private int numberOfElectrons;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param numberOfElectrons int value for numberOfElectrons.
         */
        public Builder(int numberOfElectrons) {
            this.numberOfElectrons = numberOfElectrons;
        }

        /**
         * Setter for numberOfElectrons.
         * @param numberOfElectrons int value for numberOfElectrons.
         * @return Builder.
         */
        public Builder numberOfElectrons(int numberOfElectrons) {
            this.numberOfElectrons = numberOfElectrons;
            return this;
        }

        /**
         * Builds a new {@link Orbit} object using the set fields.
         * @return {@link Orbit}.
         */
        public Orbit build() {
            return new Orbit(numberOfElectrons);
        }
    }
}
