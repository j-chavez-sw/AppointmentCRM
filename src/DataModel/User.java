package DataModel;

import java.time.ZoneId;
import java.util.TimeZone;

public class User {

    private static String userName;
    private static User instance;
    private static ZoneId zone;
    private static String userID;


    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        User.userID = userID;
    }

    private User() {
    }

    public static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ZoneId getZone() {
        return zone;
    }

    public void setZone(ZoneId zone) {
        User.zone = zone;
    }
}
