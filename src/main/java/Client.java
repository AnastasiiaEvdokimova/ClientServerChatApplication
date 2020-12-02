import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.TreeSet;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	TreeSet<Integer> recepientsList;
	
	boolean setUserName;
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
		recepientsList = new TreeSet<Integer>();
		 setUserName = false;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
	    out.writeObject("MyNewName");
		}
		catch(Exception e) {}
		
		User userData;
		while(true) {
			userData = null;
			try {
			Object data = in.readObject();
			if (data instanceof User)
			{
				userData = (User) data;
				if (userData.getOnline()) // the user has connected
				{
					callback.accept("New user has joined: " + userData.getName());
				}
				else //the user disconnected
				{
					callback.accept("A user has left: " + userData.getName());
				}
			}
			else if (data instanceof Message)
			{
				Message message = (Message) data;

				callback.accept(message.getSender() + " sent:" + message.getMessage());
			}
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(String data) {
		recepientsList.add(1);
		Message newMessage = new Message(data, recepientsList);
		
		try {
			
			out.writeObject(newMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
