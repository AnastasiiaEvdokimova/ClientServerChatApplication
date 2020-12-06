package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.function.Consumer;

import networking.Message;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */
import networking.User;
import server.Server.ClientThread;

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	private TreeSet<String> takenUsernames;
	
	public Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
		takenUsernames = new TreeSet<String>();
	}
	
	public void stopServer() {
		System.out.println("Server stopped");
		for(ClientThread client : clients) {
			client.stopThread();
			try {
				client.connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			server.mysocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.accept("Server stopped!");
	}
	
	
	public class TheServer extends Thread{
		public ServerSocket mysocket;
		public void run() {
		
			try{
				
		    mysocket = new ServerSocket(5555);
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int id;
			String userName;
			ObjectInputStream in;
			ObjectOutputStream out;
			boolean isRunning = true;
			
			void stopThread() {
				isRunning = false;
			}
			
			ClientThread(Socket s, int id){
				this.connection = s;
				this.id = id;	
			}
			
			private void sendUsers()
			{
				//the client needs to know about all the old clients
				for (ClientThread client: clients)
				{
					User oldUser = new User(client.id);
					oldUser.setName(client.userName);
					try {
					if (oldUser.getId() != id) out.writeUnshared(oldUser); //don't send the info about themselves twice
					}
					catch(Exception ex) {
						callback.accept("Connection with the client #" + id + "  lost");
					}
				}
			}
			
			public void sendMessage(Message message) {
				message.setSender(userName);
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
						//only send the message if the client is the recepient of it
						if (message.getRecepients().contains(t.id))
							t.out.writeUnshared(message);
					}
					catch(Exception e) {}
				}
			}
			//if isOnline is true, the new client has joined the server. If false, they disconnected
			public void updateClients(User user) {
				System.out.println("update: " + user.getOnline());
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
						//send the info about connection/disconnects to the users
						//have to use writeUnshared - otherwise, the older clients will get a wrong online status when a newer client leaves
							t.out.writeUnshared(user); 
					}
					catch(Exception e) {}
				}
			}
			
			public void run(){
				
				boolean nicknameSet = false;
				User user = new User(id);
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					callback.accept("Streams not open");
				}
				callback.accept("Waiting for username");
				//the user is not connected truly until they have set a nickname
				while (!nicknameSet && isRunning)
				{
					 try {
						 Object data = in.readObject();
						 callback.accept(data.toString());
						 if (data instanceof String) {
					    	String userName = data.toString();
					    	//check that the nickname is free
					    	if (takenUsernames.contains(userName))
					    	{
					    		int i = 0;
					    		String alternativeUsername;
					    		// if not, create another username by adding a number to it
					    		do {
					    			i++;
					    			alternativeUsername = userName + i;
					    			//..and seeing whether this one was taken too
					    		} while (takenUsernames.contains(alternativeUsername));
					  
					    		userName = alternativeUsername;
					    	}
					    	callback.accept("client: " + id + " set user name to " + userName);
					    	takenUsernames.add(userName);
					    	this.userName = userName;
					    	user.setName(userName);
					    	nicknameSet = true;
					    	updateClients(user); //tell other clients that there is a new user
					    	sendUsers(); // send all those who are already connected to the new user
						 }
					    	}
					    catch(Exception e) {
					    	System.out.println(e.getMessage());
					    	callback.accept("Something wrong with the socket from client: " + id + "....closing down!");
					    	takenUsernames.remove(userName);
					    	//sendMessage("Client #"+id+" has left the server!");
					    	clients.remove(this);
					    	return;
					    }
				}
				
				user.setOnline(false);
				
				//sendMessage("new client on server: client #"+id);
				
				//switch to the message accepting mode
					
				 while(isRunning) {
					    try {
					    	Message msg = (Message) in.readObject();
					    	String chatLog = "client: " + user.getName() + " sent: " + msg.getMessage() + " to: ";
					    	for (Integer i: msg.getRecepients())
					    	{
					    		chatLog += i + " ";
					    	}
					    	callback.accept(chatLog);
					    	
					    	sendMessage(msg);
					    	
					    	}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + id + "....closing down!");
					    	user.setOnline(false);
					    	System.out.println("run: " + user.getOnline());
					    	updateClients(user);
					    	takenUsernames.remove(userName);
					    	//sendMessage("Client #"+id+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}


	
	

	
