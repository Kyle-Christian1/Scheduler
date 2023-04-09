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
import models.Customer;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class CustomerMenuController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    
    private static Customer selectedCustomer;
    
    public static Customer getSelectedCustomer()
    {
        return selectedCustomer;
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        customerTable.setItems(DBQuery.getAllCustomers());
    }    

    @FXML
    private void handleAddBtn(ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        newScene("AddCustomer.fxml");
    }

    @FXML
    private void handleModifyBtn(ActionEvent event) throws IOException {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            newScene("/views/ModifyCustomer.fxml");
        } else 
            return;
        
    }

    @FXML
    private void handleDeleteBtn(ActionEvent event) throws SQLException {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        }
        
        Connection conn = DBConnection.startConnection(); // connect to database

        String deleteStatement =  "DELETE FROM customer WHERE customerId = ?;";
        
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        ps.setInt(1, selectedCustomer.getId());
        ps.execute();
        
        if (ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) affected!");
        }
        else
            System.out.println("No change!");

        updateTable();
        
        
    }

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        newScene("MainMenu.fxml");
    }
    
    private void updateTable()
    {
        customerTable.setItems(DBQuery.getAllCustomers());
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
