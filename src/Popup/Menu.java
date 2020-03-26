package Popup;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JLabel;



public class Menu extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JList<String> lRoom;
	private JList<String> lPerson;
	private static ArrayList<String> room = new ArrayList<String>();
	private static DefaultListModel<String> listModelRoom;
	private static DefaultListModel<String> listModelPerson;
	private String idRoom;


	public static void main(String[] args) throws ClassNotFoundException, SQLException{	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=TestBaza;integratedSecurity=true;";
				
			       try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {

			    	   String SQL = 
			    	   		"SELECT RoomNumber FROM RoomTable";
			    	   ResultSet rs = stmt.executeQuery(SQL);

			            // Iterate through the data in the result set and display it.
			            while (rs.next()) {	
			            	//room.add(rs.getString("RoomNumber"));
			            	listModelRoom.addElement(rs.getString("RoomNumber"));
			            	//System.out.println(rs.getString("RoomNumber"));
      
			            }



			        }
			        // Handle any errors that may have occurred.
			        catch (SQLException e) {
			            e.printStackTrace();
			        }
			       
			}
		});
	}


	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 685, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		listModelRoom = new DefaultListModel<String>();
		listModelPerson = new DefaultListModel<String>();
		
		
		lRoom = new JList<String>(listModelRoom);
		
		lRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lRoom.setPrototypeCellValue("Увеличенный");
        
        
        // Подключения слушателя
        lRoom.addListSelectionListener(new listSelectionListener());
        // Подключение слушателя мыши
        lRoom.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	
            if(e.getButton() == 1 && e.getClickCount() == 2)
            {
    			try {
    				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    			} catch (ClassNotFoundException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}  
    			String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=TestBaza;integratedSecurity=true;";
    			
    		       try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement(); Statement stmt2 = con.createStatement();) {

    		    	   String SQL = "SELECT ID, RoomNumber FROM RoomTable";
    		           ResultSet rs = stmt.executeQuery(SQL);

    		              
                	room.clear();
                	listModelPerson.clear();
    		    	   
                  
                        // Получение элемента
                    	String val = lRoom.getSelectedValue().toString(); // взять значение JList
                    	
    		            while (rs.next()) {
    		            	if(rs.getString("RoomNumber").equals(val)){
    		            		idRoom = rs.getString("ID");
    		            		
    		            	}
    		            	}
    		            
    		            String SQL2 = "SELECT * FROM Colloborators WHERE ID_ROOM = "+idRoom+"";
    		            ResultSet rs2 = stmt2.executeQuery(SQL2);

    		            while (rs2.next()) {
    		            	room.add(rs2.getString("Name"));
    		            	listModelPerson.addElement(rs2.getString("LastName")+ " " +rs2.getString("Name")+ " " + rs2.getString("SecondName") + " - " + rs2.getString("Post"));
    		            	//System.out.println(rs.getString("RoomNumber"));
    		            
      
    		            }
    		            
    		            System.out.println(room + " " + idRoom);
                    	       
                    
                }catch (SQLException e1) {
    	            e1.printStackTrace();
    	        }
            }
            	
		       
            }
        });
        
        
        
		//contentPane.add(lRoom);
		
		JScrollPane scroll = new JScrollPane(lRoom);
		scroll.setBounds(12, 60, 193, 180);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll);
		
		
		lPerson = new JList<String>(listModelPerson);
		JScrollPane scroll2 = new JScrollPane(lPerson);
		scroll2.setBounds(235, 60, 409, 216);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll2);
		
		JLabel lblNewLabel = new JLabel("\u041A\u0430\u0431\u0438\u043D\u0435\u0442\u044B");
		lblNewLabel.setBounds(12, 31, 89, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u0421\u043E\u0442\u0440\u0443\u0434\u043D\u0438\u043A\u0438");
		lblNewLabel_1.setBounds(235, 31, 117, 16);
		contentPane.add(lblNewLabel_1);
		//contentPane.add(lPerson);
		


	}
}

class listSelectionListener implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
        // Выделенная строка
        int selected = ((JList<?>)e.getSource()).
                                          getSelectedIndex();
        String val = ((JList<?>)e.getSource()).getSelectedValue().toString(); // взять значение JList
        System.out.println ("Выделенная строка : " + 
                                 String.valueOf(selected) + " - " + val);
       
    }
}
