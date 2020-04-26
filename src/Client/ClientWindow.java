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
		String connection = "/c/" + client.getLastName() + "/n/" + client.getName() + "/n/" + client.getSecondName() + "/n/" + client.getIdPerson() + "/e/";
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
		lPerson.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lPerson.setBounds(12, 65, 202, 327);
		lPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(lPerson);
		
		JLabel lblNewLabel = new JLabel("\u041E\u043D\u043B\u0430\u0439\u043D \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u0438:");
		lblNewLabel.setBounds(12, 41, 191, 16);
		contentPane.add(lblNewLabel);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.add(null, "Общий чат");
		tabbedPane.setBounds(241, 41, 500, 22);
		contentPane.add(tabbedPane);
		
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
					tabbedPane.add(null, lPerson.getSelectedValue());
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
					console("Успешное подключение к серверу. ID: " + client.getIdPerson());
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
						
						for(int i=0; i<u.length; i++){
							hashMapUsers.put(u[i].replaceAll("[^0-9]", ""), u[i].replaceAll("[^а-яёА-ЯЁ ]", ""));
						}
						
						String[] mas = new String[u.length];
						
						for(int i=0; i<u.length; i++){
							mas[i] = u[i].replaceAll("[^а-яёА-ЯЁ ]", "");
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
