package apimatic.core.models;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import apimatic.core.models.containers.NonScalarModelOuterMap;
import io.apimatic.core.types.OptionalNullable;

/**
 * This is a model class for NonScalarModel type.
 */
public class NonScalarModel {

    private OptionalNullable<Map<String, NonScalarModelOuterMap>> outerMap;

    /**
     * Default constructor.
     */
    public NonScalarModel() {}

    /**
     * Initialization constructor.
     * @param outerMap Map of String, value for outerMap.
     */
    public NonScalarModel(final Map<String, NonScalarModelOuterMap> outerMap) {
        this.outerMap = OptionalNullable.of(outerMap);
    }

    /**
     * Internal initialization constructor.
     * @param outerMap Map of String, value for outerMap.
     */
    protected NonScalarModel(final OptionalNullable<Map<String, NonScalarModelOuterMap>> outerMap) {
        this.outerMap = outerMap;
    }



    /**
     * Internal Getter for OuterMap.
     * @return Returns the Internal Map of String, NonScalarModelOuterMap
     */
    @JsonGetter("outerMap")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = OptionalNullable.Serializer.class)
    protected OptionalNullable<Map<String, NonScalarModelOuterMap>> internalGetOuterMap() {
        return this.outerMap;
    }

    /**
     * Getter for OuterMap.
     * @return Returns the Map of String, NonScalarModelOuterMap
     */
    public Map<String, NonScalarModelOuterMap> getOuterMap() {
        return OptionalNullable.getFrom(outerMap);
    }

    /**
     * Setter for OuterMap.
     * @param outerMap Value for Map of String, NonScalarModelOuterMap
     */
    @JsonSetter("outerMap")
    public void setOuterMap(Map<String, NonScalarModelOuterMap> outerMap) {
        this.outerMap = OptionalNullable.of(outerMap);
    }

    /**
     * UnSetter for OuterMap.
     */
    public void unsetOuterMap() {
        outerMap = null;
    }

    /**
     * Converts this NonScalarModel into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "NonScalarModel [" + ", outerMap=" + outerMap + "]";
    }

    /**
     * Builds a new {@link NonScalarModel.Builder} object. Creates the instance with the state of
     * the current model.
     * @return a new {@link NonScalarModel.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder;
    }

    /**
     * Class to build instances of {@link NonScalarModel}.
     */
    public static class Builder {


        private OptionalNullable<Map<String, NonScalarModelOuterMap>> outerMap;

        /**
         * Initialization constructor.
         */
        public Builder() {}



        /**
         * Setter for outerMap.
         * @param outerMap Map of String, value for outerMap.
         * @return Builder
         */
        public Builder outerMap(final Map<String, NonScalarModelOuterMap> outerMap) {
            this.outerMap = OptionalNullable.of(outerMap);
            return this;
        }

        /**
         * UnSetter for outerMap.
         * @return Builder
         */
        public Builder unsetOuterMap() {
            outerMap = null;
            return this;
        }

        /**
         * Builds a new {@link NonScalarModel} object using the set fields.
         * @return {@link NonScalarModel}
         */
        public NonScalarModel build() {
            return new NonScalarModel(outerMap);
        }
    }
}
