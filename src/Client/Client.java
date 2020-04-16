package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	private static final long serialVersionUID = 1L;

	private String address = "localhost";
	private int port = 8192;
	private String lastNameString, secondNameString, nameString;
	private Integer idPerson;

	private DatagramSocket socket;
	private InetAddress ip;
	private Thread send;

	public Client(String lastName, String name, String secondName,
			Integer idPerson) {
		this.lastNameString = lastName;
		this.nameString = name;
		this.secondNameString = secondName;
		this.idPerson = idPerson;
	}

	public String getLastName() {
		return lastNameString;
	}

	public String getName() {
		return nameString;
	}

	public String getSecondName() {
		return secondNameString;
	}

	public Integer getIdPerson() {
		return idPerson;
	}

	public boolean openConnection() {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);

		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
	}

	public void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length,
						ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	public void close() {
		new Thread() {
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		}.start();
	}

}
