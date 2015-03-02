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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(extended==false) {
                    setBounds(getX()-350,getY(),getWidth()+350,getHeight());
                    extended=true;
                }
                else if(extended==true) {
                    setBounds(getX()+350,getY(),getWidth()-350,getHeight());
                    extended=false;
                }
            }
        });
    }
}
