package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.types.BaseModel;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This is a model class for Person type.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "personType", defaultImpl = Person.class, visible = true)
@JsonSubTypes({@Type(value = Employee.class, name = "Empl"),
        @Type(value = Boss.class, name = "Boss")})
@JsonInclude(Include.ALWAYS)
public class Person extends BaseModel {
    private String address;
    private long age;
    private LocalDate birthday;
    private LocalDateTime birthtime;
    private String name;
    private String uid;
    private String personType;

    /**
     * Default constructor.
     */
    public Person() {
        setPersonType("Per");
    }

    /**
     * Initialization constructor.
     * 
     * @param address String value for address.
     * @param age long value for age.
     * @param birthday LocalDate value for birthday.
     * @param birthtime LocalDateTime value for birthtime.
     * @param name String value for name.
     * @param uid String value for uid.
     * @param personType String value for personType.
     */
    public Person(String address, long age, LocalDate birthday, LocalDateTime birthtime,
            String name, String uid, String personType) {
        this.address = address;
        this.age = age;
        this.birthday = birthday;
        this.birthtime = birthtime;
        this.name = name;
        this.uid = uid;
        this.personType = personType;
    }

    /**
     * Getter for Address.
     * 
     * @return Returns the String
     */
    @JsonGetter("address")
    public String getAddress() {
        return address;
    }

    /**
     * Setter for Address.
     * 
     * @param address Value for String
     */
    @JsonSetter("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for Age.
     * 
     * @return Returns the long
     */
    @JsonGetter("age")
    public long getAge() {
        return age;
    }

    /**
     * Setter for Age.
     * 
     * @param age Value for long
     */
    @JsonSetter("age")
    public void setAge(long age) {
        this.age = age;
    }

    /**
     * Getter for Birthday.
     * 
     * @return Returns the LocalDate
     */
    @JsonGetter("birthday")
    @JsonSerialize(using = LocalDateTimeHelper.SimpleDateSerializer.class)
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Setter for Birthday.
     * 
     * @param birthday Value for LocalDate
     */
    @JsonSetter("birthday")
    @JsonDeserialize(using = LocalDateTimeHelper.SimpleDateDeserializer.class)
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Getter for Birthtime.
     * 
     * @return Returns the LocalDateTime
     */
    @JsonGetter("birthtime")
    @JsonSerialize(using = LocalDateTimeHelper.Rfc8601DateTimeSerializer.class)
    public LocalDateTime getBirthtime() {
        return birthtime;
    }

    /**
     * Setter for Birthtime.
     * 
     * @param birthtime Value for LocalDateTime
     */
    @JsonSetter("birthtime")
    @JsonDeserialize(using = LocalDateTimeHelper.Rfc8601DateTimeDeserializer.class)
    public void setBirthtime(LocalDateTime birthtime) {
        this.birthtime = birthtime;
    }

    /**
     * Getter for Name.
     * 
     * @return Returns the String
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Setter for Name.
     * 
     * @param name Value for String
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Uid.
     * 
     * @return Returns the String
     */
    @JsonGetter("uid")
    public String getUid() {
        return uid;
    }

    /**
     * Setter for Uid.
     * 
     * @param uid Value for String
     */
    @JsonSetter("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter for PersonType.
     * 
     * @return Returns the String
     */
    @JsonGetter("personType")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPersonType() {
        return personType;
    }

    /**
     * Setter for PersonType.
     * 
     * @param personType Value for String
     */
    @JsonSetter("personType")
    public void setPersonType(String personType) {
        this.personType = personType;
    }

    /**
     * Converts this Person into string format.
     * 
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Person [" + "address=" + address + ", age=" + age + ", birthday=" + birthday
                + ", birthtime=" + birthtime + ", name=" + name + ", uid=" + uid + ", personType="
                + personType + ", additionalProperties=" + getAdditionalProperties() + "]";
    }

    /**
     * Builds a new {@link Person.Builder} object. Creates the instance with the state of the
     * current model.
     * 
     * @return a new {@link Person.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(address, age, birthday, birthtime, name, uid)
                .personType(getPersonType());
        return builder;
    }

    /**
     * Class to build instances of {@link Person}.
     */
    public static class Builder {
        private String address;
        private long age;
        private LocalDate birthday;
        private LocalDateTime birthtime;
        private String name;
        private String uid;
        private String personType = "Per";

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * 
         * @param address String value for address.
         * @param age long value for age.
         * @param birthday LocalDate value for birthday.
         * @param birthtime LocalDateTime value for birthtime.
         * @param name String value for name.
         * @param uid String value for uid.
         */
        public Builder(String address, long age, LocalDate birthday, LocalDateTime birthtime,
                String name, String uid) {
            this.address = address;
            this.age = age;
            this.birthday = birthday;
            this.birthtime = birthtime;
            this.name = name;
            this.uid = uid;
        }

        /**
         * Setter for address.
         * 
         * @param address String value for address.
         * @return Builder
         */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        /**
         * Setter for age.
         * 
         * @param age long value for age.
         * @return Builder
         */
        public Builder age(long age) {
            this.age = age;
            return this;
        }

        /**
         * Setter for birthday.
         * 
         * @param birthday LocalDate value for birthday.
         * @return Builder
         */
        public Builder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        /**
         * Setter for birthtime.
         * 
         * @param birthtime LocalDateTime value for birthtime.
         * @return Builder
         */
        public Builder birthtime(LocalDateTime birthtime) {
            this.birthtime = birthtime;
            return this;
        }

        /**
         * Setter for name.
         * 
         * @param name String value for name.
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Setter for uid.
         * 
         * @param uid String value for uid.
         * @return Builder
         */
        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        /**
         * Setter for personType.
         * 
         * @param personType String value for personType.
         * @return Builder
         */
        public Builder personType(String personType) {
            this.personType = personType;
            return this;
        }

        /**
         * Builds a new {@link Person} object using the set fields.
         * 
         * @return {@link Person}
         */
        public Person build() {
            return new Person(address, age, birthday, birthtime, name, uid, personType);
        }
    }
}
