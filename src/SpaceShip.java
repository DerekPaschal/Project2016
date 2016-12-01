/***************************
 * Purpose: SpaceShip class containing common
 * functionality for spaceship behavior.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class SpaceShip extends PhysicsSprite
{
	public double healthMax = 0, shield = 0, shieldMax = 0, energy = 0;
	public boolean left, right, forward, backward, firing;
	private double turnRate = 2, thrustPower = 0.2;
	
	public SpaceShip(Vector2D position, Rotation rotation, double size, double health, double shield)
	{
		super(position,rotation,size, 1.0, health);
		
		this.healthMax = health;
		this.shield = shield;
		this.shieldMax = shield;
		this.energy = 1000;
		
		left = false;
		right = false;
		forward = false;
		backward = false;
		firing = false;
	}
	
	@Override
	public void updateAcc(int index)
	{		
		super.CollisionDetect(index);
		
		synchronized (this.acc)
		{
			//Add Acceleration from Thrusters
			if (forward)
				this.acc = this.acc.add(new Vector2D(Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower, Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
			if (backward)
				this.acc = this.acc.add(new Vector2D(-Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower,-Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
			
			//Apply Friction Force (In Space?)
			this.acc = this.acc.subtract(this.vel.multiply(new Vector2D(0.02,0.02)));
			
			//Add Rotation
			if (left)
				//this.rotation.addAmount(-this.turnRate);
				this.rot_acc =+ -this.turnRate;
			if (right)
				//this.rotation.addAmount(this.turnRate);
				this.rot_acc =+ this.turnRate;
			
			//Apply Rotation Friction (In Space?)
			this.rot_acc = this.rot_acc - (this.rot_vel * 0.2);
		}
	}
	
	@Override
	public void collisionAlert(PhysicsSprite impactor, double impact)
	{
		this.shield -= impact;
		
		if (this.shield < 0)
		{
			this.health += this.shield;
			this.shield = 0.0;
		}
		
		if (this.health < 0)
		{
			this.remove = true;
		}
	}

	@Override
	public void draw(Graphics2D g2)
	{
		synchronized (this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				this.currentImage = new BufferedImage((int)this.size*2, (int)this.size*2, BufferedImage.TYPE_INT_ARGB); //create blank current image
				Graphics2D c2 = this.currentImage.createGraphics(); //Create graphics object for current Image
				//c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON); //Set Anti Aliasing
				
				BufferedImage shipImage = ResourceLoader.getBufferedImage("ships/13B.png"); //Load Player Ship Image
				
				Vector2D shipDims = new Vector2D(shipImage.getWidth(), shipImage.getHeight()); //Get Maximum dimensions of ship image
				double shipScale = (this.size*2) / Math.max(shipDims.x, shipDims.y); //Set scale size that will make ship image fit into size
								
				//Draw 'shield bubble'
				/*for (float alpha = 0, i = 0; (alpha < 1.0) && (this.size - i > 1); alpha+= 0.035, i++)
				{
					c2.setColor(new Color((float)0, (float)0.7, (float)0.8, (float)alpha));
					c2.drawOval((int) i, (int) i, (int)(Math.round(this.size*2)-1-i*2), (int)(Math.round(this.size*2)-1-i*2));	
				}*/
				
				float shieldRatio = (float)(this.shield / this.shieldMax);
				int i = 0;
				float alpha = (float)(0.1*shieldRatio);
		
				for (; (alpha <= 1.0 && alpha >= 0.0) && (i < 4); alpha += (0.1*shieldRatio) ,i++)
				{
					c2.setColor(new Color((float)0.0,(float)0.7,(float)0.8,alpha));
					c2.drawOval(i, i, (int)(Math.round(this.size*2)-1-i*2), (int)(Math.round(this.size*2)-1-i*2));
				}
				
				for (; (alpha >= 0.0 && alpha <= 1.0) && (this.size - i > 1); alpha-= (0.04*shieldRatio), i++)
				{
					c2.setColor(new Color((float)0.0,(float)0.7,(float)0.8,alpha));
					c2.drawOval(i, i, (int)(Math.round(this.size*2)-1-i*2), (int)(Math.round(this.size*2)-1-i*2));
				}
				
				c2.scale(shipScale, shipScale); //Apply scaler for ship image drawing
				c2.drawImage(shipImage, (int)((this.size - (shipDims.x * 0.5 * shipScale))/shipScale), (int)((this.size - (shipDims.y * 0.5 * shipScale))/shipScale), null); //Draw ship image onto current image
				
				c2.dispose();
			}
			
			super.draw(g2);
		}
	}
}
