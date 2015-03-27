import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static java.lang.Character.isUpperCase;


public class CustomButton extends JButton {

    Image Icon;
    Image Inverse;
    Image Pressed;

    public  CustomButton(String image, String inverse, String pressed) {
        try {
            InputStream in = getClass().getResourceAsStream(image);
            Icon = ImageIO.read(in);
            ImageIcon Image = new ImageIcon(Icon);
            setIcon(Image);

            in = getClass().getResourceAsStream(inverse);
            Inverse = ImageIO.read(in);
            Image = new ImageIcon(Inverse);
            setRolloverIcon(Image);

            in = getClass().getResourceAsStream(pressed);
            Pressed = ImageIO.read(in);
            Image = new ImageIcon();
            setPressedIcon(Image);

            setHorizontalTextPosition(SwingConstants.CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decorate(){
        Icon icon = getIcon();
        Image resizedImage = Icon.getScaledInstance(getWidth(),getHeight(),java.awt.Image.SCALE_SMOOTH );
    }

}
