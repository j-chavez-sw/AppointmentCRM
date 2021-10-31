package Database;

import DataModel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Predicate;


public class QueryDB {

    /////////////Methods that handle logins and other miscellaneous functions

    public boolean userLogin(String user, String password){

        try{
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT * FROM user" +
                    " WHERE userName=? AND password=?");
            prep.setString(1, user);
            prep.setString(2, password);
            ResultSet result = prep.executeQuery();
            if(result.next()){
                User currentUser = User.getInstance();
                currentUser.setUserName(user);
                currentUser.setZone(ZoneId.systemDefault());
                currentUser.setUserID(result.getString("userId"));
                ConnectDB.closeConnection();
                logUser(user,LocalDateTime.now());
                return true;
            } else{
                ConnectDB.closeConnection();
                return false;
            }
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return false;
        }

    }

    public void logUser(String user, LocalDateTime time){

        try(FileWriter loginFileOut = new FileWriter("logins.txt",true)) {
            loginFileOut.write(user+","+time +"\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public ObservableList<UserLog> getLoginReport(){

        ObservableList<UserLog> loginReport = FXCollections.observableArrayList();

        /////////////Try with resources

        try(Scanner loginFileIn = new Scanner(new BufferedReader(new FileReader("logins.txt")))){
            String name, time;
            loginFileIn.useDelimiter(",");
            while(loginFileIn.hasNextLine()){
                name = loginFileIn.next();
                loginFileIn.skip(loginFileIn.delimiter());
                time = loginFileIn.nextLine();
                System.out.println(name+":"+time);
                loginReport.add(new UserLog(name,time));

            }
            return loginReport;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ObservableList<City> getAllCities(){

        ObservableList<City> cities = FXCollections.observableArrayList();
        String tempId, tempCity;

        try{
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT city, `cityId` FROM city");

            ResultSet result = prep.executeQuery();

            while(result.next()){

                tempId = result.getString("cityId");
                tempCity = result.getString("city");

                cities.add(new City(tempId,tempCity));
            }
            ConnectDB.closeConnection();
            return cities;
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return null;
        }
    }

    public static ObservableList<String> getAllUsers(){
        ObservableList<String> users = FXCollections.observableArrayList();
        try{
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT userName FROM user");

            ResultSet result = prep.executeQuery();

            while(result.next()){
                users.add(result.getString("userName"));
            }
            ConnectDB.closeConnection();
            return users;
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return null;
        }

    }

    public static String getCityID(String city) {
        try {
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement(String.format("SELECT cityId FROM city WHERE city=?"));
            prep.setString(1,city);
            ResultSet results = prep.executeQuery();
            results.next();

            String id = results.getString("cityId");
            ConnectDB.closeConnection();
            return id;

        } catch (SQLException | ClassNotFoundException e) {
            e.getMessage();
            return null;
        }
    }

    public String getAddressID(Address address){
        try {
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement(String.format(
                    "SELECT addressId FROM address WHERE address=? AND address2=? AND cityId=? AND postalCode=?"));
            prep.setString(1,address.getAddress_1());
            prep.setString(2,address.getAddress_2());
            prep.setString(3,address.getCityID());
            prep.setString(4,address.getPostalCode());
            ResultSet results = prep.executeQuery();
            results.next();

            String id = results.getString("addressId");
            // ConnectDB.closeConnection();
            return id;

        } catch (SQLException | ClassNotFoundException e) {
            e.getMessage();
            return null;
        }
    }

    public String getZoneID(String city){
        try {
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement(String.format("SELECT zoneId FROM city WHERE city=?"));
            prep.setString(1,city);
            ResultSet results = prep.executeQuery();
            results.next();

            String id = results.getString("zoneId");
            ConnectDB.closeConnection();
            return id;

        } catch (SQLException | ClassNotFoundException e) {
            e.getMessage();
            return null;
        }
    }

    public ObservableList<String> getTypes(){
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Sports Event");
        types.add("Wedding");
        types.add("Engagement");
        types.add("Magazine");
        types.add("Portrait");

        return types;
    }

    /////////////Methods that handle Time Zone Conversion

    public LocalDateTime convertGmtToNy(LocalTime time, LocalDate date){
        ZonedDateTime gmtZDT = ZonedDateTime.of(date, time, ZoneId.of("GMT"));
        ZonedDateTime nyZDT = gmtZDT.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime local = LocalDateTime.of(nyZDT.getYear(),nyZDT.getMonth(),nyZDT.getDayOfMonth(),nyZDT.getHour(),nyZDT.getMinute(),nyZDT.getSecond());
        return local;

    }

    public LocalDateTime convertGmtToLocal(LocalTime time, LocalDate date){
        ZonedDateTime gmtZDT = ZonedDateTime.of(date, time, ZoneId.of("GMT"));
        ZonedDateTime localZDT = gmtZDT.withZoneSameInstant(TimeZone.getDefault().toZoneId());
        LocalDateTime local = LocalDateTime.of(localZDT.getYear(),localZDT.getMonth(),localZDT.getDayOfMonth(),localZDT.getHour(),localZDT.getMinute(),localZDT.getSecond());
        return local;

    }

    public LocalDateTime convertNyToLocal(LocalTime time, LocalDate date){
        ZonedDateTime nyZDT = ZonedDateTime.of(date, time, ZoneId.of("America/New_York"));
        ZonedDateTime localZDT = nyZDT.withZoneSameInstant(TimeZone.getDefault().toZoneId());
        LocalDateTime local = LocalDateTime.of(localZDT.getYear(),localZDT.getMonth(),localZDT.getDayOfMonth(),localZDT.getHour(),localZDT.getMinute(),localZDT.getSecond());
        return local;

    }

    public LocalDateTime convertLocalToNy(LocalTime time, LocalDate date){
        ZonedDateTime localZDT = ZonedDateTime.of(date, time,TimeZone.getDefault().toZoneId());
        ZonedDateTime nyZDT = localZDT.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime ny = LocalDateTime.of(nyZDT.getYear(),nyZDT.getMonth(),nyZDT.getDayOfMonth(),nyZDT.getHour(),nyZDT.getMinute(),nyZDT.getSecond());
        return ny;

    }

    public LocalDateTime convertLocalToGMT(LocalTime time, LocalDate date){
        ZonedDateTime localZDT = ZonedDateTime.of(date, time,TimeZone.getDefault().toZoneId());
        ZonedDateTime gmtZDT = localZDT.withZoneSameInstant(ZoneId.of("GMT"));
        LocalDateTime local = LocalDateTime.of(gmtZDT.getYear(),gmtZDT.getMonth(),gmtZDT.getDayOfMonth(),gmtZDT.getHour(),gmtZDT.getMinute(),gmtZDT.getSecond());
        return local;

    }

    /////////////Methods that handle Customers

    public static ObservableList<Customer> getCustomers(){

        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try{
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT customerId, "+
                    "customerName, a.`addressId`, address, address2, i.`cityId`, city, postalCode, phone\n" +
                    "FROM customer c INNER JOIN address a ON c.addressId = a.addressId\n" +
                    "INNER JOIN city i ON a.cityId = i.cityId\n" +
                    "INNER JOIN country o ON i.countryId = o.countryId ORDER BY customerId;");

            ResultSet result = prep.executeQuery();

            while(result.next()){
                City city = new City(result.getString("cityId"),
                        result.getString("city"));
                Address address = new Address(
                        result.getString("addressId"),
                        result.getString("address"),
                        result.getString("address2"),
                        result.getString("cityId"),
                        result.getString("postalCode"),
                        result.getString("phone"));
                customers.add(new Customer(result.getString("customerId"),
                                           result.getString("customerName"), address, city));
            }

            return customers;


        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return null;
        }
    }

    public boolean addCustomer(Customer customer){
        try{
            QueryDB query = new QueryDB();
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement(String.format("INSERT INTO address " +
                    "(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)"));
            prep.setString(1, customer.getAddress().getAddress_1());
            prep.setString(2, customer.getAddress().getAddress_2());
            prep.setString(3, customer.getCity().getCityID());
            prep.setString(4, customer.getAddress().getPostalCode());
            prep.setString(5, customer.getAddress().getPhoneNumber());
            prep.setString(6, LocalDateTime.now().toString());
            prep.setString(7, User.getInstance().getUserName());
            prep.setString(8, LocalDateTime.now().toString());
            prep.setString(9, User.getInstance().getUserName());

            prep.executeUpdate();

            prep = ConnectDB.conn.prepareStatement(String.format("INSERT INTO customer " +
                    "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "VALUES (?,?,?,?,?,?,?)"));
            prep.setString(1, customer.getCustomerName());
            prep.setString(2, query.getAddressID(customer.getAddress()));
            prep.setString(3, "1");
            prep.setString(4, LocalDateTime.now().toString());
            prep.setString(5, User.getInstance().getUserName());
            prep.setString(6, LocalDateTime.now().toString());
            prep.setString(7, User.getInstance().getUserName());

            prep.executeUpdate();

            ConnectDB.closeConnection();
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return false;
        }

        return false;
    }

    public boolean updateCustomer(Customer customer){
        try{
            QueryDB query = new QueryDB();
            ConnectDB.makeConnection();
            System.out.println("connection");

            PreparedStatement prep = ConnectDB.conn.prepareStatement("UPDATE address \n" +
                    "SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=?, lastUpdateBy=? \n" +
                    "WHERE addressId=? ");
            System.out.println("prep made1");
            prep.setString(1, customer.getAddress().getAddress_1());
            prep.setString(2, customer.getAddress().getAddress_2());
            prep.setString(3, customer.getCity().getCityID());
            prep.setString(4, customer.getAddress().getPostalCode());
            prep.setString(5, customer.getAddress().getPhoneNumber());
            prep.setString(6, LocalDateTime.now().toString());
            prep.setString(7, User.getInstance().getUserName());
            prep.setString(8, customer.getAddress().getAddID());

            prep.executeUpdate();

            System.out.println(customer.getCustomerName());
            System.out.println(customer.getCustomerID());

            prep = ConnectDB.conn.prepareStatement("UPDATE customer " +
                    "SET customerName=?, lastUpdate=?, lastUpdateBy=? "
                    + "WHERE customerId=? ");
            prep.setString(1, customer.getCustomerName());
            prep.setString(2, LocalDateTime.now().toString());
            prep.setString(3, User.getInstance().getUserName());
            prep.setString(4, customer.getCustomerID());

            prep.executeUpdate();



            ConnectDB.closeConnection();
            return true;
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return false;
        }

    }

    public boolean deleteCustomer(String customerID, String addressID){

        try{
            ConnectDB.makeConnection();

            PreparedStatement prep = ConnectDB.conn.prepareStatement("DELETE FROM appointment WHERE customerId=?");
            prep.setString(1,customerID);
            prep.executeUpdate();

            prep = ConnectDB.conn.prepareStatement("DELETE FROM customer WHERE customerId=?");
            prep.setString(1,customerID);
            prep.executeUpdate();

            prep = ConnectDB.conn.prepareStatement("DELETE FROM address WHERE addressId=?");
            prep.setString(1,addressID);
            prep.executeUpdate();


            return true;



        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /////////////Methods that handle Appointments - *CONTAINS LAMBDA EXPRESSIONS

    public ObservableList<LocalTime> getAppointmentTimes(LocalDate date){

        LocalTime time;
        ObservableList<LocalTime> unavailableTimes = FXCollections.observableArrayList();
        ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime appointment;
        try {
            ConnectDB.makeConnection();
            PreparedStatement prep = ConnectDB.conn.prepareStatement("select TIME(start) from appointment where DATE(start) = ? ");
            prep.setString(1,date.toString());
            ResultSet results = prep.executeQuery();
            while(results.next()) {
                appointment= convertGmtToNy(LocalTime.parse(results.getString("TIME(start)"),format),date);
                unavailableTimes.add(appointment.toLocalTime());
            }
            ConnectDB.closeConnection();

        } catch (SQLException | ClassNotFoundException e) {
            e.getMessage();
            return null;
        }

        for(int hour=8; hour<=15; hour++){
            time = LocalTime.of(hour, 0,0);
            appointmentTimes.add(time);
        }

        ///////////// Lambda Expression -> uses Predicate to remove appointment times that are taken already.
        /////////////                      More concise than looping through each element.

        Predicate<LocalTime> predicate = localTime -> unavailableTimes.contains(localTime);
        appointmentTimes.removeIf(predicate);

        ///////////// Lambda Expression -> uses Consumer to convert each appointment time to the Local time zone.

        appointmentTimes.forEach(appt -> appointmentTimes.set(appointmentTimes.indexOf(appt), convertNyToLocal(appt,date).toLocalTime()));
        return appointmentTimes;

    }

    public boolean addAppointment(Appointment appointment) throws ParseException {
        try{
            QueryDB query = new QueryDB();
            ConnectDB.makeConnection();
//            System.out.println("connection");
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//            String time = appointment.getStart().format(formatter);
//            String date = (appointment.getDate().toString() + " " + time);
            LocalDateTime dateTime = convertLocalToGMT(appointment.getStart(),appointment.getDate());
            Timestamp startTime = Timestamp.valueOf(dateTime);
            LocalDateTime dateTime2 = convertLocalToGMT(appointment.getStart().plusMinutes(59),appointment.getDate());
            Timestamp endTime = Timestamp.valueOf(dateTime2);



            PreparedStatement prep = ConnectDB.conn.prepareStatement("INSERT INTO appointment " +
                    "(customerId, userId, title, description, url, location, contact, type, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?,?,'not needed','not needed','not needed',?,?,?,?,?,?,?,?,?)");
            System.out.println("Prep");
            System.out.println(
                    appointment.getCustomerID() +
                            User.getInstance().getUserID()
                            +appointment.getLocation()
                            +appointment.getContact()
                            +appointment.getType()
                            +startTime.toString()
                            +LocalDateTime.now().toString());

            prep.setString(1, appointment.getCustomerID());
            prep.setString(2, User.getInstance().getUserID());
            prep.setString(3, appointment.getLocation());
            prep.setString(4, appointment.getContact());
            prep.setString(5, appointment.getType());
            prep.setString(6, startTime.toString());
            prep.setString(7, endTime.toString());
            prep.setString(8, convertLocalToGMT(LocalTime.now(),LocalDate.now()).toString());
            prep.setString(9, User.getInstance().getUserName());
            prep.setString(10, convertLocalToGMT(LocalTime.now(),LocalDate.now()).toString());
            prep.setString(11, User.getInstance().getUserName());

            prep.executeUpdate();
            System.out.println("executed");

            ConnectDB.closeConnection();
            return true;
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return false;
        }


    }

    public boolean updateAppointment(Appointment appointment) throws ParseException {
        try{
            QueryDB query = new QueryDB();
            ConnectDB.makeConnection();
            LocalDateTime dateTime = convertLocalToGMT(appointment.getStart(),appointment.getDate());
            Timestamp startTime = Timestamp.valueOf(dateTime);
            LocalDateTime dateTime2 = convertLocalToGMT(appointment.getStart().plusMinutes(59),appointment.getDate());
            Timestamp endTime = Timestamp.valueOf(dateTime2);



            PreparedStatement prep = ConnectDB.conn.prepareStatement("UPDATE appointment " +
                    "SET customerId=?, userId=?, location=?, contact=?, type=?, start=?, end=?, " +
                    "lastUpdate=?, lastUpdateBy=? " +
                    "WHERE appointmentId=? ");

            System.out.println("prep");
            prep.setString(1, appointment.getCustomerID());
            prep.setString(2, User.getInstance().getUserID());
            prep.setString(3, appointment.getLocation());
            prep.setString(4, appointment.getContact());
            prep.setString(5, appointment.getType());
            prep.setString(6, startTime.toString());
            prep.setString(7, endTime.toString());
            prep.setString(8, convertLocalToGMT(LocalTime.now(),LocalDate.now()).toString());
            prep.setString(9, User.getInstance().getUserName());
            prep.setString(10, appointment.getAppointmentID());

            prep.executeUpdate();
            System.out.println("executed");

            ConnectDB.closeConnection();
            return true;
        }catch(SQLException | ClassNotFoundException e){
            e.getMessage();
            return false;
        }


    }

    public ObservableList<Appointment> getAllAppointments(){

        ObservableList<Appointment> dbAppResultList = FXCollections.observableArrayList();
        try {
            ConnectDB.makeConnection();
            LocalDate date;
            LocalDateTime startTime, endTime;

            ResultSet results = ConnectDB.conn.createStatement().executeQuery("SELECT appointmentId, c.`customerId`, customerName, " +
                    "title, description, location, contact, type, DATE(start) date, TIME(start) as startTime, TIME(end) as endTime \n" +
                    "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId ORDER BY start;");

            while (results.next()) {
                try {
                    date = LocalDate.parse(results.getString("date"));
                    startTime = convertGmtToLocal(LocalTime.parse(results.getString("startTime")),date);
                    endTime = convertGmtToLocal(LocalTime.parse(results.getString("endTime")),date);

                    dbAppResultList.add(new Appointment(results.getString("appointmentId"),
                            results.getString("customerId"),
                            results.getString("customerName"),
                            results.getString("title"),
                            results.getString("description"),
                            results.getString("location"),
                            results.getString("contact"),
                            results.getString("type"),
                            startTime.toLocalDate(),
                            startTime.toLocalTime(),
                            endTime.toLocalTime()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    ;
                }
            }
            ConnectDB.closeConnection();
            return dbAppResultList;
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }

    public ObservableList<Appointment> getAllAppointmentsByMonth(LocalDate beginningDate){

        ObservableList<Appointment> dbAppResultList = FXCollections.observableArrayList();
        try {
            ConnectDB.makeConnection();
            LocalDate date;
            LocalDateTime startTime, endTime;

            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT appointmentId, c.`customerId`, customerName, " +
                    "title, description, location, contact, type, DATE(start) date, TIME(start) as startTime, TIME(end) as endTime \n" +
                    "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId " +
                    "WHERE MONTH(start)=? AND YEAR(start)=? ORDER BY start;");
            prep.setString(1, String.valueOf(beginningDate.getMonthValue()));
            prep.setString(2, String.valueOf(beginningDate.getYear()));

            ResultSet results = prep.executeQuery();


            while (results.next()) {
                try {
                    date = LocalDate.parse(results.getString("date"));
                    startTime = convertGmtToLocal(LocalTime.parse(results.getString("startTime")),date);
                    endTime = convertGmtToLocal(LocalTime.parse(results.getString("endTime")),date);

                    dbAppResultList.add(new Appointment(results.getString("appointmentId"),
                            results.getString("customerId"),
                            results.getString("customerName"),
                            results.getString("title"),
                            results.getString("description"),
                            results.getString("location"),
                            results.getString("contact"),
                            results.getString("type"),
                            startTime.toLocalDate(),
                            startTime.toLocalTime(),
                            endTime.toLocalTime()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    ;
                }
            }
            ConnectDB.closeConnection();
            return dbAppResultList;
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }

    public ObservableList<Appointment> getAllAppointmentsByWeek(LocalDate beginningDate){

        WeekFields weekFields = WeekFields.of(Locale.US);
        int weekOfYear = beginningDate.get(weekFields.weekOfWeekBasedYear());
        String week = Integer.toString(weekOfYear);


        ObservableList<Appointment> dbAppResultList = FXCollections.observableArrayList();
        try {
            ConnectDB.makeConnection();
            LocalDate date;
            LocalDateTime startTime, endTime;

            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT appointmentId, c.`customerId`, customerName, " +
                    "title, description, location, contact, type, DATE(start) date, TIME(start) as startTime, TIME(end) as endTime \n" +
                    "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId " +
                    "WHERE WEEK(DATE(start))+1=? AND YEAR(start)=? ORDER BY start;");
            prep.setString(1, week);
            prep.setString(2, String.valueOf(beginningDate.getYear()));

            ResultSet results = prep.executeQuery();


            while (results.next()) {
                try {
                    date = LocalDate.parse(results.getString("date"));
                    startTime = convertGmtToLocal(LocalTime.parse(results.getString("startTime")),date);
                    endTime = convertGmtToLocal(LocalTime.parse(results.getString("endTime")),date);

                    dbAppResultList.add(new Appointment(results.getString("appointmentId"),
                            results.getString("customerId"),
                            results.getString("customerName"),
                            results.getString("title"),
                            results.getString("description"),
                            results.getString("location"),
                            results.getString("contact"),
                            results.getString("type"),
                            startTime.toLocalDate(),
                            startTime.toLocalTime(),
                            endTime.toLocalTime()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    ;
                }
            }
            ConnectDB.closeConnection();
            return dbAppResultList;
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }

    public ObservableList<Appointment> getAllAppointmentsByContact(String contact){

        ObservableList<Appointment> dbAppResultList = FXCollections.observableArrayList();
        System.out.println(contact);
        try {
            ConnectDB.makeConnection();
            LocalDate date;
            LocalDateTime startTime, endTime;

            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT appointmentId, c.`customerId`, customerName, " +
                    "title, description, location, contact, type, DATE(start) date, TIME(start) as startTime, TIME(end) as endTime \n" +
                    "FROM customer c INNER JOIN appointment a ON c.customerId = a.customerId " +
                    "WHERE contact=? ORDER BY start;");
            prep.setString(1, contact);


            ResultSet results = prep.executeQuery();


            while (results.next()) {
                try {
                    date = LocalDate.parse(results.getString("date"));
                    startTime = convertGmtToLocal(LocalTime.parse(results.getString("startTime")),date);
                    endTime = convertGmtToLocal(LocalTime.parse(results.getString("endTime")),date);

                    dbAppResultList.add(new Appointment(results.getString("appointmentId"),
                            results.getString("customerId"),
                            results.getString("customerName"),
                            results.getString("title"),
                            results.getString("description"),
                            results.getString("location"),
                            results.getString("contact"),
                            results.getString("type"),
                            startTime.toLocalDate(),
                            startTime.toLocalTime(),
                            endTime.toLocalTime()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            ConnectDB.closeConnection();
            return dbAppResultList;
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }

    public void deleteAppointment(String id){
        try{
            ConnectDB.makeConnection();

            PreparedStatement prep = ConnectDB.conn.prepareStatement("DELETE FROM appointment WHERE appointmentId=?");
            prep.setString(1, id);
            prep.executeUpdate();


            ConnectDB.closeConnection();
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Appointment> getAppointmentOccurrences(int monthIndex){
        ObservableList<Appointment> occurrences = FXCollections.observableArrayList();
        try{
            ConnectDB.makeConnection();

            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT COUNT(*) AS amount, type FROM appointment\n" +
                    "WHERE MONTH(start)=? \n" +
                    "GROUP BY type");
            prep.setString(1, String.valueOf(monthIndex));
            ResultSet results = prep.executeQuery();
            while(results.next()) {
                occurrences.add(new Appointment(results.getString("type"), results.getInt("amount")));
            }
            ConnectDB.closeConnection();
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }


        return occurrences;
    }

    public ObservableList<Appointment> getAppointmentContacts(){
        ObservableList<Appointment> contactList = FXCollections.observableArrayList();
        try{
            ConnectDB.makeConnection();

            PreparedStatement prep = ConnectDB.conn.prepareStatement("SELECT DISTINCT contact from appointment\n");
            ResultSet results = prep.executeQuery();
            while(results.next()) {
                contactList.add(new Appointment(results.getString("contact")));
            }
            ConnectDB.closeConnection();
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }


        return contactList;
    }

}
