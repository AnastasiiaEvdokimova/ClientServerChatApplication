package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class IntroController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="usernameTextfield"
    private TextField usernameTextfield; // Value injected by FXMLLoader
    
    SceneChange sceneChangeController;

    @FXML
    void joinServer(ActionEvent event) {
    	if (usernameTextfield.getText() == "") usernameTextfield.setText("Enter your username");
    	else
    	sceneChangeController.LoadClient(usernameTextfield.getText());
    }

    @FXML
    void startServer(ActionEvent event) {
    	sceneChangeController.LoadServer();
    }
    
    void setSceneChangeController(SceneChange sceneChangeController) {
    	this.sceneChangeController = sceneChangeController;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert usernameTextfield != null : "fx:id=\"usernameTextfield\" was not injected: check your FXML file 'Intro.fxml'.";

    }
    
}