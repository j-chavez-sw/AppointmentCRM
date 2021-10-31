package ViewControl;

import DataModel.Address;
import DataModel.Appointment;
import DataModel.City;
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
import java.util.ResourceBundle;

public class CustomerModifyController implements Initializable {

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static Customer customerToEdit;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField addField;
    @FXML private TextField add2Field;
    @FXML private ComboBox cityBox;
    @FXML private TextField zipField;
    @FXML private TextField phoneField;

    @FXML private TableView<Customer> dashboardCustomerTableView;
    @FXML private TableColumn<Customer, String> cusIDCol;
    @FXML private TableColumn<Customer, String> cusNameCol;
    @FXML private TableColumn<Customer, String> cusCityCol;
    @FXML private TableColumn<Customer, String> cusPhoneCol;


    private String alertTitle = "Form Incomplete";
    private String alertHeaderIncorrect = "There is an error in your submission.";
    private String alertContentIncorrect = "Please check what you have submitted.";

    private void populateEditForm(Customer customer){

        idField.setText(customer.getCustomerID());
        idField.setDisable(true);
        nameField.setText(customer.getCustomerName());
        addField.setText(customer.getAddress().getAddress_1());
        add2Field.setText(customer.getAddress().getAddress_2());
        cityBox.getSelectionModel().select(customer.getCity());
        zipField.setText(customer.getAddress().getPostalCode());
        phoneField.setText(customer.getAddress().getPhoneNumber());
        customerToEdit = customer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        QueryDB query = new QueryDB();
        customerList = query.getCustomers();
        dashboardCustomerTableView.setItems(customerList);
        QueryDB.getCustomers();

        cusIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        cusCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        cusPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        cityBox.setItems(new QueryDB().getAllCities());

        ///////////// Lambda Expression -> uses ChangeListener to allow the user to dynamically populate information
        /////////////                      fields based on clicking the desired row in the TableView as opposed to
        /////////////                      relying on selection + edit button action.

        dashboardCustomerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateEditForm(dashboardCustomerTableView.getSelectionModel().getSelectedItem());
            }
        });
        
    }

    @FXML
    private void editCustomer(ActionEvent event) throws IOException{

        String id = dashboardCustomerTableView.getSelectionModel().getSelectedItem().getCustomerID();
        String name = dashboardCustomerTableView.getSelectionModel().getSelectedItem().getCustomerName();
        Address address = dashboardCustomerTableView.getSelectionModel().getSelectedItem().getAddress();
        City city = dashboardCustomerTableView.getSelectionModel().getSelectedItem().getCity();

        customerToEdit = new Customer(id, name, address, city);

        if(dashboardCustomerTableView.isDisable()){
            dashboardCustomerTableView.setDisable(false);
        }else{
            dashboardCustomerTableView.setDisable(true);
        }

//        populateEditForm(customerToEdit);

    }

    @FXML
    private void saveCustomer(ActionEvent event) throws IOException{
        try{
            if(customerToEdit.equals(null) || !formComplete()){

                Alert loginAlert = new Alert(Alert.AlertType.ERROR);
                loginAlert.initModality(Modality.NONE);
                loginAlert.setTitle(alertTitle);
                loginAlert.setHeaderText(alertHeaderIncorrect);
                loginAlert.setContentText((alertContentIncorrect));
                loginAlert.showAndWait();
            } else{
                QueryDB query = new QueryDB();

                customerToEdit.setCustomerName(nameField.getText());
                customerToEdit.getAddress().setAddress_1(addField.getText());
                customerToEdit.getAddress().setAddress_2(add2Field.getText());
                customerToEdit.setCity((City) cityBox.getSelectionModel().getSelectedItem());
                customerToEdit.getAddress().setPostalCode(zipField.getText());
                customerToEdit.getAddress().setPhoneNumber(phoneField.getText());

                System.out.println(customerToEdit.getCity().getCityID() + " " + customerToEdit.getCity().getCity());

                query.updateCustomer(customerToEdit);
                cancelButton(event);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }


    }

    @FXML
    private void deleteCustomer(ActionEvent event) throws IOException{
        try{
            if(dashboardCustomerTableView.getSelectionModel().getSelectedItem() == null){

                Alert loginAlert = new Alert(Alert.AlertType.ERROR);
                loginAlert.initModality(Modality.NONE);
                loginAlert.setTitle("ERROR");
                loginAlert.setHeaderText("No Customer Selected");
                loginAlert.setContentText(("Please Select a Customer."));
                loginAlert.showAndWait();
            } else{
                QueryDB query = new QueryDB();

                String customerID, addressID;
                customerID = dashboardCustomerTableView.getSelectionModel().getSelectedItem().getCustomerID();
                addressID = dashboardCustomerTableView.getSelectionModel().getSelectedItem().getAddress().getAddID();

                query.deleteCustomer(customerID,addressID);
                cancelButton(event);
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

    private boolean formComplete(){
        if(nameField.getText().isEmpty() || addField.getText().isEmpty()
                || cityBox.getValue().toString().isEmpty() || zipField.getText().isEmpty() || phoneField.getText().isEmpty()){
            return false;
        }else {
            return true;
        }
    }
}
