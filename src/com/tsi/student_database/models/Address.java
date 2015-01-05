package com.tsi.student_database.models;

public class Address {

    private int id;
    private String country;
    private String stateOrProvince;
    private String town;
    private String street;
    private String zipCode;
    private int flat;

    public Address(int id, String country, String stateOrProvince, String town,
                   String street, String zipCode, int flat) {
        this.id = id;
        this.country = country;
        this.stateOrProvince = stateOrProvince;
        this.town = town;
        this.street = street;
        this.zipCode = zipCode;
        this.flat = flat;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }
}