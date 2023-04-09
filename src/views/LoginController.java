/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.mysql.jdbc.Connection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import utils.DBConnection;
import utils.DBQuery;

/**
 *
 * @author Kyle Christian
 */
public class LoginController implements Initializable {
    
    private static User currentUser = new User();
   
    private Label label;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label locationLabel;
    @FXML
    private Label programTitleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Label locationPromptLabel;
    
    private String errorHeader;
    private String errorTitle;
    private String errorText;
    
    private static LocalTime localTime;
    private static LocalDateTime localDateTime;
    private static ZonedDateTime zdt;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ZoneId zone = ZoneId.systemDefault();
        Locale currentLocale = Locale.getDefault();
        localDateTime = LocalDateTime.now();
        localTime = localDateTime.toLocalTime();
        zdt = localDateTime.atZone(zone);
        String currentLoc = currentLocale.getDisplayCountry();
        locationLabel.setText(currentLoc);
        
        
        // Localization //
        rb = ResourceBundle.getBundle("languages/login", currentLocale);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        errorHeader = rb.getString("errorheader");
        errorTitle = rb.getString("errortitle");
        errorText = rb.getString("errortext");
        programTitleLabel.setText(rb.getString("programTitle"));
        locationPromptLabel.setText(rb.getString("currentLoc"));
        
        
       
    }    
    
    public static User getCurrentUser()
    {
        return currentUser;
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) throws IOException, SQLException {
        
        Clock clock= Clock.systemDefaultZone();
        Timestamp ts = Timestamp.valueOf(LocalDateTime.now(clock));
        
        String fileName = "LoginLog.txt", login;
        FileWriter fWriter = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fWriter);
        
        String username = usernameField.getText(); // local variables
        String password = passwordField.getText();
        Boolean validUser = false;
        
        Connection conn = DBConnection.startConnection(); // connect to database
        
        String selectStatement = "Select * FROM user WHERE userName = ? AND password = ?"; // select statement
        
        // create prepared statement reference 
        DBQuery.setPreparedStatement(conn, selectStatement);    
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        // key value pairs
        ps.setString(1, username);
        ps.setString(2, password);
        
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        while(rs.next())
        {
            String dbUserName = rs.getString("userName");
            String dbPassword = rs.getString("password");
            int dbUserId = rs.getInt("userId");
            
            if (username.equals(dbUserName) && password.equals(dbPassword)) 
            {
                validUser = true;
                currentUser.setUsername(username);
                currentUser.setUserID(dbUserId);
                
                login = currentUser.getUsername();
                outputFile.println("Username: " + login + " | " + "Local Time: " + ts + " | " + "Successful"); // LOG SUCCESSFULL ATTEMPTS
                outputFile.close();
            }
            
        }
        
        if (validUser) {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else
        {
            login = usernameField.getText();
            outputFile.println("Username: " + login + " | " + "Local Time: " + ts + " | " + "Not Successfull");  // LOG UNSUCCESSFULL ATTEMPTS
            outputFile.close();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setHeaderText(errorHeader);
            alert.setContentText(errorText);
            alert.showAndWait();
        }
        
        check15Appointment(zdt);
    }
    
    private static void check15Appointment(ZonedDateTime localTime)
    {
        DBQuery.getAllAppointments().forEach(lamVar -> {
                                                        /* This lambda goes through each appointment checking the current local time vs the appointment start time */

                                                        if (lamVar.getDateTime().toLocalDate().equals(localDateTime.toLocalDate())) {
                                                            System.out.println("Date matched");
                                                            long differenceAB = ChronoUnit.MINUTES.between(localTime.toLocalTime(), lamVar.getDateTime().toLocalTime());
                                                            long differenceBA = ChronoUnit.MINUTES.between(lamVar.getDateTime().toLocalTime(), localTime.toLocalTime());
                                                            System.out.println(differenceAB);
                                                            if(differenceAB <= 15 && differenceAB >= 0)
                                                            {
                                                                System.out.println(localTime.toLocalTime());
                                                                System.out.println(lamVar.getDateTime().toLocalTime());
                                                                System.out.println(differenceAB);
                                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                                alert.setTitle("Appointment Alert");
                                                                alert.setHeaderText("Appointment Soon!");
                                                                alert.setContentText("You have an appointment in 15 minutes.");
                                                                alert.showAndWait();
                                                            }
                                                            if (differenceBA <= 15 && differenceBA >= 0) {
                                                                System.out.println(localTime.toLocalTime());
                                                                System.out.println(lamVar.getDateTime().toLocalTime());
                                                                System.out.println(differenceAB);
                                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                                alert.setTitle("Appointment Alert");
                                                                alert.setHeaderText("Late to appointment!");
                                                                alert.setContentText("You are late to an appointment.");
                                                                alert.showAndWait();
                                                            }
                                                        }
                                                        });
    }   
}
