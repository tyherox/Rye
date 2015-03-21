import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by 의현 on 2015-02-24.
 */
public class Manager extends JPanel {

    boolean extended = false;

    public Manager(){
        setBackground(Color.DARK_GRAY);
        System.out.println(getWidth());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (extended == false) {
                    System.out.println(getWidth());
                    setBounds(getX() - getX()/5, getY(), getWidth() + getX()/5, getHeight());
                    extended = true;
                } else if (extended == true) {
                    System.out.println(getWidth());
                    setBounds(getX() + getX()/5, getY(), getWidth() - getX()/5, getHeight());
                    extended = false;
                }
            }
        });
    }
}
