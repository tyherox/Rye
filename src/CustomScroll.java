import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

public class CustomScroll extends JPanel {

    private JPanel thumb;
    private int sy;
    private int sx;
    private int py;
    private int px;
    private int y;

   	public CustomScroll(int X, final int Y, final Dimension screenSize) throws IOException {
    	setLayout(null);
        thumb = new JPanel();
        Color c= Color.BLACK;
        thumb.setBackground(c);
        thumb.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                y = e.getY();
                System.out.println("click value of y: " + y);
            }
        });
        thumb.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent evt) {
                int initial = evt.getY();
                evt.translatePoint(evt.getComponent().getLocation().x, evt.getComponent().getLocation().y);
                if((evt.getY()+thumb.getHeight()-y)<=screenSize.height)
                {
                    if((evt.getY()-y)>=0)
                    {
                        if(y-initial>0)
                        {
                            thumb.setLocation(px, evt.getY() - y);
                            wPad.scroll("up", (double) (evt.getY() - y) / (double) (Y - 10));
                        }
                        else
                        {
                            thumb.setLocation(px, evt.getY() - y);
                            wPad.scroll("down", (double) (evt.getY() - y) / (double) (Y - 10));
                        }
                    }
                    else
                    {
                        thumb.setLocation(px, 0);
                        wPad.scroll("highest", 0);
                    }
                }
                else
                {
                    thumb.setLocation(px, screenSize.height - thumb.getHeight());
                    wPad.scroll("lowest", 1);
                }
            }
        });
        add(thumb);

        sy= Y;
		py = getY();

		sx= X;
		px = getX();

        thumb.setBounds(px, py, sx, sy);
    }

	public void updateGraphic(double pChange, double sChange) {
		py = (int) (pChange*sy);
		int ny = (int) (sChange*sy);
		if(sChange == Double.POSITIVE_INFINITY)
		{
			thumb.setBounds(px, py, 0, 0);
		}
		else
		{
			thumb.setBounds(px, py, sx, ny);
		}
	}

}