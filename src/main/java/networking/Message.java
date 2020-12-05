package networking;
import java.io.Serializable;
import java.util.TreeSet;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2723363051271966964L;
	
	private String message;
	private TreeSet<Integer> receiverID;
	private String sender;
	
	// the sender will construct a message and determine the recepients
	public Message(String message, TreeSet<Integer> toWhom)
	{
		this.message = message;
		receiverID = toWhom;
	}
	
	public void setSender(String sender)
	{
		this.sender = sender;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public TreeSet<Integer> getRecepients()
	{
		return receiverID;
	}
}
