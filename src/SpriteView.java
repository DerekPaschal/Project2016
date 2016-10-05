import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SpriteView extends JFrame
{
	private SpritePanel panel;
	public LinkedList<Sprite> SpriteList;
	
	public SpriteView(String WindowTitle, LinkedList<Sprite> newSpriteList)
	{
		this.setTitle(WindowTitle);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		
		this.panel = new SpritePanel(this.getWidth(), this.getHeight(), this.SpriteList);
		this.getContentPane().add(this.panel);
		this.setIgnoreRepaint(true);
	}
	
	public void redraw()
	{
		this.repaint(0);
	}
}



@SuppressWarnings("serial")
class SpritePanel extends JPanel
{
	private int windowX, windowY;
	private LinkedList<Sprite> SpriteList;
	
	SpritePanel(int windowX, int windowY, LinkedList<Sprite> newSpriteList)
	{
		this.windowX = Math.max(windowX,0);
		this.windowY = Math.max(windowY,0);
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
				sprite.draw(g2);
			}
		}
	}
}
