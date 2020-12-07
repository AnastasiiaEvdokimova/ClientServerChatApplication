package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.TreeSet;
import java.util.function.Consumer;

import networking.Message;
import networking.User;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	boolean setUserName;
	
	String userName;
	
	int myUserID;
	
	boolean isRunning = true;
	
	private Consumer<Serializable> callback;
	public Client(Consumer<Serializable> call, String userName){
		System.out.println(userName);
		callback = call;
		this.userName = userName;
		setUserName = false;
		//callback.accept(this.userName);
		myUserID = 0; //some default value just in case
	}
	
	public void stopThread() {
		isRunning = false;
		try {
			out.writeObject(new Message(null, null));
			}
			catch (Exception ex) {};
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
	    out.writeObject(userName);
		}
		catch(Exception e) {}
		boolean isFirst = true;
		while(isRunning) {
			try {
			Serializable data = (Serializable) in.readObject();
			
			//callback.accept(data.toString());
			
			//the first user received is going to be this clien't data
			if (isFirst && data instanceof User && ((User)data).getName().contains(userName))
			{
				User myData = (User) data;
				userName = myData.getName();
				myUserID = myData.getId();
				isFirst = false;
			}
			 callback.accept(data);
			}
			catch(Exception e) {}
			
		}
		
	
    }
	
	public void send(String message, TreeSet<Integer> recepientsList) {
		recepientsList.add(myUserID); //always add yourself to the recepients list
		
		Message newMessage = new Message(message, recepientsList);
		try {
			out.reset(); //do it or your recepientsList will never change
			out.writeUnshared(newMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
