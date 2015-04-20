import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.*;

public class ImagePanel extends JLayeredPane {
    String path;

    public ImagePanel(String name) {
        if(!name.equals("eqauls")){
            path = name;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        Graphics2D graphics = (Graphics2D) g.create();
        Image img = new ImageIcon(getClass().getResource(path)).getImage();
        graphics.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

}
