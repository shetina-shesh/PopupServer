package Client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtSend;
	private JList lRoom;
	private JList lPerson;
	private JTextArea txtHistory;
	
	private Client client;
	
	private Thread run, listen;
	
	private boolean running = false;
	//private String address = "localhost";
	
	public ClientWindow(String lastName, String name, String secondName, Integer idPerson) {
		client = new Client(lastName, name, secondName, idPerson);
		boolean connect = client.openConnection();
		if(!connect){
			System.err.println("Соединение разорвано!");
			console("Соединение разорвано!");
		}
		createWindow();
		console("Добро пожаловать " +client.getLastName()+" "+client.getName()+" "+client.getSecondName()+". Выберите пользователя.");
		//Покаывает подключение рызных пользователей
		String connection = "/c/" + client.getName() + client.getIdPerson();
		client.send(connection.getBytes());
		running = true;
		run = new Thread(this, "Running");
		run.start();
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
	
	public void run(){
		listen();
	}
	
	public void send(String message){
		if(message.equals("")) return;
		message = client.getName() + ": " + message;
		message = "/m/" + message;
		client.send(message.getBytes());
		txtSend.setText("");
	}
	
	public void listen(){
		listen = new Thread("Listen"){
			public void run(){
				while(running){
					String message = client.receive();
					if(message.startsWith("/c/")){
					console("Успешное подключение к серверу. ID: " + client.getIdPerson());
					}else if(message.startsWith("/m/")){
						String text = message.split("/m/|/e/")[1];
						console(text);
					}
				}
			}
		};
		listen.start();
	}
	
}
