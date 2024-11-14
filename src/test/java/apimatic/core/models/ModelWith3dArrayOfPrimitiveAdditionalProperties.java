package apimatic.core.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import io.apimatic.core.types.AdditionalProperties;
import io.apimatic.core.utilities.ConversionHelper;

public class ModelWith3dArrayOfPrimitiveAdditionalProperties {

    private final static int ArrayDimension = 3;
    private String company;
    private AdditionalProperties<List<List<List<String>>>> additionalProperties =
            new AdditionalProperties<List<List<List<String>>>>(this.getClass());

    /**
     * Default constructor.
     */
    public ModelWith3dArrayOfPrimitiveAdditionalProperties() {
    }

    /**
     * Initialization constructor.
     *
     * @param company String value for company.
     */
    public ModelWith3dArrayOfPrimitiveAdditionalProperties(final String company) {
        this.company = company;
    }

    /**
     * Getter for Company.
     * @return Returns the String
     */
    @JsonGetter("company")
    public String getCompany() {
        return company;
    }

    /**
     * Setter for Company.
     * @param company Value for String
     */
    @JsonSetter("company")
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Any GETTER. Needed for serialization of additional properties.
     * @return Returns the map of all additional properties.
     */
    @JsonAnyGetter
    private Map<String, List<List<List<String>>>> getAdditionalProperties() {
        return additionalProperties.getAdditionalProperties();
    }

    /**
     * Any SETTER. Needed for de-serialization of additional properties.
     * @param name  The String key.
     * @param value The Object value.
     */
    @JsonAnySetter
    private void setAdditionalProperties(String name, Object value) {
        additionalProperties.setAdditionalProperty(name,
                ConversionHelper.convertToNDimensionalArray(value,
                        x -> ((String) x),
                        ArrayDimension),
                true);
    }

    /**
     * Provides access to value of additional properties using property name as key.
     * @param key The property name, which may or may not be declared.
     * @return property associated with the key.
     */
    public List<List<List<String>>> getAdditionalProperty(String key) {
        return additionalProperties.getAdditionalProperty(key);
    }

    /**
     * Converts this ChildNumberType into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "ModelWith3dArrayOfPrimitiveAdditionalProperties ["
                + "company=" + company + additionalProperties + "]";
    }

    /**
     * Builds a new {@link ModelWith3dArrayOfPrimitiveAdditionalProperties.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link ModelWith3dArrayOfPrimitiveAdditionalProperties.Builder} object
     */
    public Builder toModelWith3dArrayOfPrimitiveAdditionalPropertiesBuilder() {
        Builder builder = new Builder(company);
        return builder;
    }

    /**
     * Class to build instances of {@link ModelWith3dArrayOfPrimitiveAdditionalProperties}.
     */
    public static class Builder {
        private String company;
        private AdditionalProperties<List<List<List<String>>>> additionalProperties =
                new AdditionalProperties<List<List<List<String>>>>();

        /**
         * Initialization constructor.
         */
        public Builder() {
        }

        /**
         * Initialization constructor.
         * @param company String value for company.
         */
        public Builder(final String company) {
            this.company = company;
        }

        /**
         * Setter for company.
         * @param company String value for company.
         * @return Builder
         */
        public Builder company(String company) {
            this.company = company;
            return this;
        }

        /**
         * Setter for additionalProperties.
         * @param name  The String key.
         * @param value The String value.
         * @return Builder
         */
        public Builder additionalProperty(String name, List<List<List<String>>> value) {
            this.additionalProperties.setAdditionalProperty(name, value);
            return this;
        }

        /**
         * Builds a new {@link ModelWith3dArrayOfPrimitiveAdditionalProperties} object
         * using the set fields.
         * @return {@link ChildNumberType}
         */
        public ModelWith3dArrayOfPrimitiveAdditionalProperties build() {
            ModelWith3dArrayOfPrimitiveAdditionalProperties obj = 
                    new ModelWith3dArrayOfPrimitiveAdditionalProperties(company);
            obj.additionalProperties = this.additionalProperties;
            return obj;
        }
    }

}
