package apimatic.core.models;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import io.apimatic.core.types.AdditionalProperties;
import io.apimatic.core.utilities.ConversionHelper;
import io.apimatic.core.utilities.CoreHelper;

public class ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties {
    private String company;
    protected AdditionalProperties<Map<String, List<Vehicle>>> additionalProperties = new AdditionalProperties<Map<String, List<Vehicle>>>(
            this.getClass());

    /**
     * Default constructor.
     */
    public ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties() {
    }

    /**
     * Initialization constructor.
     * @param name    String value for name.
     * @param company String value for company.
     * @param type    String value for type.
     */
    public ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties(String company) {
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
    private Map<String, Map<String, List<Vehicle>>> getAdditionalProperties() {
        return additionalProperties.getAdditionalProperties();
    }

    /**
     * Any SETTER. Needed for de-serialization of additional properties.
     * @param name  The String key.
     * @param value The Object value.
     */
    @JsonAnySetter
    private void setAdditionalProperties(String name, Object value) {
        additionalProperties.setAdditionalProperty(name, ConversionHelper.convertToMapOfArray(value, (x) -> {
            try {
                return CoreHelper.deserialize(CoreHelper.serialize(x), Vehicle.class);
            } catch (IOException e) {
                return null;
            }
        }), true);
    }

    /**
     * Provides access to value of additional properties using property name as key.
     * @param key The property name, which may or may not be declared.
     * @return property associated with the key.
     */
    public Map<String, List<Vehicle>> getAdditionalProperty(String key) {
        return additionalProperties.getAdditionalProperty(key);
    }

    /**
     * Converts this ChildNumberType into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "ModelWithPrimitiveAdditionalProperties [" + "company=" + company + additionalProperties + "]";
    }

    /**
     * Builds a new {@link ChildNumberType.Builder} object. Creates the instance
     * with the state of the current model.
     * @return a new {@link ChildNumberType.Builder} object
     */
    public Builder toModelWithPrimitiveAdditionalPropertiesBuilder() {
        Builder builder = new Builder(company);
        return builder;
    }

    /**
     * Class to build instances of {@link ChildNumberType}.
     */
    public static class Builder {
        private String company;
        private AdditionalProperties<Map<String, List<Vehicle>>> additionalProperties = new AdditionalProperties<Map<String, List<Vehicle>>>();

        /**
         * Initialization constructor.
         */
        public Builder() {
        }

        /**
         * Initialization constructor.
         * @param name    String value for name.
         * @param company String value for company.
         */
        public Builder(String company) {
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
        public Builder additionalProperty(String name, Map<String, List<Vehicle>> value) {
            this.additionalProperties.setAdditionalProperty(name, value);
            return this;
        }

        /**
         * Builds a new {@link ChildNumberType} object using the set fields.
         * @return {@link ChildNumberType}
         */
        public ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties build() {
            ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties obj = new ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties(
                    company);
            obj.additionalProperties = this.additionalProperties;
            return obj;
        }
    }

}
