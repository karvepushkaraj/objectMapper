package model;

public class Address {

    private String address;
    private String street;
    private String city;
    private int pinCode;

    public Address(String address, String street, String city, int pinCode) {
        this.address = address;
        this.street = street;
        this.city = city;
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public int getPinCode() {
        return pinCode;
    }
}
