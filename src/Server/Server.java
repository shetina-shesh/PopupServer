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

	public Server(int port) {
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
		System.out.println("Сервер запущен на порту: " + port);
		manageClients();
		receive();
	}

	
	private void sendStatus() {
		if (clients.size() <= 0) {
			return;
		} else {
			String users = "/u/";
			for (int i = 0; i < clients.size() - 1; i++) {
				users += clients.get(i).name + "/n/";
			}
			users += clients.get(clients.size() - 1).name + "/e/";
			sendToAll(users);
		}
	}
	

	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					sendStatus();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		manage.start();
	}

	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data,
							data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);

				}
			}
		};
		receive.start();
	}

	private void sendToAll(String message) {
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}

	private void send(final byte[] data, final InetAddress address,
			final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length,
						address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void send(String message, InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}

	private void process(DatagramPacket packet) {
		// String string = new String(packet.getData());
		String string = new String(packet.getData(), packet.getOffset(),
				packet.getLength());
		if (string.startsWith("/c/")) {
			int id = Integer.parseInt(string.replaceAll("[^0-9]", ""));
			;
			System.out.println("ID " + id);
			clients.add(new ServerClient(string.substring(3, string.length())
					.replaceAll("[^а-яёА-ЯЁ]", ""), packet.getAddress(), packet
					.getPort(), id));
			System.out.println(string.substring(3, string.length()).replaceAll(
					"[^а-яёА-ЯЁ]", ""));
			String connectionID = "/c/";
			send(connectionID, packet.getAddress(), packet.getPort());
		} else if (string.startsWith("/m/")) {
			sendToAll(string);
		} else if (string.startsWith("/d/")) {
			String id = string.split("/d/|/e/")[1];
			disconnect(Integer.parseInt(id), true);
		} else {
			System.out.println(string);
		}
	}

	private void disconnect(int id, boolean status) {
		ServerClient c = null;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getID() == id) {
				c = clients.get(i);
				clients.remove(i);
				break;
			}
		}
		String message = "";
		if (status) {
			message = "Client " + c.name + " (" + c.getID() + ") @ "
					+ c.address.toString() + ":" + c.port + " disconnected.";
		}
		System.out.println(message);
	}

}
