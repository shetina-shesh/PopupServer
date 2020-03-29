package Popup;

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

public class InfoClientForm extends JFrame {

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


	/**
	 * Create the frame.
	 */
	public InfoClientForm() {
		setTitle("\u0418\u043D\u0444\u043E\u0440\u043C\u0430\u0446\u0438\u044F");
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
		txtLastName.setEditable(false);
		txtLastName.setBounds(189, 52, 116, 22);
		txtLastName.setText(Menu.lastNameString);
		contentPane.add(txtLastName);
		txtLastName.setColumns(10);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setBounds(317, 52, 116, 22);
		txtName.setText(Menu.nameString);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtSecondName = new JTextField();
		txtSecondName.setEditable(false);
		txtSecondName.setBounds(445, 52, 116, 22);
		txtSecondName.setText(Menu.secondNameString);
		contentPane.add(txtSecondName);
		txtSecondName.setColumns(10);
		
		txtPost = new JTextField();
		txtPost.setEditable(false);
		txtPost.setBounds(12, 116, 116, 22);
		txtPost.setText(Menu.postString);
		contentPane.add(txtPost);
		txtPost.setColumns(10);
		
		JLabel lblLogin = new JLabel("\u041B\u043E\u0433\u0438\u043D");
		lblLogin.setBounds(283, 92, 56, 16);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setEditable(false);
		txtLogin.setBounds(242, 116, 116, 22);
		txtLogin.setText(Menu.loginString);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u041F\u0430\u0440\u043E\u043B\u044C");
		lblNewLabel.setBounds(398, 92, 56, 16);
		contentPane.add(lblNewLabel);
		
		txtPassword = new JTextField();
		txtPassword.setEditable(false);
		txtPassword.setBounds(370, 116, 116, 22);
		txtPassword.setText(Menu.passwordString);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
	}
}
