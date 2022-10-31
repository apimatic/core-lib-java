package apimatic.core.models;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for ComplexType type.
 */
public class ComplexType {
    private List<Integer> numberListType;
    private Map<String, Integer> numberMapType;
    private InnerComplexType innerComplexType;
    private List<InnerComplexType> innerComplexListType;

    /**
     * Default constructor.
     */
    public ComplexType() {}

    /**
     * Initialization constructor.
     * @param numberListType List of Integer value for numberListType.
     * @param numberMapType Map of String, value for numberMapType.
     * @param innerComplexType InnerComplexType value for innerComplexType.
     * @param innerComplexListType List of InnerComplexType value for innerComplexListType.
     */
    public ComplexType(final List<Integer> numberListType, final Map<String, Integer> numberMapType,
            final InnerComplexType innerComplexType,
            final List<InnerComplexType> innerComplexListType) {
        this.numberListType = numberListType;
        this.numberMapType = numberMapType;
        this.innerComplexType = innerComplexType;
        this.innerComplexListType = innerComplexListType;
    }

    /**
     * Getter for NumberListType.
     * @return Returns the List of Integer
     */
    @JsonGetter("numberListType")
    public List<Integer> getNumberListType() {
        return numberListType;
    }

    /**
     * Setter for NumberListType.
     * @param numberListType Value for List of Integer
     */
    @JsonSetter("numberListType")
    public void setNumberListType(List<Integer> numberListType) {
        this.numberListType = numberListType;
    }

    /**
     * Getter for NumberMapType.
     * @return Returns the Map of String, Integer
     */
    @JsonGetter("numberMapType")
    public Map<String, Integer> getNumberMapType() {
        return numberMapType;
    }

    /**
     * Setter for NumberMapType.
     * @param numberMapType Value for Map of String, Integer
     */
    @JsonSetter("numberMapType")
    public void setNumberMapType(Map<String, Integer> numberMapType) {
        this.numberMapType = numberMapType;
    }

    /**
     * Getter for InnerComplexType.
     * @return Returns the InnerComplexType
     */
    @JsonGetter("innerComplexType")
    public InnerComplexType getInnerComplexType() {
        return innerComplexType;
    }

    /**
     * Setter for InnerComplexType.
     * @param innerComplexType Value for InnerComplexType
     */
    @JsonSetter("innerComplexType")
    public void setInnerComplexType(InnerComplexType innerComplexType) {
        this.innerComplexType = innerComplexType;
    }

    /**
     * Getter for InnerComplexListType.
     * @return Returns the List of InnerComplexType
     */
    @JsonGetter("innerComplexListType")
    public List<InnerComplexType> getInnerComplexListType() {
        return innerComplexListType;
    }

    /**
     * Setter for InnerComplexListType.
     * @param innerComplexListType Value for List of InnerComplexType
     */
    @JsonSetter("innerComplexListType")
    public void setInnerComplexListType(List<InnerComplexType> innerComplexListType) {
        this.innerComplexListType = innerComplexListType;
    }

    /**
     * Converts this ComplexType into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "ComplexType [" + "numberListType=" + numberListType + ", numberMapType="
                + numberMapType + ", innerComplexType=" + innerComplexType
                + ", innerComplexListType=" + innerComplexListType + "]";
    }

    /**
     * Builds a new {@link ComplexType.Builder} object. Creates the instance with the state of the
     * current model.
     * @return a new {@link ComplexType.Builder} object
     */
    public Builder toBuilder() {
        Builder builder =
                new Builder(numberListType, numberMapType, innerComplexType, innerComplexListType);
        return builder;
    }

    /**
     * Class to build instances of {@link ComplexType}.
     */
    public static class Builder {
        private List<Integer> numberListType;
        private Map<String, Integer> numberMapType;
        private InnerComplexType innerComplexType;
        private List<InnerComplexType> innerComplexListType;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param numberListType List of Integer value for numberListType.
         * @param numberMapType Map of String, value for numberMapType.
         * @param innerComplexType InnerComplexType value for innerComplexType.
         * @param innerComplexListType List of InnerComplexType value for innerComplexListType.
         */
        public Builder(final List<Integer> numberListType, final Map<String, Integer> numberMapType,
                final InnerComplexType innerComplexType,
                final List<InnerComplexType> innerComplexListType) {
            this.numberListType = numberListType;
            this.numberMapType = numberMapType;
            this.innerComplexType = innerComplexType;
            this.innerComplexListType = innerComplexListType;
        }

        /**
         * Setter for numberListType.
         * @param numberListType List of Integer value for numberListType.
         * @return Builder
         */
        public Builder numberListType(List<Integer> numberListType) {
            this.numberListType = numberListType;
            return this;
        }

        /**
         * Setter for numberMapType.
         * @param numberMapType Map of String, value for numberMapType.
         * @return Builder
         */
        public Builder numberMapType(Map<String, Integer> numberMapType) {
            this.numberMapType = numberMapType;
            return this;
        }

        /**
         * Setter for innerComplexType.
         * @param innerComplexType InnerComplexType value for innerComplexType.
         * @return Builder
         */
        public Builder innerComplexType(InnerComplexType innerComplexType) {
            this.innerComplexType = innerComplexType;
            return this;
        }

        /**
         * Setter for innerComplexListType.
         * @param innerComplexListType List of InnerComplexType value for innerComplexListType.
         * @return Builder
         */
        public Builder innerComplexListType(List<InnerComplexType> innerComplexListType) {
            this.innerComplexListType = innerComplexListType;
            return this;
        }

        /**
         * Builds a new {@link ComplexType} object using the set fields.
         * @return {@link ComplexType}
         */
        public ComplexType build() {
            return new ComplexType(numberListType, numberMapType, innerComplexType,
                    innerComplexListType);
        }
    }
}
