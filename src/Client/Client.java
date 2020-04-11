package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String address = "localhost";
	private int port = 8192;
	private String lastNameString, secondNameString, nameString;
	private JTextField txtSend;
	private JList lRoom;
	private JList lPerson;
	private JTextArea txtHistory;
	
	private DatagramSocket socket;
	private InetAddress ip;
	
	private Thread send;

	public Client(String lastName, String name, String secondName) {
		this.lastNameString = lastName;
		this.nameString = name;
		this.secondNameString = secondName;
		boolean connect = openConnection();
		if(!connect){
			System.err.println("Connection failed!");
			console("Connection failed!");
		}
		createWindow();
		console("Добро пожаловать " +lastNameString+" "+nameString+" "+secondNameString+". Выберите пользователя.");
	}
	
	
	private boolean openConnection(){
		try {
			socket = new DatagramSocket(port);
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
	
	private String receive(){
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
	
	private void send(final byte[] data){
		send = new Thread("Send"){
			public void run(){
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	private void createWindow(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		setTitle("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 480);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtHistory = new JTextArea();
		txtHistory.setFont(new Font("Monospaced", Font.PLAIN, 16));
		//txtHistory.setBounds(241, 65, 407, 327);
		//contentPane.add(txtHistory);
		JScrollPane scroll = new JScrollPane(txtHistory);
		scroll.setBounds(241, 65, 500, 327);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll);
		
		txtSend = new JTextField();
		txtSend.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					send(txtSend.getText());
				}
			}
		});
		txtSend.setBounds(238, 410, 410, 22);
		contentPane.add(txtSend);
		txtSend.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtSend.getText());
			}
		});
		btnSend.setBounds(661, 409, 97, 25);
		contentPane.add(btnSend);
		
		lRoom = new JList();
		lRoom.setBounds(12, 65, 202, 327);
		contentPane.add(lRoom);
		
		lPerson = new JList();
		lPerson.setBounds(241, 23, 407, 29);
		contentPane.add(lPerson);
		setVisible(true);
	}
	
	public void console(String message){
		txtHistory.append(message + "\n\r");
	}
	
	public void send(String message){
		if(message.equals("")) return;
		message = nameString + ": " + message;
		console(message);
		txtSend.setText("");
	}
}
