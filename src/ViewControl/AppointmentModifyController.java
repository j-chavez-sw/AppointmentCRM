package ViewControl;

import DataModel.Appointment;
import DataModel.Customer;
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
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AppointmentModifyController implements Initializable {

    private ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();

    private String alertTitle = "Form Incomplete";
    private String alertHeaderIncorrect = "There is an error in your submission.";
    private String alertContentIncorrect = "Please check what you have submitted.";


    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, String> appIDCol;
    @FXML
    private TableColumn<Appointment, String> appCusNameCol;
    @FXML
    private TableColumn<Appointment, String> appStartTimeCol;
    @FXML
    private TableColumn<Appointment, String> appEndTimeCol;
    @FXML
    private TableColumn<Appointment, String> appDateCol;
    @FXML
    private TableColumn<Appointment, String> appDescriptionCol;

    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> cusIDCol;
    @FXML
    private TableColumn<Customer, String> cusNameCol;
    @FXML
    private TableColumn<Customer, String> cusCityCol;
    @FXML
    private TableColumn<Customer, String> cusPhoneCol;

    @FXML
    private Label cusLBL;
    @FXML
    private ComboBox conBox;
    @FXML
    private ComboBox locBox;
    @FXML
    private ComboBox timeBox;
    @FXML
    private ComboBox typeBox;
    @FXML
    private DatePicker datePick;

    private static Appointment appointmentToEdit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        QueryDB query = new QueryDB();

        appointmentTableView.setItems(query.getAllAppointments());
        customerTableView.setItems(query.getCustomers());
        conBox.setItems(query.getAllUsers());
        locBox.setItems(query.getAllCities());
        typeBox.setItems(query.getTypes());

        ///////////// Lambda Expression -> uses ChangeListener to allow the user to dynamically populate information
        /////////////                      fields based on clicking the desired row in the TableView as opposed to
        /////////////                      relying on selection + edit button action.

        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateEditForm(appointmentTableView.getSelectionModel().getSelectedItem());
                appointmentToEdit = appointmentTableView.getSelectionModel().getSelectedItem();
            }
        });

        appIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appCusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        cusIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        cusCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        cusPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    public void constructTimeList() {
        appointmentTimes = new QueryDB().getAppointmentTimes(datePick.getValue());
    }

    public void populateTimeBox() {
        appointmentTimes.clear();
        constructTimeList();
        timeBox.setItems(appointmentTimes);
    }

    public void populateTimeBox(LocalTime time) {
        appointmentTimes.clear();
        constructTimeList();
        appointmentTimes.add(time);
        timeBox.setItems(appointmentTimes);
    }

    public void dateSelected(ActionEvent e) throws IOException {
        populateTimeBox();

    }

    private void populateEditForm(Appointment appointment) {

        cusLBL.setText("Customer: " + appointment.getCustomerName());
        conBox.getSelectionModel().select(appointment.getContact());
        locBox.getSelectionModel().select(appointment.getLocation());
        datePick.setValue(appointment.getDate());
        populateTimeBox(appointment.getStart());
        timeBox.getSelectionModel().select(appointment.getStart());
        typeBox.getSelectionModel().select(appointment.getType());

    }

    @FXML
    private void cancelButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Dashboard.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        try {
            if(!formComplete()){
                Alert loginAlert = new Alert(Alert.AlertType.ERROR);
                loginAlert.initModality(Modality.NONE);
                loginAlert.setTitle(alertTitle);
                loginAlert.setHeaderText(alertHeaderIncorrect);
                loginAlert.setContentText((alertContentIncorrect));
                loginAlert.showAndWait();
            }
            else {
                appointmentToEdit.setContact(conBox.getValue().toString());
                appointmentToEdit.setLocation(locBox.getValue().toString());
                appointmentToEdit.setDate(datePick.getValue());
                appointmentToEdit.setStart(LocalTime.parse(timeBox.getValue().toString()));
                appointmentToEdit.setType(typeBox.getValue().toString());

                QueryDB query = new QueryDB();
                query.updateAppointment(appointmentToEdit);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cancelButton(event);
        }

    }

    @FXML
    private void deleteApp(ActionEvent event) throws IOException {
        QueryDB query = new QueryDB();

        try {
            query.deleteAppointment(appointmentToEdit.getAppointmentID());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cancelButton(event);
        }
    }

    private boolean formComplete(){
        try{
            if(customerTableView.getSelectionModel().getSelectedItem() == null ||
                    conBox.getValue().toString().isEmpty() ||
                    datePick.getValue().toString().isEmpty() ||
                    timeBox.getValue().toString().isEmpty() ||
                    typeBox.getValue().toString().isEmpty()){

                return false;
            }else {
                return true;
            }}catch (NullPointerException e){
            return false;
        }

    }
}

