/***************************
 * Purpose: View component of Model-View-Controller paradigm,
 * starting point for drawing to the screen
 *
 * Original Author: Zachary Johnson
 ***************************/

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import javax.swing.ImageIcon;
import java.util.Iterator;
import java.util.Map;
import java.awt.Color;
import java.awt.image.*;


class View extends JPanel
{
	Model model;
	BufferedImage currFrame, nextFrame;
	
	//Tracking variable to allow only one instance of painting next frame at a time
	private boolean drawingNextFrame = false;
	
	private static FullScreenMenu mainMenu;

	/***************************
	 * Constructor
	 ***************************/
	
	View(Model m) throws IOException
	{
		this.model = m;
		
		this.currFrame = new BufferedImage((int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1, BufferedImage.TYPE_INT_ARGB);
	}
	
	/***************************
	 * Constructor
	 ***************************/
	public void mouseClick(Vector2D position)
	{
		//Loop through screen elements to determine what was clicked
		//Currently only the ViewWindow
		
	}
	
	/***************************
	 * Primary painting function
	 ***************************/
	public void paintComponent(Graphics g)
	{
		//Draw the current frame if it is ready (may not be ready when game first starts)
		if (this.currFrame != null)
		{
			g.drawImage(this.currFrame, 0, 0, null);
		}
		
		//Draw next frame if not already doing so
		if (this.drawingNextFrame == false)
		{
			drawNextFrame();
			this.drawingNextFrame = false;
			//g.drawImage(this.currFrame, 0, 0, null);
		}
	}
	public void drawNextFrame()
	{
		this.drawingNextFrame = true;
		
		this.nextFrame = new BufferedImage((int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.x - 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D nfg = (Graphics2D) nextFrame.getGraphics();
		
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				if (model.mv.mainMenu != null)
				{
					if (model.mv.mainMenu.needsRedraw)
						nfg.drawImage(model.mv.mainMenu.drawRecurrsive(), 0, 0, null);
					else
						nfg.drawImage(model.mv.mainMenu.image, 0, 0, null);
				}
				else
				{
					nfg.setColor(Color.GRAY);
					nfg.fillRect(0, 0, (int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1);
				}
				break;
				
			case GAME:
				nfg.setColor(Color.BLACK);
				nfg.fillRect(0, 0, (int)Game.camera.windowDim.x, (int)Game.camera.windowDim.y);
				
				model.mv.playerShip.draw(nfg);
				
				for (Sprite curr : this.model.mv.gameSprites)
				{
					curr.draw(nfg);
				}
				//this.window.draw(nfg);
				
//				nfg.setColor(Color.WHITE);
//				nfg.drawString("Magnification: " + this.window.magnification / 10 + "." + this.window.magnification % 10 + "x", 15, 15);
//				nfg.drawString("sourceMin: (" + GameConstant.sx1 + ", " + GameConstant.sy1 + ")", 15, 35);
//				nfg.drawString("sourceMax: (" + GameConstant.sx2 + ", " + GameConstant.sy2 + ")", 15, 55);
//				nfg.drawString("FPS: " + GameDebugVars.frameRate, 15, 75);
//				nfg.drawString("PPS: " + GameDebugVars.paintsPerSecond, 15, 95);
				break;
			default:
				break;
		}
		
		this.currFrame = this.nextFrame;
	}
}






























