package ViewControl;

import DataModel.Address;
import DataModel.City;
import DataModel.Customer;
import Database.QueryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerAddController implements Initializable {


    @FXML private TextField nameField;
    @FXML private TextField addField;
    @FXML private TextField add2Field;
    @FXML private ComboBox cityBox;
    @FXML private TextField zipField;
    @FXML private TextField phoneField;

    private String alertTitle = "Form Incomplete";
    private String alertHeaderIncorrect = "There is an error in your submission.";
    private String alertContentIncorrect = "Please check what you have submitted.";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        QueryDB query = new QueryDB();
        cityBox.setItems(query.getAllCities());
    }

    @FXML
    private void cancelButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Dashboard.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    };

    @FXML
    private void saveButton(ActionEvent e) throws IOException{

        try{
            if(!formComplete()){

                Alert loginAlert = new Alert(Alert.AlertType.ERROR);
                loginAlert.initModality(Modality.NONE);
                loginAlert.setTitle(alertTitle);
                loginAlert.setHeaderText(alertHeaderIncorrect);
                loginAlert.setContentText((alertContentIncorrect));
                loginAlert.showAndWait();
            } else{
                QueryDB query = new QueryDB();
                City tempCity = (City) cityBox.getSelectionModel().getSelectedItem();

                Customer newCustomer = new Customer(nameField.getText(),
                                                    new Address(addField.getText(),
                                                                add2Field.getText(),
                                                                tempCity.getCityID(),
                                                                zipField.getText(),
                                                                phoneField.getText()),
                                                                tempCity);
                query.addCustomer(newCustomer);
                cancelButton(e);

            }

        }catch(Exception ex){
            ex.printStackTrace();
        };


    }

    private boolean formComplete(){
        if(nameField.getText().isEmpty() || addField.getText().isEmpty()
                || cityBox.getValue().toString().isEmpty() || zipField.getText().isEmpty() || phoneField.getText().isEmpty()){
            return false;
        }else {
            return true;
        }
    }
}
