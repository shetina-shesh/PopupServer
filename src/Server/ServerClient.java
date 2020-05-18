package Server;

import java.net.InetAddress;

public class ServerClient {
	
	public String name, lastName, secondName;
	public String post;
	public InetAddress address;
	public int port;
	public final int ID;
	public int attempt = 0;

	public ServerClient(String lastName, String name, String secondName, InetAddress address, int port, final int ID, String post){
		this.lastName = lastName;
		this.name = name;
		this.secondName = secondName;
		this.address = address;
		this.port = port;
		this.ID = ID;
		this.post = post;
	}
	
	public int getID(){
		return ID;
	}
}
