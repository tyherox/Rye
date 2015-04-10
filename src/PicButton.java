import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static java.lang.Character.isUpperCase;


public class PicButton extends JButton {



    public PicButton(String image, String inverse, String pressed, Dimension d) {
/*
        try {
            Debug.Log("loaded image: " + image);
            InputStream in = getClass().getResourceAsStream(image);
            Image Icon = ImageIO.read(in);
            Image rImage = Icon.getScaledInstance( d.width, d.height,  java.awt.Image.SCALE_SMOOTH );
            ImageIcon Image = new ImageIcon(rImage);
            setIcon(Image);

            in = getClass().getResourceAsStream(inverse);
            Image Inverse = ImageIO.read(in);
            rImage = Inverse.getScaledInstance( d.width, d.height,  java.awt.Image.SCALE_SMOOTH );
            Image = new ImageIcon(rImage);
            setRolloverIcon(Image);

            in = getClass().getResourceAsStream(pressed);
            Image Pressed = ImageIO.read(in);
            rImage = Pressed.getScaledInstance( d.width, d.height,  java.awt.Image.SCALE_SMOOTH );
            Image = new ImageIcon(rImage);
            setPressedIcon(Image);

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalTextPosition(SwingConstants.CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

}
