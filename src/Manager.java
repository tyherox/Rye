import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by 의현 on 2015-02-24.
 */
public class Manager extends JPanel {

    int width,xP;
    static Manager holder;
    boolean extended = false;
    static JPanel menu;
    ImagePanel start;
    JPanel project;
    JPanel History;
    JTextField titleProject;
    JTextField titleHistory;

    Color primary = new Color(1, 1, 1, 167);
    Color secondary = new Color(255, 121, 109, 84);

    Color SelectedColor = new Color(128, 50, 54, 147);
    Color NormalColor = new Color(119, 128, 128, 147);
    Color GridColor = new Color(251, 255, 226, 74);

    public Manager() {
        holder = this;
        setLayout(null);
        //setBackground(Color.WHITE);
        setOpaque(false);

        menu  = new JPanel();
        menu.setLayout(null);
        menu.setOpaque(false);
        menu.setFocusable(false);
        add(menu);

        start = new ImagePanel("/Images/dragger.png");
        start.setFocusable(false);
        add(start);

    }

    public void setMenu(int x, int width){
        menu.setBounds(x, 0, width, 1200);
        revalidate();
        repaint();
    }

    public static void clearMenu(){
        Manager killer = holder;
        killer.killMenu();
    }

    public void killMenu(){
        setBounds(xP, 0, width, getHeight());
        menu.setBounds(0, 0, 0, 0);
        extended = false;
    }

    public void initializeMenu(){

        width = getWidth();
        xP = getX();
        final int w = getWidth();
        final int h = getHeight();
        final int X = getX();
        final int Y = getY();

        final int area = (int)(w*11);

        start.setBounds(0, 0, w, h);
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (extended == false) {
                    setBounds(X - area, getY(), w + area, getHeight());
                    setMenu(w, area);
                    JToolBox.killAnimation();
                    extended = true;
                } else if (extended == true) {
                    clearMenu();
                }
            }

            public void filler() {

            }
        });

        project = new JPanel();
        project.setBounds(0, 0, (int) (w * 11.5), h / 2);
        project.setBackground(primary);
        project.setLayout(null);
        menu.add(project);
        Project();

        History = new JPanel();
        History.setBounds(0, h / 2, (int) (w * 11.5), h / 2);
        History.setBackground(primary);
        History.setLayout(null);
        menu.add(History);
        History();
    }

    public void Project() {

        JScrollPane ProjectHolder = new JScrollPane();
        ProjectHolder.setBounds(0, getHeight() / 10, project.getWidth(), project.getHeight() / 10 * 9);
        ProjectHolder.setBorder(null);
        ProjectHolder.setOpaque(false);
        ProjectHolder.getViewport().setOpaque(false);
        project.add(ProjectHolder);

        DefaultTableModel ProjectModel = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        ProjectModel.setRowCount(10);
        ProjectModel.addColumn("Name");
        ProjectModel.addColumn("Date Modified");
        ProjectModel.addColumn("Order");
        JTable ProjectTable = new JTable(ProjectModel);
        ProjectTable.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        ProjectTable.setBorder(null);
        ProjectTable.setFocusable(false);
        ProjectTable.setOpaque(false);
        ProjectTable.setShowGrid(false);
        ProjectTable.setSelectionModel(new RowSelect(ProjectTable));
        ProjectTable.setDefaultRenderer(ProjectTable.getColumnClass(0), new CustomTableRenderer());
        ProjectTable.getTableHeader().setBackground(primary);
        ProjectTable.getTableHeader().setForeground(Color.WHITE);
        ProjectHolder.setViewportView(ProjectTable);

        JPanel titleHolder = new JPanel();
        titleHolder.setBounds(0,0,project.getWidth(),getHeight()/10);
        titleHolder.setBackground(Color.BLACK);
        titleHolder.setLayout(new BorderLayout());
        titleHolder.setOpaque(false);
        project.add(titleHolder);

        titleProject = new JTextField("Project Name");
        titleProject.setEnabled(false);
        titleProject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.out.println("clicked twice");
                    titleProject.setEnabled(true);
                    titleProject.setVisible(true);
                    titleProject.setFocusable(true);
                }
            }

            public void filler() {

            }
        });
        titleProject.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    titleProject.setEnabled(false);
                    titleProject.setFocusable(false);
                }
            }

            public void filler() {

            }
        });
        titleProject.setDisabledTextColor(Color.WHITE);
        titleProject.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        titleProject.setForeground(Color.WHITE);
        titleProject.setCaretColor(Color.WHITE);
        titleProject.setBackground(secondary);
        titleProject.setBorder(new EmptyBorder(0, 10, 0, 0));
        titleProject.setDragEnabled(false);
        titleHolder.add(titleProject, BorderLayout.CENTER);

    }

    public void History() {
        JScrollPane HistoryHolder = new JScrollPane();
        HistoryHolder.setBounds(0, getHeight() / 10, History.getWidth(), History.getHeight() / 10 * 9);
        HistoryHolder.setBorder(null);
        HistoryHolder.setOpaque(false);
        HistoryHolder.getViewport().setOpaque(false);
        History.add(HistoryHolder);

        DefaultTableModel HistoryModel = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        HistoryModel.setRowCount(10);
        HistoryModel.addColumn("Name");
        HistoryModel.addColumn("Date Modified");
        HistoryModel.addColumn("Order");
        JTable HistoryTable = new JTable(HistoryModel);
        HistoryTable.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        HistoryTable.setBorder(null);
        HistoryTable.setFocusable(false);
        HistoryTable.setOpaque(false);
        HistoryTable.setShowGrid(false);
        HistoryTable.setSelectionModel(new RowSelect(HistoryTable));
        HistoryTable.setDefaultRenderer(HistoryTable.getColumnClass(0), new CustomTableRenderer());
        HistoryTable.getTableHeader().setBackground(primary);
        HistoryTable.getTableHeader().setForeground(Color.WHITE);
        HistoryHolder.setViewportView(HistoryTable);

        JPanel titleHolder = new JPanel();
        titleHolder.setBounds(0, 0, this.History.getWidth(), getHeight() / 10);
        titleHolder.setBackground(Color.BLACK);
        titleHolder.setLayout(new BorderLayout());
        titleHolder.setOpaque(false);
        this.History.add(titleHolder);

        titleHistory = new JTextField("Revisions");
        titleHistory.setEnabled(false);
        titleHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.out.println("clicked twice");
                    titleHistory.setEnabled(true);
                    titleHistory.setVisible(true);
                    titleHistory.setFocusable(true);
                }
            }

            public void filler() {

            }
        });
        titleHistory.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    titleHistory.setEnabled(false);
                    titleHistory.setFocusable(false);
                }
            }

            public void filler() {

            }
        });
        titleHistory.setDisabledTextColor(Color.WHITE);
        titleHistory.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        titleHistory.setForeground(Color.WHITE);
        titleHistory.setCaretColor(Color.WHITE);
        titleHistory.setBackground(secondary);
        titleHistory.setBorder(new EmptyBorder(0, 10, 0, 0));
        titleHistory.setDragEnabled(false);
        titleHolder.add(titleHistory, BorderLayout.CENTER);
    }

    public class RowSelect extends DefaultListSelectionModel {

        public RowSelect(final JTable test) {
            addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    int r = test.getSelectedRow();
                    test.setRowSelectionInterval(r, r);
                }

            });
        }

        @Override
        public void clearSelection() {
        }

        @Override
        public void removeSelectionInterval(int index0, int index1) {
        }

    }

    class CustomTableRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setBorder(null);
            table.setGridColor(GridColor);

            if(selected){
                setBackground(SelectedColor);
                setOpaque(true);
            }
            else{
                setOpaque(false);
            }
            holder.revalidate();
            holder.repaint();
            return this;
        }
    }

}
