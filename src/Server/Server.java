package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
	private static File directory;
	private File file;

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
				users += clients.get(i).lastName + " " + clients.get(i).name
						+ " " + clients.get(i).secondName + clients.get(i).ID +" - "+clients.get(i).post
						+ "/n/";
			}
			users += clients.get(clients.size() - 1).lastName + " "
					+ clients.get(clients.size() - 1).name + " "
					+ clients.get(clients.size() - 1).secondName
					+ clients.get(clients.size() - 1).ID + " - "+clients.get(clients.size() - 1).post+"/e/";
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
		String string = new String(packet.getData(), packet.getOffset(),packet.getLength());
		if (string.startsWith("/c/")) {

			String[] users = string.split("/c/|/n/|/e/");

			int id = Integer.parseInt(string.replaceAll("[^0-9]", ""));
			System.out.println("ID " + id);
			clients.add(new ServerClient(users[1], users[2], users[3], packet
					.getAddress(), packet.getPort(), id, users[5]));
			System.out.println(users[1] + users[2] + users[3]);
			String connectionID = "/c/";
			send(connectionID, packet.getAddress(), packet.getPort());

		} else if (string.startsWith("/f/")) {
			
			String message = string.split("/f/|/e/")[1];
			String fileName = string.split("/id/|/e2/")[1];
			String idClient = fileName.split("_")[0];
			String idSecondClient = fileName.split("_")[1];
			String FileNameAnother = idSecondClient + "_" + idClient;
			
			
			
			File folder = new File("C:\\EclipseProjects\\PopupMenu\\src\\Server\\ServerUser\\");
			File[] folderEntries = folder.listFiles();
			for (File entry : folderEntries) {
				if (entry.isFile()) {
					if (entry.getName().equals(fileName + ".txt")) {
						System.out.println("File confirm 1");
						
						//записать в файл
						try {
							OutputStream outputStream = new FileOutputStream(entry, true);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
							outputStreamWriter.write(message + "\n");
							outputStreamWriter.flush();
							outputStreamWriter.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
						String str = "/m/" + message +"/e/"+idClient+"/e2/";
						// каждому пользователю свой файл отправляется
						for (int i = 0; i < clients.size(); i++) {
							ServerClient client = clients.get(i);
							if (clients.get(i).getID() == Integer.parseInt(idSecondClient)) {
								send(str.getBytes(), client.address,client.port);
							}
							if(clients.get(i).getID() == Integer.parseInt(idClient)){
								send(("/p/"+message), client.address,client.port);
							}

						}
						
					} else if (entry.getName().equals(FileNameAnother + ".txt")) {
						System.out.println("File confirm 2");
						
						//записать в файл
						try {
							OutputStream outputStream = new FileOutputStream(entry, true);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
							outputStreamWriter.write(message + "\n");
							outputStreamWriter.flush();
							outputStreamWriter.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						String str = "/m/" + message +"/e/"+idClient+"/e2/";
						// каждому пользователю свой файл отправляется
						for (int i = 0; i < clients.size(); i++) {
							ServerClient client = clients.get(i);
							if (clients.get(i).getID() == Integer.parseInt(idSecondClient)) {
								send(str.getBytes(), client.address,client.port);
							}
							if(clients.get(i).getID() == Integer.parseInt(idClient)){
								send(("/p/"+message).getBytes(), client.address,client.port);
							}

						}
					}else if(entry.getName().equals(idSecondClient + ".txt")){
						//записать в файл
						try {
							OutputStream outputStream = new FileOutputStream(entry, true);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
							outputStreamWriter.write(message + "\n");
							outputStreamWriter.flush();
							outputStreamWriter.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						sendToAll("/m/" + message + "/e/"+idSecondClient+"/e2/");
					}
				}

			}
			
			
		} else if (string.startsWith("/d/")) {
			String id = string.split("/d/|/e/")[1];
			disconnect(Integer.parseInt(id), true);
		} else if (string.startsWith("/id/")) {

			String fileName = string.split("/id/|/e/")[1];
			String idClient = fileName.split("_")[0];
			String idSecondClient = fileName.split("_")[1];
			String FileNameAnother = idSecondClient + "_" + idClient;
			if (fileName.split("_")[1].equals("Общий чат")) {
				file = new File("C:\\EclipseProjects\\PopupMenu\\src\\Server\\ServerUser\\"+ fileName.split("_")[1] + ".txt");
				System.out.println("Имя файла пришло - "+ fileName.split("_")[1]);
				if (file.exists()) {
					System.out.println("File confirm");
					try {
						BufferedReader bfreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
						String str;

						while ((str = bfreader.readLine()) != null) {
							System.out.println(str + "\n");
							str = "/id/" + str;
							// каждому пользователю свой файл отправляется
							for (int i = 0; i < clients.size(); i++) {
								ServerClient client = clients.get(i);
								if (clients.get(i).getID() == Integer.parseInt(idClient)) {
									send(str.getBytes(), client.address,client.port);
								}

							}
						}
						bfreader.close();
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			} else {
				File folder = new File("C:\\EclipseProjects\\PopupMenu\\src\\Server\\ServerUser\\");
				File[] folderEntries = folder.listFiles();
				boolean doFile = false;
				for (File entry : folderEntries) {
					if (entry.isFile()) {
						if (entry.getName().equals(fileName + ".txt")) {
							System.out.println("File confirm 1");
							try {
								BufferedReader bfreader = new BufferedReader(new InputStreamReader(new FileInputStream(entry),"UTF-8"));
								String str;

								while ((str = bfreader.readLine()) != null) {
									System.out.println(str + "\n");
									str = "/id/" + str;
									// каждому пользователю свой файл отправляется
									for (int i = 0; i < clients.size(); i++) {
										ServerClient client = clients.get(i);
										if (clients.get(i).getID() == Integer.parseInt(idClient)) {
											send(str.getBytes(),client.address, client.port);
										}

									}
								}
								bfreader.close();
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
							doFile = true;
							break;
						} else if (entry.getName().equals(FileNameAnother + ".txt")) {
							System.out.println("File confirm 2");
							try {
								BufferedReader bfreader = new BufferedReader(new InputStreamReader(new FileInputStream(entry),"UTF-8"));
								String str;

								while ((str = bfreader.readLine()) != null) {
									System.out.println(str + "\n");
									str = "/id/" + str;
									for (int i = 0; i < clients.size(); i++) {
										ServerClient client = clients.get(i);
										if (clients.get(i).getID() == Integer.parseInt(idClient)) {
											send(str.getBytes(),client.address, client.port);
										}

									}
								}
								bfreader.close();
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
							doFile = true;
							break;
						}
					}

				}
				if (doFile == false) {
					File file2 = new File("C:\\EclipseProjects\\PopupMenu\\src\\Server\\ServerUser\\" + fileName + ".txt");
					if(file2.exists()){
						System.out.println("file enabled");
					}else{
					try {
						boolean created = file2.createNewFile();
						if (created) {
							System.out.println("file created");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			}
			}

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
