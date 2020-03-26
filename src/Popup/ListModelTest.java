package Popup;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListModelTest extends JFrame
{
    // ������ ������
    private final String[]   dataList = { "�������", "�������", "����"};
    private final String[][] dataText = {{"�����", "����", "����"},
                                         {"���"  , "����", "����"},
                                         {"�����", "���������", 
                                          "�����������"}};
    private JTextArea content;

    public ListModelTest()
    {
        super("��������� ������� ������");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // �������� ������
        JPanel contents = new JPanel();
        final JList<String> list = new JList<String>(dataList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setPrototypeCellValue("�����������");
        // �������� ���������� ����
        content = new JTextArea(5, 20);
        // ����������� ���������
        list.addListSelectionListener(new listSelectionListener());
        // ����������� ��������� ����
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if ( e.getClickCount() == 2 ) {
                    // ��������� ��������
                    int selected = list.locationToIndex(e.getPoint());
                    int i = 0;
                    String txt = "";
                    while (i < dataText[selected].length)
                        txt += dataText[selected][i++] + "\n";
                    content.setText (txt);
                }
            }
        });
        
        // ���������� ����������� � ����������
        contents.add(new JLabel("������ ��������"));
        contents.add(new JScrollPane(list));
        contents.add(new JLabel("���������� ��������"));
        contents.add(new JScrollPane(content));
        // ����� ����
        setContentPane(contents);
        setSize(600, 200);
        setVisible(true);
    }
    /*
     * ��������� ������ 
     */
    class listSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            // ���������� ������
            int selected = ((JList<?>)e.getSource()).
                                              getSelectedIndex();
            System.out.println ("���������� ������ : " + 
                                     String.valueOf(selected));
        }
    }
    public static void main(String[] args) {
        new ListModelTest();
    }
}