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

public class RedactPerson extends JFrame {

	private JPanel contentPane;
	private static final long serialVersionUID = 1L;
	private JTextField txtAddRoom;
	private JTextField txtLastName;
	private JTextField txtName;
	private JTextField txtSecondName;
	private JTextField txtPost;
	private JTextField txtLogin;
	private JTextField txtPassword;
	



	/**
	 * Create the frame.
	 */
	public RedactPerson() {
		setTitle("\u0420\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435");
		setBounds(100, 100, 615, 255);
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
		lblLastname.setBounds(220, 23, 85, 16);
		contentPane.add(lblLastname);
		
		JLabel lblName = new JLabel("\u0418\u043C\u044F");
		lblName.setBounds(348, 23, 56, 16);
		contentPane.add(lblName);
		
		JLabel lblSecondname = new JLabel("\u041E\u0442\u0447\u0435\u0441\u0442\u0432\u043E");
		lblSecondname.setBounds(455, 23, 116, 16);
		contentPane.add(lblSecondname);
		
		JLabel lblPost = new JLabel("\u0414\u043E\u043B\u0436\u043D\u043E\u0441\u0442\u044C");
		lblPost.setBounds(27, 87, 101, 16);
		contentPane.add(lblPost);
		
		txtLastName = new JTextField();
		txtLastName.setBounds(189, 52, 116, 22);
		txtLastName.setText(Menu.lastNameString);
		contentPane.add(txtLastName);
		txtLastName.setColumns(10);
		
		txtName = new JTextField();
		txtName.setBounds(317, 52, 116, 22);
		txtName.setText(Menu.nameString);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtSecondName = new JTextField();
		txtSecondName.setBounds(445, 52, 116, 22);
		txtSecondName.setText(Menu.secondNameString);
		contentPane.add(txtSecondName);
		txtSecondName.setColumns(10);
		
		txtPost = new JTextField();
		txtPost.setBounds(12, 116, 116, 22);
		txtPost.setText(Menu.postString);
		contentPane.add(txtPost);
		txtPost.setColumns(10);
		
		JLabel lblLogin = new JLabel("\u041B\u043E\u0433\u0438\u043D");
		lblLogin.setBounds(283, 92, 56, 16);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(242, 116, 116, 22);
		txtLogin.setText(Menu.loginString);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u041F\u0430\u0440\u043E\u043B\u044C");
		lblNewLabel.setBounds(398, 92, 56, 16);
		contentPane.add(lblNewLabel);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(370, 116, 116, 22);
		txtPassword.setText(Menu.passwordString);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JButton btnSave = new JButton("\u0421\u043E\u0445\u0440\u0430\u043D\u0438\u0442\u044C \u0438\u0437\u043C\u0435\u043D\u0435\u043D\u0438\u044F");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// method
				
				if(txtLastName.getText().equals("") || txtName.getText().equals("") || txtSecondName.getText().equals("") || txtPost.getText().equals("") || txtLogin.getText().equals("") || txtPassword.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Все поля должны быть заполнены", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				
				String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=TestBaza;integratedSecurity=true;";
				
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();Statement stmt2 = con.createStatement();) 
				{
				            String SQL = "UPDATE Colloborators SET LastName = '"+ txtLastName.getText() +"', Name = '"+ txtName.getText() +"', SecondName = '"+ txtSecondName.getText() +"', Post = '"+ txtPost.getText() +"' WHERE ID = "+ Menu.idPerson +"";
				            String SQL2 = "UPDATE Login SET Login = '"+ txtLogin.getText()+"' , Password = '"+ txtPassword.getText() +"' WHERE ID_L = "+Menu.idPerson+"";
				            stmt.executeUpdate(SQL);
				            stmt2.executeUpdate(SQL2);
				            JOptionPane.showMessageDialog(null, "Данные обновлены", "Выполнено", JOptionPane.INFORMATION_MESSAGE);
				            dispose();
				            
		            
		        }
		        // Handle any errors that may have occurred.
		        catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
			}
		});
		btnSave.setBounds(203, 170, 190, 25);
		contentPane.add(btnSave);
	}
}
