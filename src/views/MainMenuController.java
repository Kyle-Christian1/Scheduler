/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class MainMenuController implements Initializable {

    @FXML
    private Button exitBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleCustomersBtn(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("CustomerMenu.fxml");
    }

    @FXML
    private void handleAppointmentsBtn(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("AppointmentsMenu.fxml");
    }

    @FXML
    private void handleCalendarBtn(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("Reports.fxml");
    }

    @FXML
    private void handleLogsBtn(ActionEvent event) throws FileNotFoundException, IOException {
      newScene("Logs.fxml");
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        System.exit(0);
    }
    
    private void newScene(String fxml) throws IOException
    {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
