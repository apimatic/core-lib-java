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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is a model class for Employee type.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "personType",
        defaultImpl = Employee.class,
        visible = true)
@JsonSubTypes({
    @Type(value = Boss.class, name = "Boss")
})
@JsonInclude(Include.ALWAYS)
public class Employee
        extends Person {
    private String department;
    private List<Person> dependents;
    private LocalDateTime hiredAt;
    private Days joiningDay;
    private int salary;
    private List<Days> workingDays;
    private Person boss;

    /**
     * Default constructor.
     */
    public Employee() {
        super();
        setPersonType("Empl");
        joiningDay = Days.MONDAY;
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
     * @param  personType  String value for personType.
     */
    public Employee(
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
            String personType) {
        super(address, age, birthday, birthtime, name, uid, personType);
        this.department = department;
        this.dependents = dependents;
        this.hiredAt = hiredAt;
        this.joiningDay = joiningDay;
        this.salary = salary;
        this.workingDays = workingDays;
        this.boss = boss;
    }

    /**
     * Getter for Department.
     * @return Returns the String
     */
    @JsonGetter("department")
    public String getDepartment() {
        return department;
    }

    /**
     * Setter for Department.
     * @param department Value for String
     */
    @JsonSetter("department")
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Getter for Dependents.
     * @return Returns the List of Person
     */
    @JsonGetter("dependents")
    public List<Person> getDependents() {
        return dependents;
    }

    /**
     * Setter for Dependents.
     * @param dependents Value for List of Person
     */
    @JsonSetter("dependents")
    public void setDependents(List<Person> dependents) {
        this.dependents = dependents;
    }

    /**
     * Getter for HiredAt.
     * @return Returns the LocalDateTime
     */
    @JsonGetter("hiredAt")
    @JsonSerialize(using = LocalDateTimeHelper.Rfc1123DateTimeSerializer.class)
    public LocalDateTime getHiredAt() {
        return hiredAt;
    }

    /**
     * Setter for HiredAt.
     * @param hiredAt Value for LocalDateTime
     */
    @JsonSetter("hiredAt")
    @JsonDeserialize(using = LocalDateTimeHelper.Rfc1123DateTimeDeserializer.class)
    public void setHiredAt(LocalDateTime hiredAt) {
        this.hiredAt = hiredAt;
    }

    /**
     * Getter for JoiningDay.
     * @return Returns the Days
     */
    @JsonGetter("joiningDay")
    public Days getJoiningDay() {
        return joiningDay;
    }

    /**
     * Setter for JoiningDay.
     * @param joiningDay Value for Days
     */
    @JsonSetter("joiningDay")
    public void setJoiningDay(Days joiningDay) {
        this.joiningDay = joiningDay;
    }

    /**
     * Getter for Salary.
     * @return Returns the int
     */
    @JsonGetter("salary")
    public int getSalary() {
        return salary;
    }

    /**
     * Setter for Salary.
     * @param salary Value for int
     */
    @JsonSetter("salary")
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Getter for WorkingDays.
     * @return Returns the List of Days
     */
    @JsonGetter("workingDays")
    public List<Days> getWorkingDays() {
        return workingDays;
    }

    /**
     * Setter for WorkingDays.
     * @param workingDays Value for List of Days
     */
    @JsonSetter("workingDays")
    public void setWorkingDays(List<Days> workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * Getter for Boss.
     * @return Returns the Person
     */
    @JsonGetter("boss")
    public Person getBoss() {
        return boss;
    }

    /**
     * Setter for Boss.
     * @param boss Value for Person
     */
    @JsonSetter("boss")
    public void setBoss(Person boss) {
        this.boss = boss;
    }

    /**
     * Converts this Employee into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Employee [" + "department=" + department + ", dependents=" + dependents
                + ", hiredAt=" + hiredAt + ", joiningDay=" + joiningDay + ", salary=" + salary
                + ", workingDays=" + workingDays + ", boss=" + boss + ", address=" + getAddress()
                + ", age=" + getAge() + ", birthday=" + getBirthday() + ", birthtime="
                + getBirthtime() + ", name=" + getName() + ", uid=" + getUid() + ", personType="
                + getPersonType() + ", additionalProperties=" + getAdditionalProperties() + "]";
    }

    /**
     * Builds a new {@link Employee.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link Employee.Builder} object
     */
    public Builder toEmployeeBuilder() {
        Builder builder = new Builder(getAddress(), getAge(), getBirthday(), getBirthtime(),
                getName(), getUid(), department, dependents, hiredAt, joiningDay, salary,
                workingDays, boss)
                .personType(getPersonType());
        return builder;
    }

    /**
     * Class to build instances of {@link Employee}.
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
        private String personType = "Empl";

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
         */
        public Builder(String address, long age, LocalDate birthday, LocalDateTime birthtime,
                String name, String uid, String department, List<Person> dependents,
                LocalDateTime hiredAt, Days joiningDay, int salary, List<Days> workingDays,
                Person boss) {
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
         * Setter for personType.
         * @param  personType  String value for personType.
         * @return Builder
         */
        public Builder personType(String personType) {
            this.personType = personType;
            return this;
        }

        /**
         * Builds a new {@link Employee} object using the set fields.
         * @return {@link Employee}
         */
        public Employee build() {
            return new Employee(address, age, birthday, birthtime, name, uid, department,
                    dependents, hiredAt, joiningDay, salary, workingDays, boss, personType);
        }
    }
}
