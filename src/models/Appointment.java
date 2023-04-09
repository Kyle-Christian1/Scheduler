/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 This class is for an appointment and stores appointment properties such as time and location
 */
public class Appointment {
    
    private final SimpleIntegerProperty Id = new SimpleIntegerProperty();
    private final SimpleIntegerProperty CustomerId = new SimpleIntegerProperty();
    private final SimpleStringProperty CustomerName = new SimpleStringProperty();
    private final SimpleStringProperty Title = new SimpleStringProperty();
    private final SimpleStringProperty Description = new SimpleStringProperty();
    private final SimpleStringProperty Location = new SimpleStringProperty();
    private final SimpleStringProperty Contact = new SimpleStringProperty();
    private final SimpleStringProperty Type = new SimpleStringProperty();
    private final SimpleStringProperty URL = new SimpleStringProperty();
    private final SimpleStringProperty Start = new SimpleStringProperty();
    private final SimpleStringProperty End = new SimpleStringProperty();
    
    private LocalDateTime DateTime;

    /**
     *
     */
    public Appointment() {}
    
    /**
     *
     * @param id
     * @param customerId
     * @param customerName
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param url
     * @param start
     * @param end
     * 
     */
    public Appointment(int id, int customerId, String customerName, String title, String description, String location, String contact, String type, String url, Timestamp start, Timestamp end) {
        //DateTime  = start.toLocalDateTime();
        setId(id);
        setCustomerId(customerId);
        setCustomerName(customerName);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setURL(url);
        setStart(start);
        setEnd(end);
    }
    
    private void setDateTime(String dateTime)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"); 
 	LocalDateTime ldt = LocalDateTime.parse(dateTime, dtf);
        this.DateTime = ldt;
    }
    public LocalDateTime getDateTime()
    {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return DateTime;
    }

    public int getId() {
        return Id.get();
    }
    
    public int getCustomerId() {
        return CustomerId.get();
    }
    
    public String getEnd() {
        return End.get();
    }
   
    public String getStart() {
        return Start.get();
    }
    
    public String getTitle() {
        return Title.get();
    }
    
    public String getDescription() {
        return Description.get();
    }
    
    public String getLocation() {
        return Location.get();
    }
    
    public String getContact() {
        return Contact.get();
    }
    
    public String getType() {
        return Type.get();     
    }
    
    public String getUrl() {
        return URL.get();
    }
    
    public String getCustomerName()
    {
        return CustomerName.get();
    }
    
    public void setId(int aptId) {
        this.Id.set(aptId);
    }
    
    public void setCustomerId(int CustomerId) {
        this.CustomerId.set(CustomerId);
    }
    
    public void setEnd(Timestamp TimeEnd) {
        TimeEnd.toString();
        String timeStart = TimeEnd.toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"); 
 	LocalDateTime ldt = LocalDateTime.parse(timeStart, dtf);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid); 
        LocalDateTime utcLocalDateTime = utcDate.toLocalDateTime();
        Timestamp localTimeStamp = Timestamp.valueOf(utcLocalDateTime);
        
        this.End.set(localTimeStamp.toString());   
    }
    
    public void setStart(Timestamp TimeStart) {
        TimeStart.toString();
        String timeStart = TimeStart.toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"); 
 	LocalDateTime ldt = LocalDateTime.parse(timeStart, dtf);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid); 
        LocalDateTime utcLocalDateTime = utcDate.toLocalDateTime();
        Timestamp localTimeStamp = Timestamp.valueOf(utcLocalDateTime);
        
        setDateTime(localTimeStamp.toString());
        
        this.Start.set(localTimeStamp.toString());
    } 
   
    public void setTitle(String Title) {
        this.Title.set(Title);
    }
    
    public void setDescription(String Description) {
        this.Description.set(Description);
    }
    
    public void setLocation(String Location) {
        this.Location.set(Location);
    }
    
    public void setContact(String Contact) {
        this.Contact.set(Contact);
    }

    private void setType(String type) {
        this.Type.set(type);     
    }

    private void setURL(String url) {
        this.URL.set(url);
    }

    private void setCustomerName(String customerName) {
        this.CustomerName.set(customerName);
    }
    
}   
