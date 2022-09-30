/*
 * TesterLib
 *
 * This file was automatically generated for Stamplay by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is a model class for Boss type.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "personType",
        defaultImpl = Boss.class,
        visible = true)
@JsonInclude(Include.ALWAYS)
public class Boss
        extends Employee {
    private LocalDateTime promotedAt;
    private Employee assistant;

    /**
     * Default constructor.
     */
    public Boss() {
        super();
        setPersonType("Boss");
    }

    /**
     * Initialization constructor.
     * @param  address  String value for address.
     * @param  age  long value for age.
     * @param  birthday  LocalDate value for birthday.
     * @param  birthtime  LocalDateTime value for birthtime.
     * @param  name  String value for name.
     * @param  uid  String value for uid.
     * @param  department  String value for department.
     * @param  dependents  List of Person value for dependents.
     * @param  hiredAt  LocalDateTime value for hiredAt.
     * @param  joiningDay  Days value for joiningDay.
     * @param  salary  int value for salary.
     * @param  workingDays  List of Days value for workingDays.
     * @param  boss  Person value for boss.
     * @param  promotedAt  LocalDateTime value for promotedAt.
     * @param  assistant  Employee value for assistant.
     * @param  personType  String value for personType.
     */
    public Boss(
            String address,
            long age,
            LocalDate birthday,
            LocalDateTime birthtime,
            String name,
            String uid,
            String department,
            List<Person> dependents,
            LocalDateTime hiredAt,
            Days joiningDay,
            int salary,
            List<Days> workingDays,
            Person boss,
            LocalDateTime promotedAt,
            Employee assistant,
            String personType) {
        super(address, age, birthday, birthtime, name, uid, department, dependents, hiredAt,
                joiningDay, salary, workingDays, boss, personType);
        this.promotedAt = promotedAt;
        this.assistant = assistant;
    }

    /**
     * Getter for PromotedAt.
     * @return Returns the LocalDateTime
     */
    @JsonGetter("promotedAt")
    @JsonSerialize(using = LocalDateTimeHelper.UnixTimestampSerializer.class)
    public LocalDateTime getPromotedAt() {
        return promotedAt;
    }

    /**
     * Setter for PromotedAt.
     * @param promotedAt Value for LocalDateTime
     */
    @JsonSetter("promotedAt")
    @JsonDeserialize(using = LocalDateTimeHelper.UnixTimestampDeserializer.class)
    public void setPromotedAt(LocalDateTime promotedAt) {
        this.promotedAt = promotedAt;
    }

    /**
     * Getter for Assistant.
     * @return Returns the Employee
     */
    @JsonGetter("assistant")
    public Employee getAssistant() {
        return assistant;
    }

    /**
     * Setter for Assistant.
     * @param assistant Value for Employee
     */
    @JsonSetter("assistant")
    public void setAssistant(Employee assistant) {
        this.assistant = assistant;
    }

    /**
     * Converts this Boss into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Boss [" + "promotedAt=" + promotedAt + ", assistant=" + assistant + ", address="
                + getAddress() + ", age=" + getAge() + ", birthday=" + getBirthday()
                + ", birthtime=" + getBirthtime() + ", name=" + getName() + ", uid=" + getUid()
                + ", department=" + getDepartment() + ", dependents=" + getDependents()
                + ", hiredAt=" + getHiredAt() + ", joiningDay=" + getJoiningDay() + ", salary="
                + getSalary() + ", workingDays=" + getWorkingDays() + ", boss=" + getBoss()
                + ", personType=" + getPersonType() + ", additionalProperties="
                + getAdditionalProperties() + "]";
    }

    /**
     * Builds a new {@link Boss.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link Boss.Builder} object
     */
    public Builder toBossBuilder() {
        Builder builder = new Builder(getAddress(), getAge(), getBirthday(), getBirthtime(),
                getName(), getUid(), getDepartment(), getDependents(), getHiredAt(),
                getJoiningDay(), getSalary(), getWorkingDays(), getBoss(), promotedAt, assistant)
                .personType(getPersonType());
        return builder;
    }

    /**
     * Class to build instances of {@link Boss}.
     */
    public static class Builder {
        private String address;
        private long age;
        private LocalDate birthday;
        private LocalDateTime birthtime;
        private String name;
        private String uid;
        private String department;
        private List<Person> dependents;
        private LocalDateTime hiredAt;
        private Days joiningDay = Days.MONDAY;
        private int salary;
        private List<Days> workingDays;
        private Person boss;
        private LocalDateTime promotedAt;
        private Employee assistant;
        private String personType = "Boss";

        /**
         * Initialization constructor.
         */
        public Builder() {
        }

        /**
         * Initialization constructor.
         * @param  address  String value for address.
         * @param  age  long value for age.
         * @param  birthday  LocalDate value for birthday.
         * @param  birthtime  LocalDateTime value for birthtime.
         * @param  name  String value for name.
         * @param  uid  String value for uid.
         * @param  department  String value for department.
         * @param  dependents  List of Person value for dependents.
         * @param  hiredAt  LocalDateTime value for hiredAt.
         * @param  joiningDay  Days value for joiningDay.
         * @param  salary  int value for salary.
         * @param  workingDays  List of Days value for workingDays.
         * @param  boss  Person value for boss.
         * @param  promotedAt  LocalDateTime value for promotedAt.
         * @param  assistant  Employee value for assistant.
         */
        public Builder(String address, long age, LocalDate birthday, LocalDateTime birthtime,
                String name, String uid, String department, List<Person> dependents,
                LocalDateTime hiredAt, Days joiningDay, int salary, List<Days> workingDays,
                Person boss, LocalDateTime promotedAt, Employee assistant) {
            this.address = address;
            this.age = age;
            this.birthday = birthday;
            this.birthtime = birthtime;
            this.name = name;
            this.uid = uid;
            this.department = department;
            this.dependents = dependents;
            this.hiredAt = hiredAt;
            this.joiningDay = joiningDay;
            this.salary = salary;
            this.workingDays = workingDays;
            this.boss = boss;
            this.promotedAt = promotedAt;
            this.assistant = assistant;
        }

        /**
         * Setter for address.
         * @param  address  String value for address.
         * @return Builder
         */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        /**
         * Setter for age.
         * @param  age  long value for age.
         * @return Builder
         */
        public Builder age(long age) {
            this.age = age;
            return this;
        }

        /**
         * Setter for birthday.
         * @param  birthday  LocalDate value for birthday.
         * @return Builder
         */
        public Builder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        /**
         * Setter for birthtime.
         * @param  birthtime  LocalDateTime value for birthtime.
         * @return Builder
         */
        public Builder birthtime(LocalDateTime birthtime) {
            this.birthtime = birthtime;
            return this;
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
         * Setter for uid.
         * @param  uid  String value for uid.
         * @return Builder
         */
        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        /**
         * Setter for department.
         * @param  department  String value for department.
         * @return Builder
         */
        public Builder department(String department) {
            this.department = department;
            return this;
        }

        /**
         * Setter for dependents.
         * @param  dependents  List of Person value for dependents.
         * @return Builder
         */
        public Builder dependents(List<Person> dependents) {
            this.dependents = dependents;
            return this;
        }

        /**
         * Setter for hiredAt.
         * @param  hiredAt  LocalDateTime value for hiredAt.
         * @return Builder
         */
        public Builder hiredAt(LocalDateTime hiredAt) {
            this.hiredAt = hiredAt;
            return this;
        }

        /**
         * Setter for joiningDay.
         * @param  joiningDay  Days value for joiningDay.
         * @return Builder
         */
        public Builder joiningDay(Days joiningDay) {
            this.joiningDay = joiningDay;
            return this;
        }

        /**
         * Setter for salary.
         * @param  salary  int value for salary.
         * @return Builder
         */
        public Builder salary(int salary) {
            this.salary = salary;
            return this;
        }

        /**
         * Setter for workingDays.
         * @param  workingDays  List of Days value for workingDays.
         * @return Builder
         */
        public Builder workingDays(List<Days> workingDays) {
            this.workingDays = workingDays;
            return this;
        }

        /**
         * Setter for boss.
         * @param  boss  Person value for boss.
         * @return Builder
         */
        public Builder boss(Person boss) {
            this.boss = boss;
            return this;
        }

        /**
         * Setter for promotedAt.
         * @param  promotedAt  LocalDateTime value for promotedAt.
         * @return Builder
         */
        public Builder promotedAt(LocalDateTime promotedAt) {
            this.promotedAt = promotedAt;
            return this;
        }

        /**
         * Setter for assistant.
         * @param  assistant  Employee value for assistant.
         * @return Builder
         */
        public Builder assistant(Employee assistant) {
            this.assistant = assistant;
            return this;
        }

        /**
         * Setter for personType.
         * @param  personType  String value for personType.
         * @return Builder
         */
        public Builder personType(String personType) {
            this.personType = personType;
            return this;
        }

        /**
         * Builds a new {@link Boss} object using the set fields.
         * @return {@link Boss}
         */
        public Boss build() {
            return new Boss(address, age, birthday, birthtime, name, uid, department, dependents,
                    hiredAt, joiningDay, salary, workingDays, boss, promotedAt, assistant,
                    personType);
        }
    }
}
