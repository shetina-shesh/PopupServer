package Server;

import java.net.InetAddress;

public class ServerClient {
	
	public String name, lastName, secondName;
	public String post, room;
	public InetAddress address;
	public int port;
	public final int ID;
	public int attempt = 0;

	public ServerClient(String lastName, String name, String secondName, InetAddress address, int port, final int ID, String post, String room){
		this.lastName = lastName;
		this.name = name;
		this.secondName = secondName;
		this.address = address;
		this.port = port;
		this.ID = ID;
		this.post = post;
		this.room = room;
	}
	
	public int getID(){
		return ID;
	}
}
