package model;

import java.util.List;

public class Employee {

    private long employeeId;
    private String firstName;
    private String lastName;
    private int age;
    private List<String> contactNumbers;
    private Address address;

    public Employee(long employeeId, String firstName, String lastName, int age, List<String> contactNumbers, Address address) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.contactNumbers = contactNumbers;
        this.address = address;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public List<String> getContactNumbers() {
        return contactNumbers;
    }

    public Address getAddress() {
        return address;
    }
}
