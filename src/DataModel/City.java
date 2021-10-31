package DataModel;


import Database.QueryDB;

public class City {

    private String cityID;
    private String city;

    public City(String city) {
        this.city = city;

    }

    public City(String cityID, String city) {
        this.cityID = cityID;
        this.city = city;
    }

    @Override
    public String toString() {
        return city;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}