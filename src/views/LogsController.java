/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Kyle Christian
 */
public class LogsController implements Initializable {

    @FXML
    private TextArea loginTextArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String filename = "LoginLog.txt", login;
        File file = new File(filename);
        Scanner inputFile;
        try {
            inputFile = new Scanner(file);
            if (!file.exists()) {
            System.out.println("File doesn't exist.");
            System.exit(0);
        }
        while(inputFile.hasNext())
        {
            login = inputFile.nextLine();
            loginTextArea.appendText(login + "\n");
        }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        
    }    
    
}
