/**
 * Sample Skeleton for 'Server.fxml' Controller Class
 */

package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import server.Server;

public class ServerController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="mainDebugView"
    private ListView<String> mainDebugView; // Value injected by FXMLLoader
    
    Server serverConnection;
    
    SceneChange sceneChangeController;
    
    void setSceneChangeController(SceneChange sceneChangeController) {
    	this.sceneChangeController = sceneChangeController;
    }
    
    public void stopThread()
    {
    	if (serverConnection!= null) serverConnection.stopServer();
    }

    @FXML
    void closeServer(ActionEvent event) {
    	stopThread();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainDebugView != null : "fx:id=\"mainDebugView\" was not injected: check your FXML file 'Server.fxml'.";
        serverConnection = new Server(data -> {
        	synchronized(this) {// let's not  overspam the server with too many messages
			Platform.runLater(()->{
				mainDebugView.getItems().add(data.toString());
			} 
        );
        	}
		});
    }
}
