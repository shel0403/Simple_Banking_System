package banking.account;

import banking.Utility;

import java.time.LocalDate;

public class Customer {

    private final String firstName;
    private final String secondName;
    private final String lastName;
    private final LocalDate birthDate;

    private static final String DEFAULT_NAME = "";

    public Customer(String firstName, String secondName, String lastName, LocalDate birthDate) {
        Utility.checkIfCorrect(firstName, secondName, lastName, birthDate);

        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Customer(String firstName, String lastName, LocalDate birthDate) {
        this(firstName, DEFAULT_NAME, lastName, birthDate);
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getSecondName() {
        return secondName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
