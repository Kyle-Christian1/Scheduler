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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class ReportsController implements Initializable {

    @FXML
    private TextArea reportOne;
    @FXML
    private TextArea reportTwo;
    @FXML
    private TextArea reportThree;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            generateReportOne();
            generateReportTwo();
            generateReportThree();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void generateReportOne() throws SQLException
    {
        Connection conn = DBConnection.startConnection(); // connect to database

        String queryStatement =  "SELECT customer.customerName ,count(appointmentId) AS 'Number of Appointments' \n" +
                                "FROM U05P4X.appointment \n" +
                                "INNER JOIN customer ON appointment.customerId = customer.customerId\n" +
                                "GROUP BY customer.customerName;";
                                 
        DBQuery.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
   
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        while(rs.next())
        {
            String customerName = rs.getString("customerName");
            int numberOfAppointments = rs.getInt("Number of Appointments");
            reportOne.appendText("Customer: " + customerName + " | " + "Number of Appointments " + numberOfAppointments + "\n");
        }
    }
    
    private void generateReportTwo() throws SQLException
    {
        Connection conn = DBConnection.startConnection(); // connect to database

        String queryStatement =  "SELECT appointment.contact AS 'consultant', appointment.title, appointment.description, appointment.start \n" +
                                "FROM U05P4X.appointment ORDER BY appointment.contact, appointment.start;" ;
                                 
        DBQuery.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
   
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        while(rs.next())
        {
            String contactName = rs.getString("consultant");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String start = rs.getString("start");
       
            reportTwo.appendText("Consultant: " + contactName + " | " + "Appointment " + title + " | " + "Description: " + description + " | " + "Appointment Time: " + start + "\n");
            
        }
    }
    
    private void generateReportThree() throws SQLException
    {
        Connection conn = DBConnection.startConnection(); // connect to database

        String queryStatement =  "SELECT DISTINCT month(start) AS 'Month', count(type) AS 'Types of Appointment' FROM U05P4X.appointment GROUP BY Month ORDER BY 'Month';" ;
                                 
        DBQuery.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
   
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        while(rs.next())
        {
            String month = rs.getString("Month");
            String typeOfAppointment = rs.getString("Types of Appointment");
            
            reportThree.appendText("Month: " + month + " | " + "Different Appointment Types: " + typeOfAppointment + "\n");
            
        }
    }
    
}
