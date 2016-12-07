/***************************
 * Purpose: PlayerShip class extending SpaceShip,
 * containing behaviors specific to the player's
 * ship and not shared between all spaceships.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class PlayerShip extends SpaceShip
{
	private int currentLevel, baseUpgradeCost, maxUpgradeLevel;
	public boolean upgradesUpdated = false;
	
	public PlayerShip(Vector2D position) 
	{
		super(position,new Rotation(0),40,10,10,0.05);
		this.currentLevel = 1;
		this.baseUpgradeCost = 1000;
		this.maxUpgradeLevel = 5;
	}
	
	public int getNextUpgradeCost()
	{
		return (int) (this.baseUpgradeCost + (500 * (this.currentLevel-1)));
	}
	
	public int getUpgradeLevel()
	{
		return this.currentLevel;
	}
	
	public void upgradeShip()
	{
		if (this.energy > this.getNextUpgradeCost() && this.currentLevel < this.maxUpgradeLevel)
		{
			this.energy -= this.getNextUpgradeCost();
			this.healthMax *= 1.1;
			this.health = this.healthMax;
			this.shieldMax *= 1.2;
			this.shieldRegen *= 1.2;
			this.bulletVel *= 1.1;
			this.bulletDamage *= 1.5;
			this.bulletSpread *= 2;
			this.thrustPower *= 1.1;
			this.bulletCooldown -= 1.0;
			this.currentLevel++;
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
				c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON); //Set Anti Aliasing
				
				BufferedImage shipImage = ResourceLoader.getBufferedImage("ships/13B.png"); //Load Player Ship Image
				
				Vector2D shipDims = new Vector2D(shipImage.getWidth(), shipImage.getHeight()); //Get Maximum dimensions of ship image
				double shipScale = (this.size*2*0.8) / Math.max(shipDims.x, shipDims.y); //Set scale size that will make ship image fit into size
				
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
						
				/*c2.scale(1.0/shipScale, 1.0/shipScale);
				c2.setColor(Color.white);
				c2.drawRect(0, 0, this.currentImage.getWidth()-1, this.currentImage.getHeight()-1);
				c2.fillOval((int)this.size - 1, (int)this.size - 1, 2, 2);*/
				
				c2.dispose();
			}
			
			super.draw(g2);
		}
	}

}
