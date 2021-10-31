package ViewControl;

import DataModel.Appointment;
import DataModel.Customer;
import Database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Connection connection;
    private static ObservableList<Appointment> dbAppResultList;
    private ObservableList<Customer> dbCustomerResultList;
    private LocalDate selectedDate;
    private boolean byWeek,byMonth;


    @FXML private TableView<Appointment> dashboardAppointmentTableView;
    @FXML private TableColumn<Appointment, String> appIDCol;
    @FXML private TableColumn<Appointment, String> appCusNameCol;
    @FXML private TableColumn<Appointment, String> appStartTimeCol;
    @FXML private TableColumn<Appointment, String> appEndTimeCol;
    @FXML private TableColumn<Appointment, String> appDateCol;
    @FXML private TableColumn<Appointment, String> appDescriptionCol;

    @FXML private TableView<Customer> dashboardCustomerTableView;
    @FXML private TableColumn<Customer, String> cusIDCol;
    @FXML private TableColumn<Customer, String> cusNameCol;

    @FXML Button weekBTN;
    @FXML Button monthBTN;

    @FXML DatePicker datePicker;



    @Override
    public void initialize(URL url, ResourceBundle rb){
        QueryDB query = new QueryDB();
        try {
            dbAppResultList = query.getAllAppointments();
            dashboardAppointmentTableView.setItems(dbAppResultList);
            getViewAllCustomers();
        } catch(Exception e) {
            e.printStackTrace();
        }
        fifteenMinuteWarning();
        datePicker.setValue(LocalDate.now());
        selectedDate = LocalDate.now();

        cusIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        appIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appCusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("type"));



    }


    private boolean fifteenMinuteWarning(){
        for(Appointment appointment: dbAppResultList){
            if(appointment.getDate().equals(LocalDate.now())){
                long elapsed = Duration.between(LocalTime.now(),appointment.getStart()).toMinutes();
                if(elapsed <= 15 && elapsed>=0){
                    Alert loginAlert = new Alert(Alert.AlertType.ERROR);
                    loginAlert.initModality(Modality.NONE);
                    loginAlert.setTitle("Appointment Alert!");
                    loginAlert.setHeaderText("15 Minute Warning...");
                    loginAlert.setContentText(("There is an appointment scheduled within the next 15 minutes."));
                    loginAlert.showAndWait();
                    dashboardAppointmentTableView.getSelectionModel().select(appointment);
                    return true;
                }
            }
        }
        return false;
    }





    private void getViewAllCustomers() throws SQLException, ClassNotFoundException, Exception{

        dbCustomerResultList = FXCollections.observableArrayList();
        connection = ConnectDB.makeConnection();
        ResultSet results = connection.createStatement().executeQuery("SELECT customerId, customerName\n " + "FROM customer");

        while(results.next()) {
            dbCustomerResultList.add(new Customer(results.getString("customerId"),
                                             results.getString("customerName")
            ));

        }
        dashboardCustomerTableView.setItems(dbCustomerResultList);
        ConnectDB.closeConnection();

    }

    @FXML
    private void dateSelected(ActionEvent e) throws IOException{
        selectedDate = datePicker.getValue();
        System.out.println(selectedDate);
        if(byWeek){showByWeekButton(e);}
        else if(byMonth){showByMonthButton(e);}
    }



    @FXML
    private void addCustomerButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/CustomerAdd.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    };

    @FXML
    private void modifyCustomerButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/CustomerModify.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };

    @FXML
    private void addAppointmentButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/AppointmentAdd.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };

    @FXML
    private void modifyAppointmentButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/AppointmentModify.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };

    @FXML
    private void showByWeekButton(ActionEvent event) throws IOException{
        byWeek=true;
        byMonth=false;
        QueryDB query = new QueryDB();
        dashboardAppointmentTableView.setItems(query.getAllAppointmentsByWeek(selectedDate));
    };

    @FXML
    private void showByMonthButton(ActionEvent event) throws IOException{
        byWeek=false;
        byMonth=true;
        QueryDB query = new QueryDB();
        dashboardAppointmentTableView.setItems(query.getAllAppointmentsByMonth(selectedDate));
    };

    @FXML
    private void report_1_Button(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Report1.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };

    @FXML
    private void report_2_Button(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Report2.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };

    @FXML
    private void report_3_Button(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Report3.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };

    @FXML
    private void report_4_Button(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    };




}
