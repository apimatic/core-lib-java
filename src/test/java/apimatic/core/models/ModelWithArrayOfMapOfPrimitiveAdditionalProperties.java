package apimatic.core.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import io.apimatic.core.types.AdditionalProperties;
import io.apimatic.core.utilities.ConversionHelper;

public class ModelWithArrayOfMapOfPrimitiveAdditionalProperties {

    private String company;
    private AdditionalProperties<List<Map<String, String>>> additionalProperties =
            new AdditionalProperties<List<Map<String, String>>>(this.getClass());

    /**
     * Default constructor.
     */
    public ModelWithArrayOfMapOfPrimitiveAdditionalProperties() {
    }

    /**
     * Initialization constructor.
     * @param company String value for company.
     */
    public ModelWithArrayOfMapOfPrimitiveAdditionalProperties(final String company) {
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
    private Map<String, List<Map<String, String>>> getAdditionalProperties() {
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
                ConversionHelper.convertToArrayOfMap(value,
                        x -> (String) x),
                true);
    }

    /**
     * Provides access to value of additional properties using property name as key.
     * @param key The property name, which may or may not be declared.
     * @return property associated with the key.
     */
    public List<Map<String, String>> getAdditionalProperty(String key) {
        return additionalProperties.getAdditionalProperty(key);
    }

    /**
     * Converts this ChildNumberType into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "ModelWithArrayOfMapOfPrimitiveAdditionalProperties ["
                + "company=" + company + additionalProperties + "]";
    }

    /**
     * Builds a new {@link ModelWithArrayOfMapOfPrimitiveAdditionalProperties.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link ModelWithArrayOfMapOfPrimitiveAdditionalProperties.Builder} object
     */
    public Builder toModelWithArrayOfMapOfPrimitiveAdditionalPropertiesBuilder() {
        Builder builder = new Builder(company);
        builder.additionalProperties = additionalProperties;
        return builder;
    }

    /**
     * Class to build instances of {@link ModelWithArrayOfMapOfPrimitiveAdditionalProperties}.
     */
    public static class Builder {
        private String company;
        private AdditionalProperties<List<Map<String, String>>> additionalProperties =
                new AdditionalProperties<List<Map<String, String>>>();

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
        public Builder additionalProperty(String name, List<Map<String, String>> value) {
            this.additionalProperties.setAdditionalProperty(name, value);
            return this;
        }

        /**
         * Builds a new {@link ModelWithArrayOfMapOfPrimitiveAdditionalProperties} object
         * using the set fields.
         * @return {@link ModelWithArrayOfMapOfPrimitiveAdditionalProperties}
         */
        public ModelWithArrayOfMapOfPrimitiveAdditionalProperties build() {
            ModelWithArrayOfMapOfPrimitiveAdditionalProperties obj =
                    new ModelWithArrayOfMapOfPrimitiveAdditionalProperties(company);
            obj.additionalProperties = this.additionalProperties;
            return obj;
        }
    }

}
