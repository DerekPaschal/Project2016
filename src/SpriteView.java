import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Derek Paschal
 *
 * The purpose of SpriteView is to create a display a window that is the size of the current
 * display and iterate through a Sprite list that draws to the display.
 */
@SuppressWarnings("serial")
public class SpriteView extends JFrame
{
	private SpritePanel panel;
	public LinkedList<Sprite> SpriteList;
	
	/**
	 * 
	 * @param WindowTitle The title of the window that will be created.
	 * @param newSpriteList The Sprite list that will be allowed to draw to the disply each frame.
	 */
	public SpriteView(String WindowTitle, LinkedList<Sprite> newSpriteList)
	{
		this.setTitle(WindowTitle);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		
		
		this.panel = new SpritePanel(new Vector2D(this.getWidth(), this.getHeight()), this.SpriteList);
		this.getContentPane().add(this.panel);
		this.setIgnoreRepaint(true);
	}
	
	public void redraw()
	{
		this.repaint(0);
	}
	
	public void redraw(Vector2D cameraPosition, double scale)
	{
		this.panel.scale = scale;
		this.panel.cameraPosition = cameraPosition;
		this.repaint(0);
	}
}



@SuppressWarnings("serial")
class SpritePanel extends JPanel
{
	Vector2D window;
	Vector2D cameraPosition = new Vector2D();
	double scale = 1.0;
	private LinkedList<Sprite> SpriteList;
	
	SpritePanel(Vector2D window, LinkedList<Sprite> newSpriteList)
	{
		this.window = window;
		this.SpriteList = newSpriteList;
		this.setIgnoreRepaint(true);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		synchronized (this.SpriteList)
		{
			for (Sprite sprite : this.SpriteList)
			{
				sprite.draw(g2, this.cameraPosition, this.scale);
			}
		}
	}
}
