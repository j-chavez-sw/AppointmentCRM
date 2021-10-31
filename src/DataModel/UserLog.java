package DataModel;

public class UserLog {

    private String userName;
    private String timeStamp;

    public UserLog(String userName, String timeStamp) {
        this.userName = userName;
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
