package ViewControl;

import DataModel.*;
import Database.QueryDB;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AppointmentAddController implements Initializable {

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();
    final ZoneId HQ_TIMEZONE_ID = ZoneId.of("America/Los_Angeles");

    final LocalTime HQ_OPEN_TIME  = LocalTime.of(8,0,0);
    final LocalTime HQ_CLOSE_TIME = LocalTime.of(5,0,0);

    private String alertTitle = "Form Incomplete";
    private String alertHeaderIncorrect = "There is an error in your submission.";
    private String alertContentIncorrect = "Please check what you have submitted.";

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> cusIDCol;
    @FXML private TableColumn<Customer, String> cusNameCol;
    @FXML private TableColumn<Customer, String> cusCityCol;
    @FXML private TableColumn<Customer, String> cusPhoneCol;

    @FXML private ComboBox consultantBox;
    @FXML private ComboBox startTimeBox;
    @FXML private ComboBox typeBox;
    @FXML private DatePicker datePick;

    @FXML private Label customerField;
    @FXML private Label customerLocField;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        QueryDB query = new QueryDB();
        customerList = query.getCustomers();
        customerTableView.setItems(customerList);
        QueryDB.getCustomers();

        consultantBox.setItems(query.getAllUsers());
        typeBox.setItems(query.getTypes());

        cusIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        cusCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        cusPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        ///////////// Lambda Expression -> uses ChangeListener to allow the user to dynamically populate information
        /////////////                      fields based on clicking the desired row in the TableView as opposed to
        /////////////                      relying on selection + edit button action.

        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String selectedCustomer, selectedLoc;
                selectedCustomer = customerTableView.getSelectionModel().getSelectedItem().getCustomerName();
                selectedLoc = customerTableView.getSelectionModel().getSelectedItem().getCity().getCity();
                System.out.println(selectedCustomer + " " + selectedLoc);


                customerField.setText("Customer: " + selectedCustomer);
                customerLocField.setText("Location: " + selectedLoc);

            }
        });
    }


    public void dateSelected(ActionEvent e) throws IOException{
        QueryDB query = new QueryDB();
        populateTimeBox(datePick.getValue());

    }

    public void constructTimeList(){
        appointmentTimes = new QueryDB().getAppointmentTimes(datePick.getValue());

    }

     public void populateTimeBox(LocalDate date){
        appointmentTimes.clear();
        constructTimeList();
        startTimeBox.setItems(appointmentTimes);

     }

     private boolean formComplete(){
        try{
        if(customerTableView.getSelectionModel().getSelectedItem() == null ||
                consultantBox.getValue().toString().isEmpty() ||
                datePick.getValue().toString().isEmpty() ||
                startTimeBox.getValue().toString().isEmpty() ||
                typeBox.getValue().toString().isEmpty()){

            return false;
        }else {
            return true;
        }}catch (NullPointerException e){
            return false;
        }

     }

     @FXML
     private void saveAppointment(ActionEvent e) throws IOException{
        Appointment appointment;
         try{System.out.println("try");

             if(!formComplete()){
                 System.out.println("alert should run");

                 Alert loginAlert = new Alert(Alert.AlertType.ERROR);
                 loginAlert.initModality(Modality.NONE);
                 loginAlert.setTitle(alertTitle);
                 loginAlert.setHeaderText(alertHeaderIncorrect);
                 loginAlert.setContentText((alertContentIncorrect));
                 loginAlert.showAndWait();

             } else{
                 System.out.println("query run");
                 QueryDB query = new QueryDB();
                 LocalDate date = datePick.getValue();
                 LocalTime time = LocalTime.parse(startTimeBox.getValue().toString());


                 appointment = new Appointment(customerTableView.getSelectionModel().getSelectedItem().getCustomerID(),
                                                User.getInstance().getUserName(),
                                                customerTableView.getSelectionModel().getSelectedItem().getCity().getCity(),
                                                consultantBox.getValue().toString(),
                                                typeBox.getValue().toString(),
                                                date,
                                                time);



                 query.addAppointment(appointment);

                 cancelButton(e);

             }

         }catch(Exception ex){
             ex.printStackTrace();
         }

     }

     @FXML
     private void cancelButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Dashboard.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    };
}
