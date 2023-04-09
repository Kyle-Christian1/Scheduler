/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;
import utils.SceneInterface;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class AddCustomerController implements Initializable {

    @FXML
    private ChoiceBox<String> countryChoiceBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField zipcodeField;
    @FXML
    private TextField phoneField;
    @FXML
    private CheckBox activeCheckBox;
    
    SceneInterface si = s->{   // Multiline Lambda Expression for fewer lines of code
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource(s));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            };

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countryChoiceBox.getItems().add("United States");
        countryChoiceBox.getItems().add("India");
        countryChoiceBox.getItems().add("Russia");
        
    }    

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        si.newScene("CustomerMenu.fxml");
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) throws SQLException, IOException {
        
        // local variables
        LocalDate localDate = null;
        String currentUser = LoginController.getCurrentUser().getUsername();
        int countryId = -1;
        int cityId = -1;
        int addressId = -1;
        boolean isActive = false;
        
        
        if (countryChoiceBox.getValue().equals("United States")) {
            countryId = 1;
        }else if (countryChoiceBox.getValue().equals("India")) {
            countryId = 2;
        }else if (countryChoiceBox.getValue().equals("Russia")) {
            countryId = 3;
        }
        
        if (activeCheckBox.isPressed()) {
            isActive = true;
        }
        
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String address2 = address2Field.getText();
        String city = cityField.getText();
        String zipcode = zipcodeField.getText();
        int country = countryId;
        String createDate = localDate.now().toString();
        String createdBy = currentUser;
        String lastUpdatedBy = currentUser;
        
        if(((((name.isEmpty() || phone.isEmpty()) || address.isEmpty()) || city.isEmpty()) || zipcode.isEmpty()) || country == -1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to add Customer.");
            alert.setHeaderText("One or more fields are empty");
            alert.setContentText("Make sure there are no empty fields before proceeding.");
            alert.showAndWait();
        } else {
            Connection conn = DBConnection.startConnection(); // connect to database
        
            String insertStatement =  "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, ?, ?)";

            DBQuery.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            // KEY VALUE PAIRS
            ps.setString(1, city);
            ps.setInt(2, country);
            ps.setString(3, createDate);
            ps.setString(4, createdBy);
            ps.setString(5, lastUpdatedBy);

            ps.execute();

            if(ps.getUpdateCount() > 0)
            {
                System.out.println(ps.getUpdateCount() + " row(s) affected!");
            }
            else
                System.out.println("No change!");

            String queryStatement = "SELECT cityId FROM city WHERE city = ?";

            DBQuery.setPreparedStatement(conn, queryStatement);
            ps = DBQuery.getPreparedStatement();

            // KEY VALUE PAIRS
            ps.setString(1, city);

            ps.execute();

            ps.getResultSet().next();
            cityId = ps.getResultSet().getInt("cityId");


            insertStatement = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            DBQuery.setPreparedStatement(conn, insertStatement);
            ps = DBQuery.getPreparedStatement();

            // KEY VALUE PAIRS
            ps.setString(1, address);
            ps.setString(2, address2);
            ps.setInt(3, cityId);
            ps.setString(4, zipcode);
            ps.setString(5, phone);
            ps.setString(6, createDate);
            ps.setString(7, currentUser);
            ps.setString(8, currentUser);

            ps.execute();

            queryStatement = "SELECT addressId FROM address WHERE address = ?";

            DBQuery.setPreparedStatement(conn, queryStatement);
            ps = DBQuery.getPreparedStatement();

            ps.setString(1, address);

            ps.execute();

            ps.getResultSet().next();
            addressId = ps.getResultSet().getInt("addressId");

            insertStatement = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?)";

            DBQuery.setPreparedStatement(conn, insertStatement);
            ps = DBQuery.getPreparedStatement();

            // KEY VALUE PAIRS
            ps.setString(1, name);
            ps.setInt(2, addressId);
            ps.setBoolean(3, isActive);
            ps.setString(4, createDate);
            ps.setString(5, currentUser);
            ps.setString(6, currentUser);

            ps.execute();

            ((Node) (event.getSource())).getScene().getWindow().hide();
            si.newScene("CustomerMenu.fxml");
        }
        
       
    }
}
