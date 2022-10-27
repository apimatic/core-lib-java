package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for GrandParentClass type.
 */
public class GrandParentClass {
    private String grandParentOptional;
    private String grandParentRequiredNullable;
    private String grandParentRequired;

    /**
     * Default constructor.
     */
    public GrandParentClass() {
        grandParentRequired = "not nullable and required";
    }

    /**
     * Initialization constructor.
     * @param  grandParentRequiredNullable  String value for grandParentRequiredNullable.
     * @param  grandParentRequired  String value for grandParentRequired.
     * @param  grandParentOptional  String value for grandParentOptional.
     */
    public GrandParentClass(
            String grandParentRequiredNullable,
            String grandParentRequired,
            String grandParentOptional) {
        this.grandParentOptional = grandParentOptional;
        this.grandParentRequiredNullable = grandParentRequiredNullable;
        this.grandParentRequired = grandParentRequired;
    }

    /**
     * Getter for GrandParentOptional.
     * @return Returns the String
     */
    @JsonGetter("Grand_Parent_Optional")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getGrandParentOptional() {
        return grandParentOptional;
    }

    /**
     * Setter for GrandParentOptional.
     * @param grandParentOptional Value for String
     */
    @JsonSetter("Grand_Parent_Optional")
    public void setGrandParentOptional(String grandParentOptional) {
        this.grandParentOptional = grandParentOptional;
    }

    /**
     * Getter for GrandParentRequiredNullable.
     * @return Returns the String
     */
    @JsonGetter("Grand_Parent_Required_Nullable")
    public String getGrandParentRequiredNullable() {
        return grandParentRequiredNullable;
    }

    /**
     * Setter for GrandParentRequiredNullable.
     * @param grandParentRequiredNullable Value for String
     */
    @JsonSetter("Grand_Parent_Required_Nullable")
    public void setGrandParentRequiredNullable(String grandParentRequiredNullable) {
        this.grandParentRequiredNullable = grandParentRequiredNullable;
    }

    /**
     * Getter for GrandParentRequired.
     * @return Returns the String
     */
    @JsonGetter("Grand_Parent_Required")
    public String getGrandParentRequired() {
        return grandParentRequired;
    }

    /**
     * Setter for GrandParentRequired.
     * @param grandParentRequired Value for String
     */
    @JsonSetter("Grand_Parent_Required")
    public void setGrandParentRequired(String grandParentRequired) {
        this.grandParentRequired = grandParentRequired;
    }

    /**
     * Converts this GrandParentClass into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "GrandParentClass [" + "grandParentRequiredNullable=" + grandParentRequiredNullable
                + ", grandParentRequired=" + grandParentRequired + ", grandParentOptional="
                + grandParentOptional + "]";
    }

    /**
     * Builds a new {@link GrandParentClass.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link GrandParentClass.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(grandParentRequiredNullable, grandParentRequired)
                .grandParentOptional(getGrandParentOptional());
        return builder;
    }

    /**
     * Class to build instances of {@link GrandParentClass}.
     */
    public static class Builder {
        private String grandParentRequiredNullable;
        private String grandParentRequired = "not nullable and required";
        private String grandParentOptional;

        /**
         * Initialization constructor.
         */
        public Builder() {
        }

        /**
         * Initialization constructor.
         * @param  grandParentRequiredNullable  String value for grandParentRequiredNullable.
         * @param  grandParentRequired  String value for grandParentRequired.
         */
        public Builder(String grandParentRequiredNullable, String grandParentRequired) {
            this.grandParentRequiredNullable = grandParentRequiredNullable;
            this.grandParentRequired = grandParentRequired;
        }

        /**
         * Setter for grandParentRequiredNullable.
         * @param  grandParentRequiredNullable  String value for grandParentRequiredNullable.
         * @return Builder
         */
        public Builder grandParentRequiredNullable(String grandParentRequiredNullable) {
            this.grandParentRequiredNullable = grandParentRequiredNullable;
            return this;
        }

        /**
         * Setter for grandParentRequired.
         * @param  grandParentRequired  String value for grandParentRequired.
         * @return Builder
         */
        public Builder grandParentRequired(String grandParentRequired) {
            this.grandParentRequired = grandParentRequired;
            return this;
        }

        /**
         * Setter for grandParentOptional.
         * @param  grandParentOptional  String value for grandParentOptional.
         * @return Builder
         */
        public Builder grandParentOptional(String grandParentOptional) {
            this.grandParentOptional = grandParentOptional;
            return this;
        }

        /**
         * Builds a new {@link GrandParentClass} object using the set fields.
         * @return {@link GrandParentClass}
         */
        public GrandParentClass build() {
            return new GrandParentClass(grandParentRequiredNullable, grandParentRequired,
                    grandParentOptional);
        }
    }
}
