package DataModel;

import Database.QueryDB;

import java.sql.Date;
import java.sql.Timestamp;

public class Address {

    private String addressID;
    private String address_1;
    private String address_2;
    private String cityID;
    private String postalCode;
    private String phoneNumber;

    public Address(String addressID, String address_1, String address_2, String cityID, String postalCode, String phoneNumber) {
        this.addressID = addressID;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.cityID = cityID;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    public Address(String address_1, String address_2, String cityID, String postalCode, String phoneNumber) {
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.cityID = cityID;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }



    public String getAddID() {
        return addressID;
    }

    public void setAddID(String addressID) {
        this.addressID = addressID;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}