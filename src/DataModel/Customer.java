package DataModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Customer {

    private String customerID;
    private String customerName;
    private Address address;
    private City city;
    private String active;
    private SimpleStringProperty phone;


    public Customer(String customerID, String customerName) {
        this.customerID = customerID;
        this.customerName = customerName;
    }

    public Customer(String customerID, String customerName, Address address, City city, String active) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.active = active;
    }

    public Customer(String customerID, String customerName, Address address, City city) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.active = "1";
        this.phone = new SimpleStringProperty(address.getPhoneNumber());
    }

    public Customer(String customerName, Address address, City city) {
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.active = "1";

    }


    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
}