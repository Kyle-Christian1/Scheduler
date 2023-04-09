/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Kyle Christian
 */
public class DBConnection {
    
    // JDBC URL components
    private static final String protocol = "jdbc:";
    private static final String vendorName = "mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/U05P4X";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    
    // Driver Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    
    private static final String Username = "U05P4X"; // Username
    private static final String Password = "53688566072"; // Password
    
    public static Connection startConnection()
    {
        try
        {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, Username, Password);
            System.out.println("Database connection successful.");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    public static void closeConnection() 
    {
        try
        {
            conn.close();
            System.out.println("Database connection closed.");
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
