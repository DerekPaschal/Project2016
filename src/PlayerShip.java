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
import java.awt.image.BufferedImage;

public class PlayerShip extends SpaceShip
{
	public PlayerShip(Vector2D position) 
	{
		super(position,new Rotation(0),40,10,20,0.02);
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
						
				c2.dispose();
			}
			
			super.draw(g2);
		}
	}

}
