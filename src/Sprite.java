/***************************
 * Purpose: Sprite class to handle essential
 * attributes of most ingame sprite objects.
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
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
	
	public void draw(Graphics2D g2)
	{
		AffineTransform at = new AffineTransform();
		//at.scale(camera.Zoom, camera.Zoom);
		double transX = this.pos.x - (this.currentImage.getWidth()/2) - ViewCamera.pos.x + (ViewCamera.renderRes.x/2);
		double transY = this.pos.y - (this.currentImage.getHeight()/2) - ViewCamera.pos.y + (ViewCamera.renderRes.y/2);
		
		
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
}
