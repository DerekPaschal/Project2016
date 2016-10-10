import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Derek Paschal
 *
 * The purpose of SpriteView is to create a display a window that is the size of the current
 * display and iterate through Sprite lists that draw to the display.
 */
@SuppressWarnings("serial")
public class SpriteView extends JPanel
{
	ViewCamera camera;
	private ArrayList<Sprite> ForeGroundSprites;
	private ArrayList<Sprite> BackGroundSprites;
	
	SpriteView(ViewCamera camera, ArrayList<Sprite> ForeGroundSprites, ArrayList<Sprite> BackGroundSprites)
	{
		this.camera = camera;
		this.ForeGroundSprites = ForeGroundSprites;
		this.BackGroundSprites = BackGroundSprites;
		this.setIgnoreRepaint(true);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		synchronized (this.BackGroundSprites)
		{
			for (Sprite sprite : this.BackGroundSprites)
			{
				sprite.draw(g2, this.camera);
			}
		}
		synchronized (this.ForeGroundSprites)
		{
			for (Sprite sprite : this.ForeGroundSprites)
			{
				sprite.draw(g2, this.camera);
			}
		}
	}
}






/*@SuppressWarnings("serial")
public class SpriteView extends JFrame
{
	private SpritePanel panel;
	
	*//**
	 * 
	 * @param WindowTitle The title of the window that will be created.
	 * @param ForeGroundSprites The Sprite list that will be allowed to draw to the foreground each frame.
	 * @param BackGroundSprites The Sprite list that will be allowed to draw to the background each frame.
	 *//*
	public SpriteView(String WindowTitle, ArrayList<Sprite> ForeGroundSprites, ArrayList<Sprite> BackGroundSprites, ViewCamera camera)
	{
		this.setTitle(WindowTitle);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		
		camera.Zoom = 1.0;
		camera.windowDim = new Vector2D(this.getWidth(), this.getHeight());
		camera.pos = new Vector2D(camera.windowDim.x/2.0, camera.windowDim.y/2.0);
		
		this.panel = new SpritePanel(camera, ForeGroundSprites, BackGroundSprites);
		this.getContentPane().add(this.panel);
		this.setIgnoreRepaint(true);
	}
	
	public void redraw()
	{
		this.repaint(0);
	}
}*/