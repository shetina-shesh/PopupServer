package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String login, password;
	private JTextField txtSend;
	private JList lRoom;
	private JList lPerson;

	public Client(String login, String password) {
		this.login = login;
		this.password = password;
		createWindow();
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
		
		JTextArea txtHistory = new JTextArea();
		txtHistory.setBounds(241, 65, 407, 327);
		contentPane.add(txtHistory);
		
		txtSend = new JTextField();
		txtSend.setBounds(238, 410, 410, 22);
		contentPane.add(txtSend);
		txtSend.setColumns(10);
		
		JButton btnSend = new JButton("Send");
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
}
