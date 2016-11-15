/***************************
 * Purpose: Map boundary object for use in maps
 * to contain game objects within the given area.
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapBoundary extends Sprite
{
	private Rectangle mapBounds;
	public Color boundaryColor;
	public int boundaryWidth;
	private double repulsiveForce;
	private ArrayList<PhysicsSprite> sprites;
	
	public MapBoundary(Rectangle size)
	{
		this.sprites = new ArrayList<PhysicsSprite>();
		this.mapBounds = new Rectangle(size);
		this.boundaryColor = Color.RED;
		this.boundaryWidth = 10;
		this.repulsiveForce = 0.1;
	}
	
	public MapBoundary(Rectangle size, ArrayList<PhysicsSprite> spriteList)
	{
		
		this.sprites = spriteList;
		this.mapBounds = new Rectangle(size);
		this.boundaryColor = Color.RED;
		this.boundaryWidth = 10;
		this.repulsiveForce = 0.1;
	}
	
	//Add sprite to list of sprites affected by force field
	public void addSprite(PhysicsSprite s)
	{
		synchronized (this.sprites)
		{
			this.sprites.add(s);
		}
	}
	
	//Remove sprite from list of sprites affected by force field
	public boolean removeSprite(PhysicsSprite s)
	{
		synchronized (this.sprites)
		{
			return this.sprites.remove(s);
		}
	}
	
	public void setBounds(Rectangle size)
	{
		synchronized (this.mapBounds)
		{
			this.mapBounds = size;
		}
	}
	public int getUpperBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.y;
		}
	}
	public int getLowerBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.y + (int) this.mapBounds.getHeight();
		}
	}
	public int getLeftBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.x;
		}
	}
	public int getRightBound()
	{
		synchronized (this.mapBounds)
		{
			return this.mapBounds.x + (int) this.mapBounds.getWidth();
		}
	}
	public int getWidth()
	{
		synchronized(this.mapBounds)
		{
			return this.mapBounds.width;
		}
	}
	public int getHeight()
	{
		synchronized(this.mapBounds)
		{
			return this.mapBounds.height;
		}
	}
	
	public void setForce(double force)
	{
		this.repulsiveForce = force;
	}
	public double getForce()
	{
		return this.repulsiveForce;
	}
	
	public void checkCollision()
	{
		PhysicsSprite impactor;
		for(PhysicsSprite pSprite : this.sprites)
		{
			if (pSprite instanceof PhysicsSprite)
			{
				impactor = (PhysicsSprite) pSprite;
				
				if (impactor.pos.x - impactor.size < this.getLeftBound())
					impactor.acc.x += this.repulsiveForce * Math.abs(impactor.pos.x - impactor.size - this.getLeftBound());
				
				else if (impactor.pos.x+impactor.size > this.getRightBound())
					impactor.acc.x += -this.repulsiveForce * Math.abs(impactor.pos.x + impactor.size - this.getRightBound());
				
				if (impactor.pos.y-impactor.size < this.getUpperBound())
					impactor.acc.y += this.repulsiveForce * Math.abs(impactor.pos.y - impactor.size - this.getUpperBound());
				
				else if (impactor.pos.y+impactor.size > this.getLowerBound())
					impactor.acc.y += -this.repulsiveForce * Math.abs(impactor.pos.y + impactor.size - this.getLowerBound());
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		this.currentImage = new BufferedImage((int)(this.mapBounds.width + (2*this.boundaryWidth)), (int)(this.mapBounds.height + (2*this.boundaryWidth)), BufferedImage.TYPE_INT_ARGB); 

		this.pos = new Vector2D(this.mapBounds.x + this.getWidth()/2, this.mapBounds.y + this.getHeight()/2);
		Graphics2D c2 = this.currentImage.createGraphics();
		
		c2.setColor(boundaryColor);
		c2.fillRect(0, 0, this.boundaryWidth, this.mapBounds.height+(2*this.boundaryWidth));
		c2.fillRect(0, 0, this.mapBounds.width+(2*this.boundaryWidth), this.boundaryWidth);
		c2.fillRect(this.mapBounds.width+this.boundaryWidth, 0, this.boundaryWidth, this.mapBounds.height+this.boundaryWidth);
		c2.fillRect(0, this.mapBounds.height+this.boundaryWidth, this.mapBounds.width+(2*this.boundaryWidth), this.boundaryWidth);
		
		c2.dispose();

		super.draw(g2);
	}
	
	/*public String getInfo()
	{
		String s = "Boundary Color: " + this.boundaryColor.toString() + "\n" +
				"Coordinates: (" + this.mapBounds.x + ", " + this.mapBounds.y + ") to (" + (this.mapBounds.x + this.mapBounds.width) + ", " + (this.mapBounds.y + this.mapBounds.height) + ")" +
				"Affected Sprites: " + this.sprites.size() + "\n";
		
		return s;
	}*/
}
