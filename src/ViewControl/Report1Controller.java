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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Report1Controller implements Initializable {

    @FXML Label yearLBL;
    @FXML ComboBox monthBox;
    @FXML TableView<Appointment> reportField;
    @FXML TableColumn<Appointment,String> typeCOL;
    @FXML TableColumn<Appointment,Integer> amountCOL;


    private ObservableList<String> monthList;
    private ObservableList<Appointment> typeOccurrencesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        yearLBL.setText("Year: " + LocalDate.now().getYear());
        typeOccurrencesList = FXCollections.observableArrayList();

        monthList = FXCollections.observableArrayList();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        
        monthBox.setItems(monthList);

        typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCOL.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

    @FXML
    public void monthSelected(ActionEvent event){
        QueryDB query = new QueryDB();
        int monthIndex = monthBox.getSelectionModel().getSelectedIndex()+1;

        reportField.setItems(query.getAppointmentOccurrences(monthIndex));
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
