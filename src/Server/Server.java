package Server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private Thread run, manage, send, receive;
	
	public Server(int port){
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}

	public void run() {
		running = true;
		System.out.println("������ ������� �� �����: " + port);
		manageClients();
		receive();
	}
	
	private void manageClients(){
		manage = new Thread("Manage"){
			public void run(){
				while(running){
					//Managing
				}
			}
		};
		manage.start();
	}
	
	private void receive(){
		receive = new Thread("Receive"){
			public void run(){
				while(running){
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
					//delete
					clients.add(new ServerClient("������", packet.getAddress(), packet.getPort(), 50));
					System.out.println(clients.get(0).address.toString() + ":" + clients.get(0).port);
					
				}
			}
		};
		receive.start();
	}
	
	private void sendToAll(String message){
		for(int i=0; i < clients.size(); i++){
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	private void send(final byte[] data, final InetAddress address, final int port){
		send = new Thread("Send"){
			public void run(){
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	private void send(String message, InetAddress address, int port){
		send(message.getBytes(), address, port);
	}
	
	private void process(DatagramPacket packet){
		//String string = new String(packet.getData());
		String string = new String(packet.getData(),packet.getOffset(),packet.getLength());
		if(string.startsWith("/c/")){
			int id = Integer.parseInt(string.replaceAll("[^0-9]", ""));;
			System.out.println("ID "+ id);
			clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
			System.out.println(string.substring(3, string.length()).replaceAll("[^�-���-ߨ]", ""));
			String connectionID = "/c/";
			send(connectionID, packet.getAddress(), packet.getPort());
		}else if (string.startsWith("/m/")){
			sendToAll(string);
		}else{
			System.out.println(string);
		}
	}
	
}
