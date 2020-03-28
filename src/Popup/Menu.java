package Popup;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
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
import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;




public class Menu extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JList<String> lRoom;
	private JList<String> lPerson;
	private static ArrayList<String> room = new ArrayList<String>();
	private static DefaultListModel<String> listModelRoom;
	private static DefaultListModel<String> listModelPerson;
	private String idRoom, idPerson;
	private JPopupMenu pMenuRoom;
	private JPopupMenu pMenuPerson;
	private JTextField txtRoom;
	private String roomAdd;
	public static String roomNumber;


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
			            	listModelRoom.addElement(rs.getString("RoomNumber"));      
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
		lRoom.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Подключения слушателя
        lRoom.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
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
            }else if (SwingUtilities.isRightMouseButton(e)
                    && !lRoom.isSelectionEmpty()
                    && lRoom.locationToIndex(e.getPoint())
                       == lRoom.getSelectedIndex()) {
                        pMenuRoom.show(lRoom, e.getX(), e.getY());
                        System.out.print(lRoom.getSelectedIndex());
                        }
            	
		       
            }
        });
        
        
        
		//contentPane.add(lRoom);
        
        pMenuRoom = new JPopupMenu();
		JMenuItem addPerson = new JMenuItem("Добавить пользователя");
		JMenuItem deleteRoom = new JMenuItem("Удалить комнату");
		pMenuRoom.add(addPerson);
		pMenuRoom.add(deleteRoom);
		
		
		deleteRoom.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println ("EEEEEEEEEEEEEEEEEEEEEE");
                
                int result = JOptionPane.showOptionDialog(null, 
                        "Удаление комнаты приведет к удалению всех сотрудников в ней. Продолжить?", 
                        "Внимание", 
                        JOptionPane.OK_CANCEL_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        new String[]{"ДА", "НЕТ"},
                        "default");
// Окна подтверждения c 2-мя параметрами
                if (result == JOptionPane.YES_OPTION)
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
    		           
                        // Получение элемента
                     	String val = lRoom.getSelectedValue().toString(); // взять значение JList
                     	
     		            while (rs.next()) {
     		            	if(rs.getString("RoomNumber").equals(val)){
     		            		idRoom = rs.getString("ID");
     		            		break;
     		            		
     		            	}
     		            	}
     		            
    		            	String SQLS = "SELECT ID FROM Colloborators WHERE ID_ROOM = "+idRoom+"";
    		            	String SQLD = "DELETE FROM Login WHERE ID_L = "+ idPerson +"";
    		            	ResultSet rs3 = stmt.executeQuery(SQLS);
    		            	
    		            	while (rs3.next()){
    		            		idPerson = rs3.getString("ID");
    		            		stmt2.executeUpdate(SQLD);
    		            	}
    		            	
    		            	String SQL2 = "DELETE FROM Colloborators WHERE ID_ROOM = "+idRoom+"";
    		            	String SQL3 = "DELETE FROM RoomTable WHERE ID = "+idRoom+"";
    		            	stmt.executeUpdate(SQL2);
    		            	stmt.executeUpdate(SQL3);
    		            	listModelRoom.removeElementAt(lRoom.getSelectedIndex());
    		            	JOptionPane.showMessageDialog(null, "Удаление прошло успешно", "Выполнено", JOptionPane.INFORMATION_MESSAGE);

    		        }
    		        // Handle any errors that may have occurred.
    		        catch (SQLException e) {
    		            e.printStackTrace();
    		        }
    				
                	System.out.println ("yeeeeeeeeeeeeeees");
                }
                else if (result == JOptionPane.NO_OPTION)
                	System.out.println ("nooooooooooooooooooooooooo");
                
            }
        });
		
		
		addPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//открыть новое окно
				roomNumber = lRoom.getSelectedValue().toString();
				AddClientForm addForm = new AddClientForm();
				addForm.setVisible(true);
				
			}
		});
		
		
		JScrollPane scroll = new JScrollPane(lRoom);
		scroll.setBounds(12, 60, 193, 180);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll);
		
		
		lPerson = new JList<String>(listModelPerson);
		lPerson.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Подключения слушателя
        lPerson.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        // Подключение слушателя мыши
        lPerson.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
            	if (SwingUtilities.isRightMouseButton(me)
                    && !lPerson.isSelectionEmpty()
                    && lPerson.locationToIndex(me.getPoint())
                       == lPerson.getSelectedIndex()) {
            		pMenuPerson.show(lPerson, me.getX(), me.getY());
                        System.out.print(lPerson.getSelectedIndex());
                        }
            	
		       
            }
            
        });
        
        pMenuPerson = new JPopupMenu();
		JMenuItem info = new JMenuItem("Подробная информация");	
		JMenuItem deletePerson = new JMenuItem("Удалить пользователя");
		JMenuItem redactPerson = new JMenuItem("Изменить данные пользователя");
		pMenuPerson.add(info);
		pMenuPerson.add(deletePerson);
		pMenuPerson.add(redactPerson);
        
		
		info.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println ("VAUUUUU");
            }
        });
        
        
		
		JScrollPane scroll2 = new JScrollPane(lPerson);
		scroll2.setBounds(235, 60, 409, 216);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll2);
		
		JLabel labelRoom = new JLabel("\u041A\u0430\u0431\u0438\u043D\u0435\u0442\u044B");
		labelRoom.setBounds(12, 31, 89, 16);
		contentPane.add(labelRoom);
		
		
		
		JLabel labelPerson = new JLabel("\u0421\u043E\u0442\u0440\u0443\u0434\u043D\u0438\u043A\u0438");
		labelPerson.setBounds(235, 31, 117, 16);
		contentPane.add(labelPerson);
		
		txtRoom = new JTextField();
		txtRoom.setBounds(12, 269, 193, 22);
		contentPane.add(txtRoom);
		txtRoom.setColumns(10);
		
		JButton btnAddRoom = new JButton("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u043A\u0430\u0431\u0438\u043D\u0435\u0442");
		btnAddRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//
				roomAdd = txtRoom.getText();
				if(roomAdd.equals("")){
					JOptionPane.showMessageDialog(null, "Не введен номер кабинета", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					listModelRoom.clear();
					txtRoom.setText("");
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=TestBaza;integratedSecurity=true;";
				
			       try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			            String SQL = "INSERT INTO RoomTable VALUES('"+roomAdd+"')";
			            stmt.executeUpdate(SQL);
			        }
			        // Handle any errors that may have occurred.
			        catch (SQLException e) {
			            e.printStackTrace();
			        }
 
					
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
			}
		});
		btnAddRoom.setBounds(38, 293, 141, 25);
		contentPane.add(btnAddRoom);
		
		JLabel lblNewLabel = new JLabel("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043D\u043E\u043C\u0435\u0440 \u043A\u0430\u0431\u0438\u043D\u0435\u0442\u0430:");
		lblNewLabel.setBounds(12, 252, 193, 16);
		contentPane.add(lblNewLabel);
		//contentPane.add(lPerson);
		


	}
}



