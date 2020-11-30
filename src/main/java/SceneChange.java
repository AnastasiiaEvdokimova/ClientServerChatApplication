//figured we will be able to reuse this

/*import java.io.IOException;

import client.Client;
import game.BaccaratInfo;
import initialization.BaccaratClient;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneChange {
	Client client;
	Stage primaryStage;
	private GameFormController controller;
	
	public SceneChange(Stage primaryStage){
		this.primaryStage = primaryStage;
		
		
		try {
			FXMLLoader mainPane = new FXMLLoader(BaccaratClient.class.getResource("StartForm.fxml"));
			primaryStage.setScene(new Scene(mainPane.load()));
			primaryStage.getScene().getStylesheets().add("bootstrap3.css");
			StartFormController controller = mainPane.getController();
			controller.setSceneChangeController(this);
			primaryStage.setTitle("Baccarat");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setInitialScene() {
		try {
			FXMLLoader mainPane = new FXMLLoader(BaccaratClient.class.getResource("StartForm.fxml"));
			primaryStage.setScene(new Scene(mainPane.load()));
			primaryStage.getScene().getStylesheets().add("bootstrap3.css");
			StartFormController controller = mainPane.getController();
			controller.setSceneChangeController(this);
			primaryStage.setTitle("Baccarat");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changeToGameScene(String ip, int portNumber) {
		
	    try {
	    	
	    	startClient(ip, portNumber);
    		FXMLLoader mainPane = new FXMLLoader(getClass().getResource("GameForm.fxml"));
			primaryStage.setScene(new Scene(mainPane.load()));
			primaryStage.getScene().getStylesheets().add("bootstrap3.css");
			controller = mainPane.getController();
			controller.setSceneChangeController(this);
	    	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	private void startClient(String ip, int portNumber) {
		client = new Client(data->{
			  Platform.runLater(()->{
				  BaccaratInfo gameInfo = (BaccaratInfo) data;
				  controller.displayGame(gameInfo);
				  ;});}, ip, portNumber);
		    client.start();
		   primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				client.stopThread();
				Platform.exit();
				System.exit(0);
			}
		});
	}
	
	public void sendBet(double betAmount, short betWho) {
		BaccaratInfo info = new BaccaratInfo();
		info.setBetData(betWho, betAmount);
		try {
			client.send(info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			setInitialScene();
			primaryStage.setTitle("Could not connect! Please try another address!");
			client.stopThread();
		}catch(NullPointerException e) {
			setInitialScene();
			primaryStage.setTitle("Could not connect! Please try another address!");
			client.stopThread();
		}
	}
}
*/