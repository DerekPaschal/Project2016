/***************************
 * Purpose: Sprite class to handle essential
 * attributes of most ingame sprite objects.
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

abstract class Sprite
{
	public Vector2D pos;
	public BufferedImage currentImage;
	public Rotation rotation;
	public boolean visible;
	public boolean remove;
	
	public Sprite(Vector2D position)
	{
		this.pos = new Vector2D(position);
		this.rotation = new Rotation(0);
		this.visible = true;
		this.remove = false;
	}
	
	public Sprite()
	{
		this(new Vector2D(0,0));
	}
	
	//Draw the sprite with the position relative to the camera object
	public void draw(Graphics2D g2)
	{
		if (this.currentImage == null)
			return;
		
		AffineTransform at = new AffineTransform();
		//at.scale(camera.Zoom, camera.Zoom);
		double transX = this.pos.x - (this.currentImage.getWidth()/2) - ViewCamera.pos.x + (ViewCamera.renderRes.x*0.5);
		double transY = this.pos.y - (this.currentImage.getHeight()/2) - ViewCamera.pos.y + (ViewCamera.renderRes.y*0.5);
		
		
		if (!(transX <= ViewCamera.renderRes.x && transX+currentImage.getWidth()  >= 0 && 
				transY <= ViewCamera.renderRes.y && transY+currentImage.getHeight() >= 0))
		{
			return;
		}
		
		at.translate(transX, transY);
		
		double rotateX = currentImage.getWidth() / 2;
		double rotateY = currentImage.getHeight() / 2;
		at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		
		
		g2.drawImage(currentImage, at, null);
		
	}
	
	//Draw the sprite at a static location on the screen 
	//The position of the camera does not affect where the sprite appears on the screen
	//(Use this for GUI elements)
	public void drawStatic(Graphics2D g2, AffineTransform at)
	{
		if (this.currentImage == null)
			return;
		
		if (at == null)
			at = new AffineTransform();
		
		g2.drawImage(currentImage, at, null);
	}
	
	//Draw static sprite with no transformation
	public void drawStatic(Graphics2D g2)
	{
		drawStatic(g2, null);
	}
}
