import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * Created by 의현 on 2015-02-24.
 */
public class Manager extends JPanel {

    boolean extended = false;
    JPanel menu;
    JPanel project;
    JPanel revisons;
    JPanel start;
    Color test = new Color(138, 130, 116);

    public Manager(final int width,final int X, int height) {

        final int area = width*9;

        setLayout(null);
        setBackground(Color.DARK_GRAY);

        menu  = new JPanel();
        menu.setLayout(null);
        menu.setBackground(test);
        add(menu);

        start = new JPanel();
        start.setBackground(Color.BLACK);
        start.setBounds(0, 0, width, height);
        add(start);
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (extended == false) {
                    System.out.println(getWidth());
                    setBounds(X - area, getY(), width + area, getHeight());
                    setMenu(width, area);
                    extended = true;
                } else if (extended == true) {
                    System.out.println(getWidth());
                    setBounds(X, getY(), width, getHeight());
                    clearMenu();
                    extended = false;
                }
            }

            public void filler() {

            }
        });

    }

    public void initializeMenu(int width, int height){
        JPanel panel = new JPanel();
        panel.setBackground(test);
        panel.setBounds(0,0,width*9,height/2);
        //panel.setOpaque(false);
        panel.setLayout(null);
        menu.add(panel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0,0,panel.getWidth(),panel.getHeight()*2);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        panel.add(scrollPane);

        JList list = new JList();
        list.setOpaque(false);
        list.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        DefaultListModel listModel = new DefaultListModel();
        list.setBorder(null);
        list.setFocusable(false);
        scrollPane.setViewportView(list);
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        listModel.addElement("This is a test file");
        listModel.addElement("Is this what it would look like?");
        listModel.addElement("Science fair test");
        listModel.addElement("History test guide");
        listModel.addElement("This is great stuff");
        listModel.addElement("I find this appalling");
        listModel.addElement("Well autocorrect is much overdue");
        listModel.addElement("Hopefully I can get it working soon");
        listModel.addElement("This is just the first ideal");
        listModel.addElement("We'll see how this goes");
        listModel.addElement("This is some good stuff");
        listModel.addElement("Last one here!");

        JLabel label = new JLabel("Project: History");
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Lucida Grande", Font.BOLD, 25));
        scrollPane.setColumnHeaderView(label);
    }

    public void setMenu(int x, int width){
        menu.setBounds(x,0,width,this.getHeight());
        menu.setBackground(Color.WHITE);
    }

    public void clearMenu(){
        menu.setBounds(0,0,0,0);
        menu.setBackground(Color.WHITE);
    }
}
