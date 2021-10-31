package ViewControl;

import DataModel.Appointment;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Report2Controller implements Initializable {

    @FXML Label conLBL;
    @FXML TableView<Appointment> reportTable;
    @FXML TableView<Appointment> conTable;
    @FXML TableColumn<Appointment,String> conNameCOL;
    @FXML TableColumn<Appointment,String> nameCOL;
    @FXML TableColumn<Appointment,String> dateCOL;
    @FXML TableColumn<Appointment,String> startCOL;
    @FXML TableColumn<Appointment,String> typeCOL;


    private ObservableList<Appointment> contactList;
    private ObservableList<Appointment> appointmentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        QueryDB query = new QueryDB();
        conTable.setItems(query.getAppointmentContacts());


        conNameCOL.setCellValueFactory(new PropertyValueFactory<>("contact"));
        nameCOL.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateCOL.setCellValueFactory(new PropertyValueFactory<>("date"));
        startCOL.setCellValueFactory(new PropertyValueFactory<>("start"));
        typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));

        conTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reportTable.setItems(query.getAllAppointmentsByContact(conTable.getSelectionModel().getSelectedItem().getContact()));
            }
        });

    }

    @FXML
    public void onBack(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Dashboard.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
