/***************************
 * Purpose: Sprite class to handle essential
 * attributes of most ingame sprite objects.
 * 
 * Inherits from GameEntity
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
	
	public void draw(Graphics2D g2, ViewCamera camera)
	{
		AffineTransform at = new AffineTransform();
		at.scale(camera.Zoom, camera.Zoom);
		double transX = this.pos.x - (this.currentImage.getWidth()/2) - camera.pos.x + (camera.windowDim.x/2);
		double transY = this.pos.y - (this.currentImage.getHeight()/2) - camera.pos.y + (camera.windowDim.y/2);
		
		
		if (!(transX <= camera.windowDim.x/camera.Zoom && transX+currentImage.getWidth()  >= 0 && 
				transY <= camera.windowDim.y/camera.Zoom && transY+currentImage.getHeight() >= 0))
		{
			return;
		}
		
		at.translate(transX, transY);
		
		double rotateX = currentImage.getWidth() / 2;
		double rotateY = currentImage.getHeight() / 2;
		at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		
		
		g2.drawImage(currentImage, at, null);
		
		/*Color temp = g2.getColor();
		g2.setColor(Color.white);
		g2.fillOval((int)(this.pos.x - camera.pos.x), (int)(this.pos.y - camera.pos.y),4 ,4 );
		g2.setColor(temp);*/
	}
	
	/*public void draw(Graphics2D g2)
	{
		AffineTransform at = new AffineTransform();
		at.scale(1.0, 1.0);
		double a = this.pos.x, b = currentImage.getWidth(), c = at.getScaleX();
		double transX = (this.pos.x - ((double)currentImage.getWidth() * at.getScaleX() * 1.0))/at.getScaleX();
		double transY = (this.pos.y - ((double)currentImage.getHeight() * at.getScaleY() * 1.0))/at.getScaleY();
		at.translate(transX, transY);
		
		double rotateX = currentImage.getWidth() / 2;
		double rotateY = currentImage.getHeight() / 2;
		at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		
		
		g2.drawImage(currentImage, at, null);
	}*/
}
