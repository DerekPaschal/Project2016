/***************************
 * Purpose: View component of Model-View-Controller paradigm,
 * starting point for drawing to the screen
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;


@SuppressWarnings("serial")
class View extends JPanel
{
	Model model;
	BufferedImage Frame; //This implementation is drawing synchronized (must always display entire frame at a time)
	
	//Tracking variable to allow only one instance of painting next frame at a time
	private static boolean drawingFrame = false;
	
	View(Model m) throws IOException
	{
		this.model = m;
		
		this.Frame = new BufferedImage((int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1, BufferedImage.TYPE_INT_ARGB);
	}
	
	/***************************
	 * Primary painting function
	 ***************************/
	public void paintComponent(Graphics g)
	{
		if (!View.drawingFrame)
		{
			View.drawingFrame = true;
			drawNextFrame();
			g.drawImage(this.Frame, 0, 0, null);
			View.drawingFrame = false;
		}
		
	}
	public void drawNextFrame()
	{
		this.Frame = new BufferedImage((int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.x - 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) Frame.getGraphics();
		
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				if (model.mv.mainMenu != null)
				{
					if (model.mv.mainMenu.needsRedraw)
						g2.drawImage(model.mv.mainMenu.drawRecurrsive(), 0, 0, null);
					else
						g2.drawImage(model.mv.mainMenu.image, 0, 0, null);
				}
				else
				{
					g2.setColor(Color.GRAY);
					g2.fillRect(0, 0, (int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1);
				}
				break;
				
			case GAME:
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, (int)Game.camera.windowDim.x, (int)Game.camera.windowDim.y);
				
				//Fully synchronized sprite list
				synchronized(this.model.mv.gameSprites)
				{
					for (int i = this.model.mv.gameSprites.size()-1; i >= 0; i--)
					{
						this.model.mv.gameSprites.get(i).draw(g2, Game.camera);
					}
				}
				
				break;
			default:
				break;
		}
	}
}






























