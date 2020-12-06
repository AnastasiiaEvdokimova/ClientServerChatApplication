package controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChange{
	Stage primaryStage;
	
	public SceneChange(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void LoadIntro() {
		try {
			FXMLLoader mainPane = new FXMLLoader(getClass().getResource("Intro.fxml"));
			primaryStage.setScene(new Scene(mainPane.load()));
			primaryStage.getScene().getStylesheets().add("bootstrap3.css");
			IntroController controller = mainPane.getController();
			controller.setSceneChangeController(this);
			primaryStage.setTitle("Not Yahoo! Messanger");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void LoadServer() {
		// Execute server load logic here!
		
		try {
			FXMLLoader mainPane = new FXMLLoader(getClass().getResource("Server.fxml"));
			primaryStage.setScene(new Scene(mainPane.load()));
			primaryStage.getScene().getStylesheets().add("bootstrap3.css");
			ServerController controller = mainPane.getController();
			controller.setSceneChangeController(this);
			primaryStage.setTitle("Not Yahoo! Messanger");
			primaryStage.setOnCloseRequest(e->{
				controller.stopThread();
			});
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void LoadClient(String name) {
		// Execute client load logic here!
		
		try {
			FXMLLoader mainPane = new FXMLLoader(getClass().getResource("Client.fxml"));
			primaryStage.setScene(new Scene(mainPane.load()));
			primaryStage.getScene().getStylesheets().add("bootstrap3.css");
			ClientController controller = mainPane.getController();
			controller.setUserName(name);
			controller.setSceneChangeController(this);
			primaryStage.setTitle("Not Yahoo! Messanger");
			primaryStage.setOnCloseRequest(e->{
				controller.stopThread();
			});
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}