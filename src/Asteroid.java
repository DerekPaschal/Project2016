/***************************
 * Purpose: Asteroid class containing the
 * functionality for an asteroid object on the
 * GameMap with physics interactions.
 *
 * Contributors:
 * - Derek Paschal
 * - Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Asteroid extends PhysicsSprite
{
	public Color color;
	int type;
	
	public Asteroid(Vector2D position, Rotation rotation, double size, double restitution)
	{
		
		super(position, rotation, Math.round(size),restitution);
		this.vel = new Vector2D();
		int color_value = (int)(128+(Math.random()*64));
		this.color = new Color(color_value,color_value,color_value);
		type = (int)Math.floor(Math.random()*2);
	}
	
	public void setColor(Color c)
	{
		this.color = c;
	}
	
	@Override
	public void updateAcc(ArrayList<PhysicsSprite> physicsSprites)
	{				
		super.CollisionDetect(physicsSprites);
	}
	
	@Override
	public void updateVelPos()
	{
		super.updateVelPos();
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				this.currentImage = new BufferedImage((int)Math.round(this.size*2), (int)Math.round(this.size*2), 
						BufferedImage.TYPE_INT_ARGB);
				
				BufferedImage tempImage;
				
				switch(type)
				{
					case 0:
						tempImage = ResourceLoader.getBufferedImage("asteroids/asteroid1.png"); //Load Asteroid Image
						break;
					case 1:
						tempImage = ResourceLoader.getBufferedImage("asteroids/asteroid2.png"); //Load Asteroid Image
						break;
					default:
						tempImage = ResourceLoader.getBufferedImage("asteroids/asteroid1.png"); //Load Asteroid Image
						break;
				}
				
				Graphics2D c2 = this.currentImage.createGraphics();
				c2.drawImage(tempImage, 
						0, 0, this.currentImage.getWidth()-1, this.currentImage.getHeight()-1, 
						0, 0, tempImage.getWidth()-1, tempImage.getHeight()-1, null);
			}
			
			super.draw(g2);
		}
	}

	@Override
	public void collisionAlert(PhysicsSprite impactor) 
	{
		this.rot_vel = Math.random()*5-2.5;
//		this.health--;
//		
//		if (this.health < 0)
//			this.remove = true;
	}
}
