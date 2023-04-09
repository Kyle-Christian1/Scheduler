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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.Customer;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class ModifyCustomerController implements Initializable {

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
    private ChoiceBox<String> countryChoiceBox;
    @FXML
    private TextField phoneField;
    @FXML
    private CheckBox activeCheckBox;
    
    private final ObservableList<String> countries = FXCollections.observableArrayList("United States", "India", "Russia");
    
    Customer selectedCustomer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedCustomer = CustomerMenuController.getSelectedCustomer();
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        address2Field.setText(selectedCustomer.getAddress2());
        cityField.setText(selectedCustomer.getCity());
        zipcodeField.setText(selectedCustomer.getZip());
        countryChoiceBox.setItems(countries);
        countryChoiceBox.setValue(selectedCustomer.getCountry());
        phoneField.setText(selectedCustomer.getPhone());
        
    }    

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("CustomerMenu.fxml");
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) throws SQLException, IOException {
        
        if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || cityField.getText().isEmpty() || zipcodeField.getText().isEmpty() || countryChoiceBox.getValue().isEmpty() || phoneField.getText().isEmpty()) { 
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to add Customer.");
            alert.setHeaderText("One or more fields are empty");
            alert.setContentText("Make sure there are no empty fields before proceeding.");
            alert.showAndWait();
        } else {
            Connection conn = DBConnection.startConnection(); // connect to database

        String updateStatement =  "UPDATE address SET address = ?, address2 = ?, postalCode = ?, phone = ? WHERE addressId = ?;";
                                 
        DBQuery.setPreparedStatement(conn, updateStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        ps.setString(1, addressField.getText());
        ps.setString(2, address2Field.getText());
        ps.setString(3, zipcodeField.getText());
        ps.setString(4, phoneField.getText());
        ps.setInt(5, selectedCustomer.getAddressId());

        ps.execute();
        
        updateStatement = "UPDATE customer SET customerName = ? WHERE customerId = ?;";
        
        DBQuery.setPreparedStatement(conn, updateStatement);
        ps = DBQuery.getPreparedStatement();
        
        ps.setString(1, nameField.getText());
        ps.setInt(2, selectedCustomer.getId());
        
        ps.execute();
        
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("CustomerMenu.fxml");
        }
        
        
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
