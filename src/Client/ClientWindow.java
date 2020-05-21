package Client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.omg.CORBA.PUBLIC_MEMBER;

public class ClientWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtSend;
	private JList lPerson;
	private JTextArea txtHistory;
	
	private Client client;
	
	private Thread run, listen;
	
	private boolean running = false;
	private JTabbedPane tabbedPane;
	private HashMap<String, String> hashMapUsers;
	
	public ClientWindow(String lastName, String name, String secondName, Integer idPerson, String post, String room) {
		client = new Client(lastName, name, secondName, idPerson, post, room);
		boolean connect = client.openConnection();
		if(!connect){
			System.err.println("Соединение разорвано!");
			console("Соединение разорвано!");
		}
		createWindow();
		console("Добро пожаловать " +client.getLastName()+" "+client.getName()+" "+client.getSecondName()+". \nВыберите пользователя.");
		//Покаывает подключение рызных пользователей
		String connection = "/c/" + client.getLastName() + "/n/" + client.getName() + "/n/" + client.getSecondName() + "/n/" + client.getIdPerson() + "/n/"+client.getPost() + "/n/"+client.getRoom()+"/e/";
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
		
		setTitle("\u041A\u043E\u0440\u043F\u043E\u0440\u0430\u0442\u0438\u0432\u043D\u044B\u0439 \u0447\u0430\u0442");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(932, 656);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtHistory = new JTextArea();
		txtHistory.setFont(new Font("Monospaced", Font.PLAIN, 16));
		JScrollPane scroll = new JScrollPane(txtHistory);
		scroll.setBounds(316, 217, 598, 354);
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
		txtSend.setBounds(316, 584, 489, 22);
		contentPane.add(txtSend);
		txtSend.setColumns(10);
		
		JButton btnSend = new JButton("\u041E\u0442\u043F\u0440\u0430\u0432\u0438\u0442\u044C");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtSend.getText(), true);
				
			}
		});
		btnSend.setBounds(817, 583, 97, 25);
		contentPane.add(btnSend);
		
		lPerson = new JList();
		lPerson.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lPerson.setBounds(12, 193, 295, 415);
		lPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(lPerson);
		
		JLabel lblNewLabel = new JLabel("\u041E\u043D\u043B\u0430\u0439\u043D \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u0438:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(12, 142, 191, 16);
		contentPane.add(lblNewLabel);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.add(null, "Общий чат");
		tabbedPane.setBounds(316, 193, 598, 22);
		contentPane.add(tabbedPane);
		
		JLabel lblProfile = new JLabel("\u0412\u044B \u0432\u043E\u0448\u043B\u0438 \u043A\u0430\u043A:");
		lblProfile.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblProfile.setText("\u0412\u044B \u0432\u043E\u0448\u043B\u0438 \u043A\u0430\u043A - " + client.getLastName()+" "+client.getName()+" "+client.getSecondName()+".");
		lblProfile.setBounds(12, 0, 729, 38);
		contentPane.add(lblProfile);
		
		JLabel lblPost = new JLabel("New label");
		lblPost.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblPost.setText("Ваша должность: " + client.getPost());
		lblPost.setBounds(12, 39, 772, 16);
		contentPane.add(lblPost);
		
		JLabel lblRoom = new JLabel("New label");
		lblRoom.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblRoom.setText("Ваш кабинет: "+ client.getRoom());
		lblRoom.setBounds(12, 63, 704, 16);
		contentPane.add(lblRoom);
		
		JLabel lblNewLabel_1 = new JLabel("\u2116 \u043A\u0430\u0431.");
		lblNewLabel_1.setBounds(12, 171, 56, 16);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u0424\u0418\u041E");
		lblNewLabel_2.setBounds(131, 171, 56, 16);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("\u0414\u043E\u043B\u0436\u043D\u043E\u0441\u0442\u044C");
		lblNewLabel_3.setBounds(233, 171, 74, 16);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("\u0412\u044B\u0431\u0435\u0440\u0438\u0442\u0435 \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044F.");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(12, 92, 237, 37);
		contentPane.add(lblNewLabel_4);
		
		final JPopupMenu pMenuPerson = new JPopupMenu();
		JMenuItem addPerson = new JMenuItem("Написать");
		pMenuPerson.add(addPerson);
		
		// Подключения слушателя
		lPerson.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		lPerson.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					String namePersonTabbedPane = (String)lPerson.getSelectedValue();
					tabbedPane.add(null, namePersonTabbedPane);
					//namePersonTabbedPane.split("-")[0]
				} else if (SwingUtilities.isRightMouseButton(e)
						&& !lPerson.isSelectionEmpty()
						&& lPerson.locationToIndex(e.getPoint()) == lPerson.getSelectedIndex()) {
					pMenuPerson.show(lPerson, e.getX(), e.getY());	
					System.out.print(lPerson.getSelectedValue() + "\n");
				}

			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				String disconnect = "/d/" + client.getIdPerson() + "/e/";
				send(disconnect, false);
				client.close();
				running = false;
			}
		});
		
		addPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// открыть новое окно
				tabbedPane.add(null, lPerson.getSelectedValue());
			}
		});
		
		
		final JPopupMenu pMenuClose = new JPopupMenu();
		JMenuItem deleteTab = new JMenuItem("Закрыть");
		pMenuClose.add(deleteTab);
		
		tabbedPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3 && e.getClickCount() == 1) {
					pMenuClose.show(tabbedPane, e.getX(), e.getY());
				}else if(e.getButton() == 1 && e.getClickCount() == 2){
					int select = tabbedPane.getSelectedIndex();
					String string = tabbedPane.getTitleAt(select);
					txtHistory.setText("");
					for(Map.Entry<String, String> entry: hashMapUsers.entrySet()){
						if(entry.getValue().equals(string)){
							String nameFile = "/id/";
							nameFile += client.getIdPerson() + "_" +entry.getKey() + "/e/";
							sendFile(nameFile);
						}
						
						
					}
					
					if(select == 0){
						// Выбор вкладки общий чат
						String GeneralChat = "/id/"+client.getIdPerson()+"_Общий чат/e/";
						sendFile(GeneralChat);
						System.out.println("Ziro");
					}
				}

			}
		});
		
		
		deleteTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int select = tabbedPane.getSelectedIndex();
				if(select == 0){
					System.out.println("Ziro");
				}
				else{
				tabbedPane.removeTabAt(select);
				}

			}
		});
		
		setVisible(true);
	}
	
	public void sendFile(String nameFile){
		client.send(nameFile.getBytes());
	}

	public void console(String message){
		txtHistory.append(message + "\n\r");
	}
	
	public void fileHistory(String message){
		txtHistory.append(message + "\n\r");
	}
	
	public void run(){
		listen();
	}
	
	public void send(String message, boolean text){
		if(message.equals("")) return;
		
		if(text){
			message = client.getLastName() + " " +client.getName() + ": " + message;
			message = "/f/" + message + "/e//id/"+client.getIdPerson()+"_";
			
			if(tabbedPane.getSelectedIndex() == 0){
				// Выбор вкладки общий чат
				message = message + "Общий чат/e2/";
				System.out.println("Ziro");
			}else{
			
			String string = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			for(Map.Entry<String, String> entry: hashMapUsers.entrySet()){
				if(entry.getValue().equals(string)){
					message = message + entry.getKey()+"/e2/";
				}
				
				
			}
		}
			
			
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
					//console("Успешное подключение к серверу. ID: " + client.getIdPerson());
					console("Успешное подключение к серверу.");
					}else if(message.startsWith("/id/")){
						String text = message.substring(4);
						fileHistory(text);
					}else if(message.startsWith("/p/")){
						String pMessage = message.substring(3);
						console(pMessage.split("/e/")[0]);
					}else if(message.startsWith("/m/")){
						String messageFromClient = message.split("/m/|/e/")[1];
						String idClientMessage = message.split("/e/|/e2/")[1];
						String tabClient = null;
						
						for(Map.Entry<String, String> entry: hashMapUsers.entrySet()){
							if(entry.getKey().equals(idClientMessage)){
								tabClient = entry.getValue();
							}
						}	
						String string = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
						if(string.equals(idClientMessage)){
							console(messageFromClient);
						}else if(string.equals(tabClient)){
							if(client.getIdPerson() == Integer.parseInt(idClientMessage)){
								System.out.println("Сам себе пишет хах");
							}else{
							console(messageFromClient);
							}
						}
					}else if(message.startsWith("/u/")){
						//////////////////////////////////////////
						String[] u = message.split("/u/|/n/|/e/");
						hashMapUsers = new HashMap<String, String>();
						
						System.out.println(u.length);
						
						for(int i=0; i<u.length; i++){
							System.out.println(i + "element = " + u[i]+"\n");
						}
						
						for(int i=1; i<u.length - 1; i++){
							System.out.println(i + " element - " + u[i] + "\n" + "id - "+u[i].split("/i/")[0] + "\n" + "room - "+u[i].split("/i/")[1]);
							
							hashMapUsers.put(u[i].split("/i/")[0], u[i].split("/i/")[1]);
							
						}
						
						String[] mas = new String[u.length];
						
						for(int i=1; i<u.length - 1; i++){

							mas[i] = u[i].split("/i/")[1];
							
						}
						
						update(Arrays.copyOfRange(mas, 1, mas.length - 1));
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
