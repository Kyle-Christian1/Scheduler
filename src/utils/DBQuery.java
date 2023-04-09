/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;

/**
 *
 * @author Kyle Christian
 */
public class DBQuery {
    
    private static PreparedStatement statement; // Statement ref
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
  
    
    // Create Statement object
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException
    {
        statement = conn.prepareStatement(sqlStatement);
    }
    
    // Return the Statement object
    public static PreparedStatement getPreparedStatement()
    {
        return statement;
    }
    
    public static ObservableList<Customer> getAllCustomers() {
        Connection conn = DBConnection.startConnection();
        allCustomers.clear();
        try {
            String queryStatement = "SELECT customer.customerId, customer.customerName, address.addressId, address.address, address.address2, address.phone, address.postalCode, city.cityId, city.city, country.countryId, country.country"
                                  + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                                  + "INNER JOIN city ON address.cityId = city.cityId "
                                  + "INNER JOIN country ON city.countryId = country.countryId";
            setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                // int id, String name, String address, String address2, String city, String phone, String zip, String country
                Customer customer = new Customer(
                    rs.getInt("customerId"), 
                    rs.getString("customerName"), 
                    rs.getString("address"),
                    rs.getString("address2"),
                    rs.getString("city"),
                    rs.getString("phone"),
                    rs.getString("postalCode"),
                    rs.getString("country"),
                    rs.getInt("addressId"),
                    rs.getInt("cityId"),
                    rs.getInt("countryId"));
                allCustomers.add(customer);
            }
            statement.close();
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    public static ObservableList<Appointment> getMonthlyAppointments (LocalDate localDate) {
        Appointment appointment;
        Date date = Date.valueOf(localDate);
        
        try {
            Connection conn = DBConnection.startConnection();
            monthlyAppointments.clear();
            String queryStatement = "SELECT appointment.appointmentId, customer.customerId, customer.customerName, appointment.title, appointment.description, appointment.contact, appointment.location,\n" +
                                    "appointment.type, appointment.url, appointment.start, appointment.end\n" +
                                    "FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId WHERE MONTH(appointment.start) = MONTH(?)"; 
            setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = getPreparedStatement();
            
            ps.setDate(1, date);
            
            
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            
            while(rs.next()) {
                appointment = new Appointment(
                    rs.getInt("appointmentId"), 
                    rs.getInt("customerId"), 
                    rs.getString("customerName"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("contact"),
                    rs.getString("type"),
                    rs.getString("url"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"));
                monthlyAppointments.add(appointment);
            }
            ps.close();
            return monthlyAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    public static ObservableList<Appointment> getWeeklyAppointments (LocalDate localDate) {
        Appointment appointment;
        Date date = Date.valueOf(localDate);
        
        try {
            Connection conn = DBConnection.startConnection();
            weeklyAppointments.clear();
            String queryStatement = "SELECT appointment.appointmentId, customer.customerId, customer.customerName, appointment.title, appointment.description, appointment.contact, appointment.location,\n" +
                                    "appointment.type, appointment.url, appointment.start, appointment.end\n" +
                                    "FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId WHERE WEEK(appointment.start) = WEEK(?)"; 
            setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = getPreparedStatement();
            
            ps.setDate(1, date);
            
            
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            
            while(rs.next()) {
                appointment = new Appointment(
                    rs.getInt("appointmentId"), 
                    rs.getInt("customerId"), 
                    rs.getString("customerName"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("contact"),
                    rs.getString("type"),
                    rs.getString("url"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"));
                weeklyAppointments.add(appointment);
            }
            ps.close();
            return weeklyAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    public static ObservableList<Appointment> getAllAppointments() {
        Connection conn = DBConnection.startConnection();
        allAppointments.clear();
        try {
            String queryStatement = "SELECT appointment.appointmentId, customer.customerId, customer.customerName, appointment.title, appointment.description, appointment.contact, appointment.location, "
                                  + "appointment.type, appointment.url, appointment.start, appointment.end"
                                  + " FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId ";
                                  
            setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                // int id, int custId, String title, String description, String location, String contact, String type, String url, String start, String end
                Appointment appointment = new Appointment(
                    rs.getInt("appointmentId"), 
                    rs.getInt("customerId"), 
                    rs.getString("customerName"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("contact"),
                    rs.getString("type"),
                    rs.getString("url"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"));
                    
                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
}
