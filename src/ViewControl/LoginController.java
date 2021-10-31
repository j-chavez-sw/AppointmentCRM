package ViewControl;

import DataModel.Address;
import DataModel.City;
import DataModel.Customer;
import DataModel.User;
import Database.QueryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.TimeZone;


public class LoginController implements Initializable {

    private TimeZone timeZone;
    private ZoneId zone;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button loginButton;

    @FXML
    private Label programNameLBL;
    @FXML
    private Label usernameLBL;
    @FXML
    private Label passwordLBL;

    private String alertTitle;
    private String alertHeaderIncorrect;
    private String alertContentIncorrect;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        timeZone = TimeZone.getDefault();
        resources = PropertyResourceBundle.getBundle("Languages/lang", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("zh")) {

            programNameLBL.setText(resources.getString("scheduler"));
            usernameLBL.setText(resources.getString("username"));
            passwordLBL.setText(resources.getString("password"));
            loginButton.setText(resources.getString("login"));

            alertContentIncorrect = resources.getString("alertContentIncorrect");
            alertHeaderIncorrect = resources.getString("alertHeaderIncorrect");
            alertTitle = resources.getString("alertTitle");

        }
    }

    @FXML
    private void attemptLogin(ActionEvent event) throws IOException {
        String user;
        String pass;
        QueryDB query = new QueryDB();

        query.convertGmtToNy(LocalTime.of(1,0,0), LocalDate.now());
        query.convertGmtToLocal(LocalTime.of(1,0,0), LocalDate.now());

        user = userField.getText();
        pass = passField.getText();




        if(query.userLogin(user,pass)){


            Parent parent = FXMLLoader.load(getClass().getResource("/ViewControl/Dashboard.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

        else {
            Alert loginAlert = new Alert(Alert.AlertType.ERROR);
            loginAlert.initModality(Modality.NONE);
            loginAlert.setTitle(alertTitle);
            loginAlert.setHeaderText(alertHeaderIncorrect);
            loginAlert.setContentText((alertContentIncorrect));

            loginAlert.showAndWait();


        }


    }










}
