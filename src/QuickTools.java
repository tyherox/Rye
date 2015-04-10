import javax.swing.*;
import java.awt.*;

/**
 * Created by 의현 on 2015-02-22.
 */
public class QuickTools extends JPanel {
    private static JLabel wc;
    private static JLabel pc;

    public QuickTools(){
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        setOpaque(false);

        wc = new JLabel("Word Count: 0",SwingConstants.CENTER);
        wc.setForeground(Color.WHITE);
        wc.setBorder(BorderFactory.createEmptyBorder());
        add(wc);

        pc = new JLabel("Page Count: ",SwingConstants.CENTER);
        pc.setForeground(Color.WHITE);
        pc.setBorder(BorderFactory.createEmptyBorder());
        add(pc);
    }

    public static void returnWc(int i) {
        wc.setText("Word Count: " + i);
    }
}
