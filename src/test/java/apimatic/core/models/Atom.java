package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for Atom type.
 */
public class Atom {
    /**
     * A number of electrons integer
     */
    private int numberOfElectrons;
    /**
     * A number of protons integer
     */
    private Integer numberOfProtons;

    /**
     * Default constructor.
     */
    public Atom() {}

    /**
     * Initialization constructor.
     * @param numberOfElectrons int value for numberOfElectrons.
     * @param numberOfProtons Integer value for numberOfProtons.
     */
    public Atom(int numberOfElectrons, final Integer numberOfProtons) {
        this.numberOfElectrons = numberOfElectrons;
        this.numberOfProtons = numberOfProtons;
    }

    /**
     * Initialization constructor.
     * @param numberOfElectrons int value for numberOfElectrons.
     */
    @JsonCreator
    private Atom(@JsonProperty("NumberOfElectrons") int numberOfElectrons) {
        this.numberOfElectrons = numberOfElectrons;
    }

    /**
     * Getter for NumberOfElectrons.
     * @return Returns the int
     */
    @JsonGetter("NumberOfElectrons")
    public int getNumberOfElectrons() {
        return numberOfElectrons;
    }

    /**
     * Setter for NumberOfElectrons.
     * @param numberOfElectrons Value for int
     */
    @JsonSetter("NumberOfElectrons")
    public void setNumberOfElectrons(int numberOfElectrons) {
        this.numberOfElectrons = numberOfElectrons;
    }

    /**
     * Getter for NumberOfProtons.
     * @return Returns the Integer
     */
    @JsonGetter("NumberOfProtons")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getNumberOfProtons() {
        return numberOfProtons;
    }

    /**
     * Setter for NumberOfProtons.
     * @param numberOfProtons Value for Integer
     */
    @JsonSetter("NumberOfProtons")
    public void setNumberOfProtons(Integer numberOfProtons) {
        this.numberOfProtons = numberOfProtons;
    }

    /**
     * Converts this Atom into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Atom [" + "numberOfElectrons=" + numberOfElectrons + ", numberOfProtons="
                + numberOfProtons + "]";
    }

    /**
     * Builds a new {@link Atom.Builder} object. Creates the instance with the state of the current
     * model.
     * @return a new {@link Atom.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(numberOfElectrons).numberOfProtons(getNumberOfProtons());
        return builder;
    }

    /**
     * Class to build instances of {@link Atom}.
     */
    public static class Builder {
        /**
         * A number of electrons
         */
        private int numberOfElectrons;
        /**
         * A number of protons
         */
        private Integer numberOfProtons;

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
         * @return Builder
         */
        public Builder numberOfElectrons(int numberOfElectrons) {
            this.numberOfElectrons = numberOfElectrons;
            return this;
        }

        /**
         * Setter for numberOfProtons.
         * @param numberOfProtons Integer value for numberOfProtons.
         * @return Builder
         */
        public Builder numberOfProtons(Integer numberOfProtons) {
            this.numberOfProtons = numberOfProtons;
            return this;
        }

        /**
         * Builds a new {@link Atom} object using the set fields.
         * @return {@link Atom}
         */
        public Atom build() {
            return new Atom(numberOfElectrons, numberOfProtons);
        }
    }
}
