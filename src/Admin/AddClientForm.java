package Admin;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddClientForm extends JFrame {

	private JPanel contentPane;
	private static final long serialVersionUID = 1L;
	private JTextField txtAddRoom;
	private JTextField txtLastName;
	private JTextField txtName;
	private JTextField txtSecondName;
	private JTextField txtPost;
	private JTextField txtLogin;
	private JTextField txtPassword;
	
	private String room, lastname, name, secondname, post;
	private String idRoom, idPerson;
	private String login, password;
	private boolean flag = true;


	/**
	 * Create the frame.
	 */
	public AddClientForm() {
		setTitle("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044F");
		setBounds(100, 100, 589, 248);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblRoom = new JLabel("\u041A\u0430\u0431\u0438\u043D\u0435\u0442");
		lblRoom.setBounds(43, 31, 56, 16);
		contentPane.add(lblRoom);
		
		txtAddRoom = new JTextField();
		txtAddRoom.setEditable(false);
		txtAddRoom.setBounds(12, 52, 116, 22);
		txtAddRoom.setText(Menu.roomNumber);
		contentPane.add(txtAddRoom);
		txtAddRoom.setColumns(10);
		
		JLabel lblLastname = new JLabel("\u0424\u0430\u043C\u0438\u043B\u0438\u044F");
		lblLastname.setBounds(171, 23, 85, 16);
		contentPane.add(lblLastname);
		
		JLabel lblName = new JLabel("\u0418\u043C\u044F");
		lblName.setBounds(299, 23, 56, 16);
		contentPane.add(lblName);
		
		JLabel lblSecondname = new JLabel("\u041E\u0442\u0447\u0435\u0441\u0442\u0432\u043E");
		lblSecondname.setBounds(407, 23, 116, 16);
		contentPane.add(lblSecondname);
		
		JLabel lblPost = new JLabel("\u0414\u043E\u043B\u0436\u043D\u043E\u0441\u0442\u044C");
		lblPost.setBounds(22, 87, 97, 16);
		contentPane.add(lblPost);
		
		txtLastName = new JTextField();
		txtLastName.setBounds(140, 52, 116, 22);
		contentPane.add(txtLastName);
		txtLastName.setColumns(10);
		
		txtName = new JTextField();
		txtName.setBounds(268, 52, 116, 22);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtSecondName = new JTextField();
		txtSecondName.setBounds(397, 52, 116, 22);
		contentPane.add(txtSecondName);
		txtSecondName.setColumns(10);
		
		txtPost = new JTextField();
		txtPost.setBounds(12, 116, 116, 22);
		contentPane.add(txtPost);
		txtPost.setColumns(10);
		
		JButton btnAddPerson = new JButton("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C");
		btnAddPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//addPerson method			
				room = txtAddRoom.getText();		
				lastname = txtLastName.getText();
				name = txtName.getText();
				secondname = txtSecondName.getText();
				post = txtPost.getText();
				login = txtLogin.getText();
				password = txtPassword.getText();
				
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				
				String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=TestBaza;integratedSecurity=true;";
				
				
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = "SELECT ID, RoomNumber FROM RoomTable";
		            ResultSet rs = stmt.executeQuery(SQL);
		            
		            while (rs.next()) {
		            	if(rs.getString("RoomNumber").equals(room)){
		            		idRoom = rs.getString("ID");
		            		break;
		            	}
		            	}
		            
		            	if(lastname.equals("") || name.equals("") || secondname.equals("") || post.equals("") || login.equals("") || password.equals("")){
		            		JOptionPane.showMessageDialog(null, "Все поля должны быть заполнены", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
		            	}
		            	else{
		            		
		 			            String SQL2 = "INSERT INTO Colloborators VALUES('"+idRoom+"','"+lastname+"','"+name+"','"+secondname+"','"+post+"')";
		 			            stmt.executeUpdate(SQL2);
		 			            
		 			            String SQL3 = "SELECT * FROM Colloborators";
		 			            ResultSet rs3 = stmt.executeQuery(SQL3);
		 			            
		 			            while(rs3.next()){
		 			            	if(lastname.equals(rs3.getString("LastName")) && name.equals(rs3.getString("Name")) && secondname.equals(rs3.getString("SecondName")) && post.equals(rs3.getString("Post"))){
		 			            		idPerson = rs3.getString("ID");
		 			            		break;
		 			            	}
		 			            }
		 			            
		 			            String SQL4 = "INSERT INTO Login VALUES ('"+ idPerson +"','"+ login +"','"+ password +"')";
		 			           stmt.executeUpdate(SQL4);
		 			           
		 			       JOptionPane.showMessageDialog(null, "Пользователь добавлен", "Успеншо", JOptionPane.INFORMATION_MESSAGE);
		 			       dispose();
		            	}
		            
		        }
		        // Handle any errors that may have occurred.
		        catch (SQLException e) {
		            e.printStackTrace();
		        }
						
			
		}
		});
		btnAddPerson.setBounds(240, 163, 97, 25);
		contentPane.add(btnAddPerson);
		
		JLabel lblLogin = new JLabel("\u041B\u043E\u0433\u0438\u043D");
		lblLogin.setBounds(257, 92, 56, 16);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(227, 116, 116, 22);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u041F\u0430\u0440\u043E\u043B\u044C");
		lblNewLabel.setBounds(407, 87, 56, 16);
		contentPane.add(lblNewLabel);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(379, 116, 116, 22);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
	}
}
