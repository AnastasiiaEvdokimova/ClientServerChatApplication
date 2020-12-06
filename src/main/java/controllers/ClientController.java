/**
 * Sample Skeleton for 'Client.fxml' Controller Class
 */

package controllers;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

import client.Client;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;
import networking.Message;
import networking.User;

public class ClientController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="messageTextView"
    private ListView<String> messageTextView; // Value injected by FXMLLoader

    @FXML // fx:id="userTextView"
    private ListView<String> userTextView; // Value injected by FXMLLoader

    @FXML // fx:id="messageTextfield"
    private TextField messageTextfield; // Value injected by FXMLLoader
    
    private TreeSet<Integer> recepientList;
    
    private TreeMap<String, Integer> userNameIDMap;
    
    private TreeMap<Integer, String> userIDNameMap;
    
	private Client clientConnection;
	
	
	public void setUserName(String userName)
	{
//only start the client after the username is passed
    	clientConnection = new Client(new Call(), userName);
    	clientConnection.start();
	}

    @FXML
    void sendMessage(ActionEvent event) {

    }
    
    public void stopThread()
    {
    	clientConnection.stopThread();
    }
    

    //adds all users currently in the list to the listView
    private void displayUsers()
    {
    	userTextView.getItems().clear();
    	//display the updated list of users
    	for (String userName: userNameIDMap.keySet())
    	{
    		userTextView.getItems().add(userName);
    	}
    }
    
    private void addUser(User newUser)
    {	
    	messageTextView.getItems().add("AddUser called");
    	//put the user on the user maps
    	userNameIDMap.put(newUser.getName(), newUser.getId());
    	//
    	userIDNameMap.put(newUser.getId(), newUser.getName());
    	//display the user
    	displayUsers();
    }
    
    private void deleteUser(String userName)
    {
    	messageTextView.getItems().add("deleteUser called");
    	//put the new user on the map
    	userNameIDMap.remove(userName);
    	//display the user
    	displayUsers();
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
   
        //initialize everything
        recepientList = new TreeSet<Integer>();
        userNameIDMap = new TreeMap<String, Integer>();
        userIDNameMap = new TreeMap<Integer, String>();
        userTextView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String userName) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                    	recepientList.add(userNameIDMap.get(userName));
                    } else {
                    	recepientList.remove(userNameIDMap.get(userName));
                    }
                });
                return observable;
            }
        }));
    }
    
    

class Call implements Consumer<Serializable>{

	@Override
	public void accept(Serializable data) {
		Platform.runLater(()-> {
				if (data instanceof User)
			{
				User userData = (User) data;
				if (userData.getOnline()) // the user has connected
				{
					addUser(userData);
					messageTextView.getItems().add(userData.getName() + " connected to the server");
				}
				else //the user disconnected
				{
					deleteUser(userData.getName());
					messageTextView.getItems().add(userData.getName() + " disconnected from the server");
				}
			}
			else if (data instanceof Message)
			{
				Message message = (Message) data;
				if (userNameIDMap.size() > message.getRecepients().size())
				{
					String recepientList = " (privately to ";
					for (Integer rec: message.getRecepients())
					{
						recepientList += userIDNameMap.get(rec);
					}
					recepientList += ")";
					messageTextView.getItems().add(message.getSender() + " sent: " + message.getMessage() + recepientList);
				}
				else
				{
					messageTextView.getItems().add(message.getSender() + " sent: " + message.getMessage());
				}
			}
			else if (data instanceof String)
			{
				messageTextView.getItems().add(data.toString());
			}
			}
		);
		
	}
	
}

}

