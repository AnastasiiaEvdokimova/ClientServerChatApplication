import java.io.Serializable;
//this object is passed from the server to clients as the "who is online" information
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3680946942516191610L;
	int id;
	String userName;
	boolean isOnline;
	
	public User(int id) {
		this.userName = "Unknown";
		this.id = id;
		//the user is online by default
		isOnline = true;
	}
	
	public void setName(String userName)
	{
		this.userName = userName;
	}
	
	public String getName()
	{
		return userName;
	}
	
	public int getId()
	{
		return id;
	}
	/*Returns true if the user is still online*/
	public boolean getOnline()
	{
		return isOnline;
	}
	
	public void setOnline(boolean onlineStatus)
	{
		this.isOnline = onlineStatus;
	}
}
