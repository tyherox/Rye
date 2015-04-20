import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

public class CustomScroll extends JPanel {

    private ImagePanel thumb;
    private int sy;
    private int sx;
    private int py;
    private int px;
    private int y;

   	public CustomScroll(int X, final int Y, final Dimension screenSize) throws IOException {
        setLayout(null);
        thumb = new ImagePanel("/Images/thumb.png");
        Color c= new Color(141, 141, 125);
        thumb.setBackground(c);
        setOpaque(false);

        thumb.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                y = e.getY();
            }
            public void filler() {

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
                            WPad.scroll("up", (double) (evt.getY() - y) / (double) (Y - 10));
                        }
                        else
                        {
                            thumb.setLocation(px, evt.getY() - y);
                            WPad.scroll("down", (double) (evt.getY() - y) / (double) (Y - 10));
                        }
                    }
                    else
                    {
                        thumb.setLocation(px, 0);
                        WPad.scroll("highest", 0);
                    }
                }
                else
                {
                    thumb.setLocation(px, screenSize.height - thumb.getHeight());
                    WPad.scroll("lowest", 1);
                }
            }

            public void filler()
            {

            }
        });
        add(thumb);

        sy= Y;
		py = getY();

		sx= X/2;
		px = sx/2;

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