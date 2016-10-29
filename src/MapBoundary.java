/***************************
 * Purpose: Map boundary object for use in maps
 * to contain game objects within the given area.
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapBoundary extends Sprite
{
	private Rectangle mapBounds;
	public Color boundaryColor;
	public int boundaryWidth;
	private double repulsiveForce;
	
	public MapBoundary()
	{
		this.mapBounds = new Rectangle(500, 500);
		this.boundaryColor = Color.RED;
		this.boundaryWidth = 10;
		this.repulsiveForce = 0.1;
	}
	
	public void setBounds(Rectangle size)
	{
		this.mapBounds = size;
	}
	public Rectangle getRect()
	{
		return this.mapBounds;
	}
	public int getUpperBound()
	{
		return this.mapBounds.y;
	}
	public int getLowerBound()
	{
		return this.mapBounds.y + (int) this.mapBounds.getHeight();
	}
	public int getLeftBound()
	{
		return this.mapBounds.x;
	}
	public int getRightBound()
	{
		return this.mapBounds.x + (int) this.mapBounds.getWidth();
	}
	
	public void setForce(double force)
	{
		this.repulsiveForce = force;
	}
	public double getForce()
	{
		return this.repulsiveForce;
	}
	
	public void checkCollision(PhysicsSprite impactor)
	{
		if (impactor.pos.x - impactor.size < this.getLeftBound())// && impactor.vel.x < 0)
		{
//			impactor.vel.x = -impactor.vel.x;
			impactor.vel.x += this.repulsiveForce * Math.abs(impactor.pos.x - impactor.size - this.getLeftBound());
		}
		else if (impactor.pos.x+impactor.size > this.getRightBound())// && impactor.vel.x > 0)
		{
//			impactor.vel.x = -impactor.vel.x;
			impactor.vel.x += -this.repulsiveForce * Math.abs(impactor.pos.x + impactor.size - this.getRightBound());
		}
		if (impactor.pos.y-impactor.size < this.getUpperBound())// && impactor.vel.y < 0)
		{
//			impactor.vel.y = -impactor.vel.y;
			impactor.vel.y += this.repulsiveForce * Math.abs(impactor.pos.y - impactor.size - this.getUpperBound());
		}
		else if (impactor.pos.y+impactor.size > this.getLowerBound())// && impactor.vel.y > 0)
		{
//			impactor.vel.y = -impactor.vel.y;
			impactor.vel.y += -this.repulsiveForce * Math.abs(impactor.pos.y + impactor.size - this.getLowerBound());
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		//double transX = this.pos.x - ViewCamera.pos.x + (ViewCamera.renderRes.x/2);
		//double transY = this.pos.y - ViewCamera.pos.y + (ViewCamera.renderRes.y/2);
		
		
		
		this.currentImage = new BufferedImage((int)(this.mapBounds.width + (2*this.boundaryWidth)), (int)(this.mapBounds.height + (2*this.boundaryWidth)), BufferedImage.TYPE_INT_ARGB); 
		

		this.pos = new Vector2D(mapBounds.width/2, mapBounds.height/2);
		Graphics2D c2 = this.currentImage.createGraphics();
		
		//c2.setColor(new Color(128,128,128,255));
		//c2.fillRect(0, 0, this.mapBounds.width + this.boundaryWidth, this.mapBounds.height + this.boundaryWidth);
		
		c2.setColor(boundaryColor);
		c2.fillRect(0, 0, this.boundaryWidth, this.mapBounds.height+(2*this.boundaryWidth));
		c2.fillRect(0, 0, this.mapBounds.width+(2*this.boundaryWidth), this.boundaryWidth);
		c2.fillRect(this.mapBounds.width+this.boundaryWidth, 0, this.boundaryWidth, this.mapBounds.height+this.boundaryWidth);
		c2.fillRect(0, this.mapBounds.height+this.boundaryWidth, this.mapBounds.width+(2*this.boundaryWidth), this.boundaryWidth);
		
		//c2.fillRect(0, 0, 1000, 1000);
		c2.dispose();
		
		//Draw map boundary rectangle
		/*Color oldColor = g2.getColor();
		g2.setColor(boundaryColor);
		for (int i = 0; i < boundaryWidth; i++)
		{
			g2.drawRect((int)transX - i, (int)transY - i, (int)mapBounds.getWidth() + 2 * i, (int)mapBounds.getHeight() + 2 * i);
		}
		g2.setColor(oldColor);*/
		
		
		//g2.drawImage(this.currentImage, this.mapBounds.x, this.mapBounds.y, null);
		super.draw(g2);
	}
}