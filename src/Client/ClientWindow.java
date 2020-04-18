package Client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.DefaultListModel;
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
import javax.swing.JLabel;

public class ClientWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtSend;
	private JList lPerson;
	private JTextArea txtHistory;
	
	private Client client;
	
	private Thread run, listen;
	
	private boolean running = false;
	
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
					send(txtSend.getText(), true);
				}
			}
		});
		txtSend.setBounds(238, 410, 410, 22);
		contentPane.add(txtSend);
		txtSend.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtSend.getText(), true);
			}
		});
		btnSend.setBounds(661, 409, 97, 25);
		contentPane.add(btnSend);
		
		lPerson = new JList();
		lPerson.setBounds(12, 65, 202, 327);
		contentPane.add(lPerson);
		
		JLabel lblNewLabel = new JLabel("\u041E\u043D\u043B\u0430\u0439\u043D \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u0438:");
		lblNewLabel.setBounds(12, 41, 191, 16);
		contentPane.add(lblNewLabel);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				String disconnect = "/d/" + client.getIdPerson() + "/e/";
				send(disconnect, false);
				client.close();
				running = false;
			}
		});
		
		setVisible(true);
	}

	public void console(String message){
		txtHistory.append(message + "\n\r");
	}
	
	public void run(){
		listen();
	}
	
	public void send(String message, boolean text){
		if(message.equals("")) return;
		if(text){
			message = client.getName() + ": " + message;
			message = "/m/" + message;
		}
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
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
					}else if(message.startsWith("/u/")){
						String[] u = message.split("/u/|/n/|/e/");
						update(Arrays.copyOfRange(u, 1, u.length - 1));
					}
				}
			}
		};
		listen.start();
	}
	
	public void update(String[] users){
		lPerson.setListData(users);
	}
}
