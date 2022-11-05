package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.apimatic.core.types.BaseModel;
import io.apimatic.coreinterfaces.http.Context;

/**
 * A test model for test cases.
 */
public class TestModel extends BaseModel {
    private Context context;
    private String name;
    private String field;

    /**
     * Default constructor.
     */
    public TestModel() {}

    /**
     * Initialization constructor.
     * @param name String value for name.
     * @param field String value for field.
     */
    public TestModel(final String name, final String field) {
        this.name = name;
        this.field = field;
    }

    /**
     * Getter for the context.
     * @return {@link Context}.
     */
    @JsonIgnore
    public Context getContext() {
        return context;
    }

    /**
     * Getter for Name.
     * @return Returns the String.
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Setter for Name.
     * @param name Value for String.
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Field.
     * @return Returns the String.
     */
    @JsonGetter("field")
    public String getField() {
        return field;
    }

    /**
     * Setter for Field.
     * @param field Value for String.
     */
    @JsonSetter("field")
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Converts this TestModel into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "TestModel [" + "name=" + name + ", field=" + field + ", additionalProperties="
                + getAdditionalProperties() + "]";
    }

    /**
     * Builds a new {@link TestModel.Builder} object. Creates the instance with the state of the
     * current model.
     * @return a new {@link TestModel.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder = new Builder(name, field);
        return builder;
    }

    /**
     * Class to build instances of {@link TestModel}.
     */
    public static class Builder {
        private Context context;
        private String name;
        private String field;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param name String value for name.
         * @param field String value for field.
         */
        public Builder(final String name, final String field) {
            this.name = name;
            this.field = field;
        }


        /**
         * Setter for httpContext.
         * @param httpContext {@link Context} value for httpContext.
         * @return Builder.
         */
        public Builder httpContext(Context httpContext) {
            this.context = httpContext;
            return this;
        }

        /**
         * Setter for name.
         * @param name String value for name.
         * @return Builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Setter for field.
         * @param field String value for field.
         * @return Builder.
         */
        public Builder field(String field) {
            this.field = field;
            return this;
        }

        /**
         * Builds a new {@link TestModel} object using the set fields.
         * @return {@link TestModel}.
         */
        public TestModel build() {
            TestModel model = new TestModel(name, field);
            model.context = context;
            return model;
        }
    }
}

