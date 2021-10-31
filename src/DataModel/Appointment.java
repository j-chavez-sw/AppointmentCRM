package DataModel;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private String appointmentID;
    private String customerID;
    private String userID;
    private String customerName;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String createdBy;
    private String lastUpdatedBy;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Timestamp createDate;
    private Timestamp lastUpdate;
    private int amount;

    public Appointment(String contact) {
        this.contact = contact;
    }

    public Appointment(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public Appointment(String appointmentID, String customerName, String description, LocalDate date, LocalTime start, LocalTime end) {
        this.appointmentID = appointmentID;
        this.customerName = customerName;
        this.description = description;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Appointment(String customerID, String userID, String location, String contact, String type, LocalDate date, LocalTime start) {
        this.customerID = customerID;
        this.userID = userID;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.date = date;
        this.start = start;
    }

    public Appointment(String appointmentID, String customerID, String customerName, String title, String description,
                       String location, String contact, String type, LocalDate date, LocalTime start, LocalTime end) {
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
