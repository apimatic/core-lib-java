package apimatic.core.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a model class for AttributesAndElements type.
 */
public class AttributesAndElements {
    private String stringAttr;
    private int numberAttr;
    private String stringElement;
    private int numberElement;

    /**
     * Default constructor.
     */
    public AttributesAndElements() {}

    /**
     * Initialization constructor.
     * @param stringAttr String value for stringAttr.
     * @param numberAttr int value for numberAttr.
     * @param stringElement String value for stringElement.
     * @param numberElement int value for numberElement.
     */
    public AttributesAndElements(final String stringAttr, int numberAttr,
            final String stringElement, int numberElement) {
        this.stringAttr = stringAttr;
        this.numberAttr = numberAttr;
        this.stringElement = stringElement;
        this.numberElement = numberElement;
    }

    /**
     * Getter for StringAttr. string attribute (attribute name "string")
     * @return Returns the String.
     */
    @JsonGetter("string-attr")
    @XmlAttribute(name = "string")
    public String getStringAttr() {
        return stringAttr;
    }

    /**
     * Setter for StringAttr. string attribute (attribute name "string")
     * @param stringAttr Value for String.
     */
    @JsonSetter("string-attr")
    public void setStringAttr(String stringAttr) {
        this.stringAttr = stringAttr;
    }

    /**
     * Getter for NumberAttr. number attribute (attribute name "number")
     * @return Returns the int.
     */
    @JsonGetter("number-attr")
    @XmlAttribute(name = "number")
    public int getNumberAttr() {
        return numberAttr;
    }

    /**
     * Setter for NumberAttr. number attribute (attribute name "number")
     * @param numberAttr Value for int.
     */
    @JsonSetter("number-attr")
    public void setNumberAttr(int numberAttr) {
        this.numberAttr = numberAttr;
    }

    /**
     * Getter for StringElement. string element (element name "string")
     * @return Returns the String.
     */
    @JsonGetter("string-element")
    @XmlElement(name = "string")
    public String getStringElement() {
        return stringElement;
    }

    /**
     * Setter for StringElement. string element (element name "string")
     * @param stringElement Value for String.
     */
    @JsonSetter("string-element")
    public void setStringElement(String stringElement) {
        this.stringElement = stringElement;
    }

    /**
     * Getter for NumberElement. number element (element name "number")
     * @return Returns the int.
     */
    @JsonGetter("number-element")
    @XmlElement(name = "number")
    public int getNumberElement() {
        return numberElement;
    }

    /**
     * Setter for NumberElement. number element (element name "number")
     * @param numberElement Value for int.
     */
    @JsonSetter("number-element")
    public void setNumberElement(int numberElement) {
        this.numberElement = numberElement;
    }

    /**
     * Converts this AttributesAndElements into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "AttributesAndElements [" + "stringAttr=" + stringAttr + ", numberAttr=" + numberAttr
                + ", stringElement=" + stringElement + ", numberElement=" + numberElement + "]";
    }

    /**
     * Builds a new {@link AttributesAndElements.Builder} object. Creates the instance with the
     * state of the current model.
     * @return a new {@link AttributesAndElements.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(stringAttr, numberAttr, stringElement, numberElement);
        return builder;
    }

    /**
     * Class to build instances of {@link AttributesAndElements}.
     */
    public static class Builder {
        private String stringAttr;
        private int numberAttr;
        private String stringElement;
        private int numberElement;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param stringAttr String value for stringAttr.
         * @param numberAttr int value for numberAttr.
         * @param stringElement String value for stringElement.
         * @param numberElement int value for numberElement.
         */
        public Builder(final String stringAttr, int numberAttr, final String stringElement,
                int numberElement) {
            this.stringAttr = stringAttr;
            this.numberAttr = numberAttr;
            this.stringElement = stringElement;
            this.numberElement = numberElement;
        }

        /**
         * Setter for stringAttr.
         * @param stringAttr String value for stringAttr.
         * @return Builder.
         */
        public Builder stringAttr(String stringAttr) {
            this.stringAttr = stringAttr;
            return this;
        }

        /**
         * Setter for numberAttr.
         * @param numberAttr int value for numberAttr.
         * @return Builder.
         */
        public Builder numberAttr(int numberAttr) {
            this.numberAttr = numberAttr;
            return this;
        }

        /**
         * Setter for stringElement.
         * @param stringElement String value for stringElement.
         * @return Builder.
         */
        public Builder stringElement(String stringElement) {
            this.stringElement = stringElement;
            return this;
        }

        /**
         * Setter for numberElement.
         * @param numberElement int value for numberElement.
         * @return Builder.
         */
        public Builder numberElement(int numberElement) {
            this.numberElement = numberElement;
            return this;
        }

        /**
         * Builds a new {@link AttributesAndElements} object using the set fields.
         * @return {@link AttributesAndElements}.
         */
        public AttributesAndElements build() {
            return new AttributesAndElements(stringAttr, numberAttr, stringElement, numberElement);
        }
    }
}
