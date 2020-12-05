/**
 * Sample Skeleton for 'Server.fxml' Controller Class
 */

package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="mainDebugView"
    private ListView<?> mainDebugView; // Value injected by FXMLLoader
    
    SceneChange sceneChangeController;
    
    void setSceneChangeController(SceneChange sceneChangeController) {
    	this.sceneChangeController = sceneChangeController;
    }

    @FXML
    void closeServer(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainDebugView != null : "fx:id=\"mainDebugView\" was not injected: check your FXML file 'Server.fxml'.";

    }
}
