package com.pahanaedu.model;

import java.sql.Timestamp;

//Model class representing a Customer in the system.

public class Customer {
    private String accountNumber;
    private String name;
    private String address;
    private String telephoneNumber;
    private Timestamp registrationDate;

    // Constructors
    public Customer() {
    }

    public Customer(String accountNumber, String name, String address, String telephoneNumber, Timestamp registrationDate) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.registrationDate = registrationDate;
    }

    public Customer(String accountNumber, String name, String address, String telephoneNumber) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}