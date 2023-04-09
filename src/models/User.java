/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Kyle Christian
 */
public class User {
    
    private static String username;
    private static Integer userID;

    public static String getUsername() {
        return username;
    }
    
    public static int getUserID()
    {
        return userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setUserID(int id)
    {
        this.userID = id;
    }
    
}
