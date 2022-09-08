package apimatic.core_lib.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class DeleteBody
        extends BaseModel {
    private String name;
    private String field;

    /**
     * Default constructor.
     */
    public DeleteBody() {
    }

    /**
     * Initialization constructor.
     * @param  name  String value for name.
     * @param  field  String value for field.
     */
    public DeleteBody(
            String name,
            String field) {
        this.name = name;
        this.field = field;
    }

    /**
     * Getter for Name.
     * @return Returns the String
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Setter for Name.
     * @param name Value for String
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Field.
     * @return Returns the String
     */
    @JsonGetter("field")
    public String getField() {
        return field;
    }

    /**
     * Setter for Field.
     * @param field Value for String
     */
    @JsonSetter("field")
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Converts this DeleteBody into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "DeleteBody [" + "name=" + name + ", field=" + field + ", additionalProperties="
                + getAdditionalProperties() + "]";
    }

    /**
     * Builds a new {@link DeleteBody.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link DeleteBody.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(name, field);
        return builder;
    }

    /**
     * Class to build instances of {@link DeleteBody}.
     */
    public static class Builder {
        private String name;
        private String field;

        /**
         * Initialization constructor.
         */
        public Builder() {
        }

        /**
         * Initialization constructor.
         * @param  name  String value for name.
         * @param  field  String value for field.
         */
        public Builder(String name, String field) {
            this.name = name;
            this.field = field;
        }

        /**
         * Setter for name.
         * @param  name  String value for name.
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Setter for field.
         * @param  field  String value for field.
         * @return Builder
         */
        public Builder field(String field) {
            this.field = field;
            return this;
        }

        /**
         * Builds a new {@link DeleteBody} object using the set fields.
         * @return {@link DeleteBody}
         */
        public DeleteBody build() {
            return new DeleteBody(name, field);
        }
    }
}

