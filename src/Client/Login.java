package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JTextField txtPassword;
	private String namePerson;
	private String secondNamePerson;
	private String lastNamePerson;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		setTitle("\u0410\u0432\u0442\u043E\u0440\u0438\u0437\u0430\u0446\u0438\u044F");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(425, 479);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String textLogin = txtLogin.getText();
				String textPassword = txtPassword.getText();
				login(textLogin, textPassword);
			}
		});
		btnConnect.setBounds(155, 365, 97, 25);
		contentPane.add(btnConnect);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(122, 157, 163, 22);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(122, 254, 163, 22);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(175, 128, 56, 16);
		contentPane.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(155, 220, 97, 16);
		contentPane.add(lblPassword);
	}
	
	
	private void login(String login, String password) {
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=TestBaza;integratedSecurity=true;";
		
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {

	    	   String SQL ="SELECT Colloborators.LastName, Colloborators.Name, Colloborators.SecondName, Colloborators.Post, Login.Login, Login.Password FROM Colloborators, Login WHERE Colloborators.ID = Login.ID_L AND Login.Login = '"+login+"' AND Login.Password = '"+password+"'";
	    	   ResultSet rs = stmt.executeQuery(SQL);
	    	   
	            while (rs.next()) {	
	            	namePerson = rs.getString("Name");
	            	secondNamePerson = rs.getString("SecondName");
	            	lastNamePerson = rs.getString("LastName");    
	            }
	            
	            System.out.println(lastNamePerson + " " + namePerson + " " + secondNamePerson);

	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
		
		if(namePerson == null){
			JOptionPane.showMessageDialog(null, "Неверный логин или пароль", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
		new Client(lastNamePerson, namePerson, secondNamePerson);
		System.out.println(login + ", " + password);
		dispose();
		}
		
	}
	
}
