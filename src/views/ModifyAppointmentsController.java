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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class ModifyAppointmentsController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField urlField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<LocalTime> startComboBox;
    @FXML
    private ComboBox<LocalTime> endComboBox;
    
    private final ObservableList<LocalTime> times = FXCollections.observableArrayList(LocalTime.of(8, 30), LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0), LocalTime.of(10, 30),
                                                                                      LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0), LocalTime.of(12, 30), LocalTime.of(1, 0),
                                                                                      LocalTime.of(1, 30), LocalTime.of(2, 0), LocalTime.of(2, 30), LocalTime.of(3, 0), LocalTime.of(3, 30));
    
    Appointment selectedAppointment;
    Customer selectedCustomer;
    
    @FXML
    private Label Date;
    @FXML
    private Label currentCustomerLabel;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    
    
    
    
   
    
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        customerTable.setItems(DBQuery.getAllCustomers());
        startComboBox.setItems(times);
        endComboBox.setItems(times);
        selectedAppointment = AppointmentsMenuController.getSelectedAppointment();
        titleField.setText(selectedAppointment.getTitle());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        contactField.setText(selectedAppointment.getContact());
        typeField.setText(selectedAppointment.getType());
        urlField.setText(selectedAppointment.getUrl());
        dateField.setValue(selectedAppointment.getDateTime().toLocalDate());
        startComboBox.setValue(selectedAppointment.getDateTime().toLocalTime());
        endComboBox.setValue(selectedAppointment.getDateTime().toLocalTime().plusMinutes(30));
        currentCustomerLabel.setText(selectedAppointment.getCustomerName());
    }    

    @FXML
    private void handleStartComboBoxSelect(ActionEvent event) {
    }

    @FXML
    private void handleEndComboBox(ActionEvent event) {
    }

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException  {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("AppointmentsMenu.fxml");
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) throws IOException, SQLException {
        
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        ZoneId zone = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String localStartTime = dateField.getValue().toString() + " " + startComboBox.getValue();
        LocalDateTime localStartDateTime = LocalDateTime.parse(localStartTime, formatter);
        ZonedDateTime zonedStartDateTime = localStartDateTime.atZone(zone);
        ZonedDateTime utcZonedStart = zonedStartDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcLocalStartDateTime = utcZonedStart.toLocalDateTime();
        
        String localEndTime = dateField.getValue().toString() + " " + endComboBox.getValue().toString();
        LocalDateTime localEndDateTime = LocalDateTime.parse(localEndTime, formatter);
        ZonedDateTime zonedEndDateTime = localEndDateTime.atZone(zone);
        ZonedDateTime utcZonedEnd = zonedEndDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcLocalEndDateTime = utcZonedEnd.toLocalDateTime();
        
        Connection conn = DBConnection.startConnection(); // connect to database

        String updateStatement =  "UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, customerId = ? WHERE appointmentId = ?";
                                 
        DBQuery.setPreparedStatement(conn, updateStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        ps.setString(1, titleField.getText());
        ps.setString(2, descriptionField.getText());
        ps.setString(3, locationField.getText());
        ps.setString(4, contactField.getText());
        ps.setString(5, typeField.getText());
        ps.setString(6, urlField.getText());
        ps.setTimestamp(7, Timestamp.valueOf(utcLocalStartDateTime));
        ps.setTimestamp(8, Timestamp.valueOf(utcLocalEndDateTime));
        ps.setInt(9, customerTable.getSelectionModel().getSelectedItem().getId());
        ps.setInt(10, selectedAppointment.getId());

        ps.execute();
        
        if (ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) changed!");
        }
        else
            System.out.println("No change!");
        
        updateStatement = "Update Appointment SET lastUpdate = NOW()";
        DBQuery.setPreparedStatement(conn, updateStatement);
        ps = DBQuery.getPreparedStatement();
        
        if (ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) changed!");
        }
        else 
            System.out.println("No change!");
        
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("AppointmentsMenu.fxml");
    }
    
    private void newScene(String s) throws IOException
    {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(s));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
}
