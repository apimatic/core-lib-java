package apimatic.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.utilities.LocalDateTimeHelper;

/**
 * This is a model class for InnerComplexType type.
 */
public class InnerComplexType {
    private String stringType;
    private boolean booleanType;
    private LocalDateTime dateTimeType;
    private LocalDate dateType;
    private UUID uuidType;
    private long longType;
    private double precisionType;
    private Object objectType;
    private List<String> stringListType;

    /**
     * Default constructor.
     */
    public InnerComplexType() {}

    /**
     * Initialization constructor.
     * @param stringType String value for stringType.
     * @param booleanType boolean value for booleanType.
     * @param dateTimeType LocalDateTime value for dateTimeType.
     * @param dateType LocalDate value for dateType.
     * @param uuidType UUID value for uuidType.
     * @param longType long value for longType.
     * @param precisionType double value for precisionType.
     * @param objectType Object value for objectType.
     * @param stringListType List of String value for stringListType.
     */
    public InnerComplexType(final String stringType, boolean booleanType,
            final LocalDateTime dateTimeType, final LocalDate dateType, final UUID uuidType,
            long longType, double precisionType, final Object objectType,
            final List<String> stringListType) {
        this.stringType = stringType;
        this.booleanType = booleanType;
        this.dateTimeType = dateTimeType;
        this.dateType = dateType;
        this.uuidType = uuidType;
        this.longType = longType;
        this.precisionType = precisionType;
        this.objectType = objectType;
        this.stringListType = stringListType;
    }

    /**
     * Getter for StringType.
     * @return Returns the String.
     */
    @JsonGetter("stringType")
    public String getStringType() {
        return stringType;
    }

    /**
     * Setter for StringType.
     * @param stringType Value for String.
     */
    @JsonSetter("stringType")
    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    /**
     * Getter for BooleanType.
     * @return Returns the boolean.
     */
    @JsonGetter("booleanType")
    public boolean getBooleanType() {
        return booleanType;
    }

    /**
     * Setter for BooleanType.
     * @param booleanType Value for boolean.
     */
    @JsonSetter("booleanType")
    public void setBooleanType(boolean booleanType) {
        this.booleanType = booleanType;
    }

    /**
     * Getter for DateTimeType.
     * @return Returns the LocalDateTime.
     */
    @JsonGetter("dateTimeType")
    @JsonSerialize(using = LocalDateTimeHelper.Rfc8601DateTimeSerializer.class)
    public LocalDateTime getDateTimeType() {
        return dateTimeType;
    }

    /**
     * Setter for DateTimeType.
     * @param dateTimeType Value for LocalDateTime.
     */
    @JsonSetter("dateTimeType")
    @JsonDeserialize(using = LocalDateTimeHelper.Rfc8601DateTimeDeserializer.class)
    public void setDateTimeType(LocalDateTime dateTimeType) {
        this.dateTimeType = dateTimeType;
    }

    /**
     * Getter for DateType.
     * @return Returns the LocalDate.
     */
    @JsonGetter("dateType")
    @JsonSerialize(using = LocalDateTimeHelper.SimpleDateSerializer.class)
    public LocalDate getDateType() {
        return dateType;
    }

    /**
     * Setter for DateType.
     * @param dateType Value for LocalDate.
     */
    @JsonSetter("dateType")
    @JsonDeserialize(using = LocalDateTimeHelper.SimpleDateDeserializer.class)
    public void setDateType(LocalDate dateType) {
        this.dateType = dateType;
    }

    /**
     * Getter for UuidType.
     * @return Returns the UUID.
     */
    @JsonGetter("uuidType")
    public UUID getUuidType() {
        return uuidType;
    }

    /**
     * Setter for UuidType.
     * @param uuidType Value for UUID.
     */
    @JsonSetter("uuidType")
    public void setUuidType(UUID uuidType) {
        this.uuidType = uuidType;
    }

    /**
     * Getter for LongType.
     * @return Returns the long.
     */
    @JsonGetter("longType")
    public long getLongType() {
        return longType;
    }

    /**
     * Setter for LongType.
     * @param longType Value for long.
     */
    @JsonSetter("longType")
    public void setLongType(long longType) {
        this.longType = longType;
    }

    /**
     * Getter for PrecisionType.
     * @return Returns the double.
     */
    @JsonGetter("precisionType")
    public double getPrecisionType() {
        return precisionType;
    }

    /**
     * Setter for PrecisionType.
     * @param precisionType Value for double.
     */
    @JsonSetter("precisionType")
    public void setPrecisionType(double precisionType) {
        this.precisionType = precisionType;
    }

    /**
     * Getter for ObjectType.
     * @return Returns the Object.
     */
    @JsonGetter("objectType")
    public Object getObjectType() {
        return objectType;
    }

    /**
     * Setter for ObjectType.
     * @param objectType Value for Object.
     */
    @JsonSetter("objectType")
    public void setObjectType(Object objectType) {
        this.objectType = objectType;
    }

    /**
     * Getter for StringListType.
     * @return Returns the List of String.
     */
    @JsonGetter("stringListType")
    public List<String> getStringListType() {
        return stringListType;
    }

    /**
     * Setter for StringListType.
     * @param stringListType Value for List of String.
     */
    @JsonSetter("stringListType")
    public void setStringListType(List<String> stringListType) {
        this.stringListType = stringListType;
    }

    /**
     * Converts this InnerComplexType into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "InnerComplexType [" + "stringType=" + stringType + ", booleanType=" + booleanType
                + ", dateTimeType=" + dateTimeType + ", dateType=" + dateType + ", uuidType="
                + uuidType + ", longType=" + longType + ", precisionType=" + precisionType
                + ", objectType=" + objectType + ", stringListType=" + stringListType + "]";
    }

    /**
     * Builds a new {@link InnerComplexType.Builder} object. Creates the instance with the state of
     * the current model.
     * @return a new {@link InnerComplexType.Builder} object.
     */
    public Builder toBuilder() {
        Builder builder =
                new Builder(stringType, booleanType, dateTimeType, dateType, uuidType, longType,
                        precisionType, objectType, stringListType);
        return builder;
    }

    /**
     * Class to build instances of {@link InnerComplexType}.
     */
    public static class Builder {
        private String stringType;
        private boolean booleanType;
        private LocalDateTime dateTimeType;
        private LocalDate dateType;
        private UUID uuidType;
        private long longType;
        private double precisionType;
        private Object objectType;
        private List<String> stringListType;

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param stringType String value for stringType.
         * @param booleanType boolean value for booleanType.
         * @param dateTimeType LocalDateTime value for dateTimeType.
         * @param dateType LocalDate value for dateType.
         * @param uuidType UUID value for uuidType.
         * @param longType long value for longType.
         * @param precisionType double value for precisionType.
         * @param objectType Object value for objectType.
         * @param stringListType List of String value for stringListType.
         */
        public Builder(final String stringType, boolean booleanType,
                final LocalDateTime dateTimeType, final LocalDate dateType, final UUID uuidType,
                long longType, double precisionType, final Object objectType,
                final List<String> stringListType) {
            this.stringType = stringType;
            this.booleanType = booleanType;
            this.dateTimeType = dateTimeType;
            this.dateType = dateType;
            this.uuidType = uuidType;
            this.longType = longType;
            this.precisionType = precisionType;
            this.objectType = objectType;
            this.stringListType = stringListType;
        }

        /**
         * Setter for stringType.
         * @param stringType String value for stringType.
         * @return Builder.
         */
        public Builder stringType(String stringType) {
            this.stringType = stringType;
            return this;
        }

        /**
         * Setter for booleanType.
         * @param booleanType boolean value for booleanType.
         * @return Builder.
         */
        public Builder booleanType(boolean booleanType) {
            this.booleanType = booleanType;
            return this;
        }

        /**
         * Setter for dateTimeType.
         * @param dateTimeType LocalDateTime value for dateTimeType.
         * @return Builder.
         */
        public Builder dateTimeType(LocalDateTime dateTimeType) {
            this.dateTimeType = dateTimeType;
            return this;
        }

        /**
         * Setter for dateType.
         * @param dateType LocalDate value for dateType.
         * @return Builder.
         */
        public Builder dateType(LocalDate dateType) {
            this.dateType = dateType;
            return this;
        }

        /**
         * Setter for uuidType.
         * @param uuidType UUID value for uuidType.
         * @return Builder.
         */
        public Builder uuidType(UUID uuidType) {
            this.uuidType = uuidType;
            return this;
        }

        /**
         * Setter for longType.
         * @param longType long value for longType.
         * @return Builder.
         */
        public Builder longType(long longType) {
            this.longType = longType;
            return this;
        }

        /**
         * Setter for precisionType.
         * @param precisionType double value for precisionType.
         * @return Builder.
         */
        public Builder precisionType(double precisionType) {
            this.precisionType = precisionType;
            return this;
        }

        /**
         * Setter for objectType.
         * @param objectType Object value for objectType.
         * @return Builder.
         */
        public Builder objectType(Object objectType) {
            this.objectType = objectType;
            return this;
        }

        /**
         * Setter for stringListType.
         * @param stringListType List of String value for stringListType.
         * @return Builder.
         */
        public Builder stringListType(List<String> stringListType) {
            this.stringListType = stringListType;
            return this;
        }

        /**
         * Builds a new {@link InnerComplexType} object using the set fields.
         * @return {@link InnerComplexType}.
         */
        public InnerComplexType build() {
            return new InnerComplexType(stringType, booleanType, dateTimeType, dateType, uuidType,
                    longType, precisionType, objectType, stringListType);
        }
    }
}
