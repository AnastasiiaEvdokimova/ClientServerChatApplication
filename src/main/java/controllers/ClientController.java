/**
 * Sample Skeleton for 'Client.fxml' Controller Class
 */

package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="messageTextView"
    private ListView<?> messageTextView; // Value injected by FXMLLoader

    @FXML // fx:id="userTextView"
    private ListView<?> userTextView; // Value injected by FXMLLoader

    @FXML // fx:id="messageTextfield"
    private TextField messageTextfield; // Value injected by FXMLLoader

    @FXML
    void sendMessage(ActionEvent event) {

    }
    
    SceneChange sceneChangeController;
    
    void setSceneChangeController(SceneChange sceneChangeController) {
    	this.sceneChangeController = sceneChangeController;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert messageTextView != null : "fx:id=\"messageTextView\" was not injected: check your FXML file 'Client.fxml'.";
        assert userTextView != null : "fx:id=\"userTextView\" was not injected: check your FXML file 'Client.fxml'.";
        assert messageTextfield != null : "fx:id=\"messageTextfield\" was not injected: check your FXML file 'Client.fxml'.";

    }
}
