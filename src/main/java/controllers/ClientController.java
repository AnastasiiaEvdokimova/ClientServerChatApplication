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
    
    private TreeSet<Integer> recepientsList;
    
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
    	
    	String messageText = messageTextfield.getText();
    	if (messageText == "") messageTextfield.setText("Please enter your message");
    	else {
    		clientConnection.send(messageText, recepientsList);
    		messageTextfield.clear();
    	}
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
    //adds the new user and updates the online list. Synchronized so that multiple updates won't mess with each other
    private synchronized void addUser(User newUser)
    {	
    	//put the user on the user maps
    	userNameIDMap.put(newUser.getName(), newUser.getId());
    	//
    	userIDNameMap.put(newUser.getId(), newUser.getName());
    	//display the user
    	displayUsers();
    }
    //removes the new user and updates the online list. Synchronized so that multiple updates won't mess with each other
    private synchronized void deleteUser(User user)
    {
    	//remove the user from the maps
    	userNameIDMap.remove(user.getName());
    	userIDNameMap.remove(user.getId());
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
        recepientsList = new TreeSet<Integer>();
        userNameIDMap = new TreeMap<String, Integer>();
        userIDNameMap = new TreeMap<Integer, String>();
        
        //this code sets the handlers on all the checkboxes
        userTextView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String userName) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                    	//messageTextView.getItems().add(userName + " added to recepients list");
                    	recepientsList.add(userNameIDMap.get(userName));
                    } else {
                      //	messageTextView.getItems().add(userName + " removed from recepients list");
                    	recepientsList.remove(userNameIDMap.get(userName));
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
					deleteUser(userData);
					messageTextView.getItems().add(userData.getName() + " disconnected from the server");
				}
			}
			else if (data instanceof Message)
			{
				Message message = (Message) data;
				// if the message was not meant for all the users
				if (userNameIDMap.size() > message.getRecepients().size() && message.getRecepients().size() != 1)
				{
					//add a marker to it
					String recepientList = " (privately to ";
					for (Integer rec: message.getRecepients())
					{
						recepientList += userIDNameMap.get(rec) + ", ";
					}
					//get rid of the last ", "
					recepientList = recepientList.substring(0, recepientList.length()-2);
					recepientList += ")";
					messageTextView.getItems().add(message.getSender() + " sent: " + message.getMessage() + recepientList);
				}
				else
				{
					messageTextView.getItems().add(message.getSender() + " sent: " + message.getMessage());
				}
			}
				//this is mostly here for debug/special notices from the server
			else if (data instanceof String)
			{
				messageTextView.getItems().add(data.toString());
			}
			}
		);
		
	}
	
}

}

