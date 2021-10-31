package ViewControl;

import DataModel.UserLog;
import Database.QueryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Report3Controller implements Initializable {

    @FXML TableView<UserLog> reportField;
    @FXML TableColumn<UserLog,String> nameCOL;
    @FXML TableColumn<UserLog,String> timeCOL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        QueryDB query = new QueryDB();
        reportField.setItems(query.getLoginReport());

        nameCOL.setCellValueFactory(new PropertyValueFactory<>("userName"));
        timeCOL.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
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
