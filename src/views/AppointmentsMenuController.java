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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointment;
import utils.DBConnection;
import utils.DBQuery;
import utils.SceneInterface;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class AppointmentsMenuController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentsTable;
    
    private static Appointment selectedAppointment;
    private static LocalDate selectedDate;
    
    
    SceneInterface si = s->{   // Multiline Lambda Expression for fewer lines of code
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource(s));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + " error");
            }
            };
    @FXML
    private TableColumn<Appointment, Integer> appointmentIDColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerNameColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private DatePicker monthlyDatePicker;
    @FXML
    private DatePicker weeklyDatePicker;
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("End")); 
        appointmentsTable.setItems(DBQuery.getAllAppointments());
    }    

    @FXML
    private void handleBackBtn(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        si.newScene("MainMenu.fxml");
    }

    @FXML
    private void handleAddBtn(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        si.newScene("AddAppointments.fxml");
    }
    
    @FXML
    private void handleModifyBtn(ActionEvent event) {
        if(appointmentsTable.getSelectionModel().getSelectedItem() != null) {
            selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            si.newScene("ModifyAppointments.fxml");
        } else 
            return;
    }

    @FXML
    private void handleDeleteBtn(ActionEvent event) throws SQLException {
        if(appointmentsTable.getSelectionModel().getSelectedItem() != null) {
            selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        }
        
        Connection conn = DBConnection.startConnection(); // connect to database

        String deleteStatement =  "DELETE FROM appointment WHERE appointmentId = ?;";
        
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        ps.setInt(1, selectedAppointment.getId());
        ps.execute();
        
        if (ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) affected!");
        }
        else
            System.out.println("No change!");

        updateTable();
        
        conn.close();
        
    }
    
    private void updateTable()
    {
        appointmentsTable.setItems(DBQuery.getAllAppointments());
    }
    
    public static Appointment getSelectedAppointment()
    {
        return selectedAppointment;
    }


    @FXML
    private void handleMonthlyDatePicker(ActionEvent event) {
        selectedDate = monthlyDatePicker.getValue();
        appointmentsTable.setItems(DBQuery.getMonthlyAppointments(selectedDate));
    }

    @FXML
    private void handleWeeklyDatePicker(ActionEvent event) {
        selectedDate = weeklyDatePicker.getValue();
        appointmentsTable.setItems(DBQuery.getWeeklyAppointments(selectedDate));
    }
    
}
