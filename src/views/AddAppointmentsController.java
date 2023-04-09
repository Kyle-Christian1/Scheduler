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
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
public class AddAppointmentsController implements Initializable {

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
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private Label Date;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<LocalTime> startComboBox;
    
    String time = LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    boolean canCreate = true;
    
    private final ObservableList<LocalTime> times = FXCollections.observableArrayList(LocalTime.of(8, 30), LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0), LocalTime.of(10, 30),
                                                                                      LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0), LocalTime.of(12, 30), LocalTime.of(1, 0),
                                                                                      LocalTime.of(1, 30), LocalTime.of(2, 0), LocalTime.of(2, 30), LocalTime.of(3, 0), LocalTime.of(3, 30));
    @FXML
    private ComboBox<LocalTime> endComboBox;
    
            
//            {LocalTime.of(8, 30), LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0), LocalTime.of(10, 30),
//                         LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0), LocalTime.of(12, 30), LocalTime.of(1, 0),
//                         LocalTime.of(1, 30), LocalTime.of(2, 0), LocalTime.of(2, 30), LocalTime.of(3, 0), LocalTime.of(3, 30)};
    
   // private final ObservableList<LocalTime> times = FXCollections.observableArrayList(00:00:00,00:00:00,00:00:00,00:00:00,00:00:00,00:00:00,00:00:00,00:00:00);

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
        
        
    }    

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        newScene("AppointmentsMenu.fxml");
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) throws SQLException, IOException {
        Clock clock = Clock.systemDefaultZone();    // current local clock
        LocalDate localDate = LocalDate.now(clock);
        ZoneId zone = ZoneId.systemDefault();       // user's zone
        String currentUser = LoginController.getCurrentUser().getUsername(); // current customer reference
        
        
        DateTimeFormatter formatterUTC = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String localStartTime = dateField.getValue().toString() + " " + startComboBox.getValue();
        LocalDateTime localStartDateTime = LocalDateTime.parse(localStartTime, formatterUTC);
        ZonedDateTime zonedStartDateTime = localStartDateTime.atZone(zone);
        ZonedDateTime utcZonedStart = zonedStartDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcLocalStartDateTime = utcZonedStart.toLocalDateTime();
        
        String localEndTime = dateField.getValue().toString() + " " + endComboBox.getValue().toString();
        LocalDateTime localEndDateTime = LocalDateTime.parse(localEndTime, formatterUTC);
        ZonedDateTime zonedEndDateTime = localEndDateTime.atZone(zone);
        ZonedDateTime utcZonedEnd = zonedEndDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcLocalEndDateTime = utcZonedEnd.toLocalDateTime();
        
        
        int userID = LoginController.getCurrentUser().getUserID();
        int customerID = customerTable.getSelectionModel().getSelectedItem().getId();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String contact = contactField.getText();
        String type = typeField.getText();
        String url = urlField.getText();
        String date = dateField.getValue().toString();
        String start = date + " " + startComboBox.getValue();
        String end = date + " " + endComboBox.getValue();
        String createDate = localDate.toString();
        String createdBy = currentUser;
        String lastUpdatedBy = currentUser;
        
        

        if ((((((((title.isEmpty() || description.isEmpty()) || location.isEmpty()) || contact.isEmpty()) || type.isEmpty()) || url.isEmpty()) || date.isEmpty()) || start.isEmpty()) || end.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to add appointment.");
            alert.setHeaderText("One or more fields are empty");
            alert.setContentText("Make sure there are no empty fields before proceeding.");
            alert.showAndWait();
        } 
        else if(dateField.getValue().getDayOfWeek().toString().equals("SUNDAY") || dateField.getValue().getDayOfWeek().toString().equals("SATURDAY"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to add appointment.");
            alert.setHeaderText("Outside Business Hours");
            alert.setContentText("Date must be within business operation hours.");
            alert.showAndWait();
        }
        else {
            Connection conn = DBConnection.startConnection(); // connect to database
        
        String insertStatement =  "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) " +
                                  " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        // KEY VALUE PAIRS
        ps.setInt(1, customerID);
        ps.setInt(2, userID);
        ps.setString(3, title);
        ps.setString(4, description);
        ps.setString(5, location);
        ps.setString(6, contact);
        ps.setString(7, type);
        ps.setString(8, url);
        ps.setTimestamp(9, Timestamp.valueOf(utcLocalStartDateTime));
        ps.setTimestamp(10, Timestamp.valueOf(utcLocalEndDateTime));
        ps.setString(11, createDate);
        ps.setString(12, createdBy);
        ps.setString(13, lastUpdatedBy);
        
        if (canCreate) {
            ps.execute();
        
            newScene("AppointmentsMenu.fxml");
        }
        }
        
        
    }
    

    @FXML
    private void handleStartComboBoxSelect(ActionEvent event) {
        endComboBox.setValue(startComboBox.getValue().plusMinutes(30));
    }

    @FXML
    private void handleEndComboBox(ActionEvent event) {
        startComboBox.setValue(endComboBox.getValue().minusMinutes(30));
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
