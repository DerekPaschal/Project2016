/***************************
 * Purpose: BackgroundSprite drawn behind
 * all regular sprites on a GameMap, does
 * not interact with anything.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BackgroundSprite extends Sprite
{
	String imgPath;
	
	public BackgroundSprite(Vector2D pos, String imagePath)
	{
		super(pos);
		this.imgPath = imagePath;
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		synchronized(this.imageLock)
		{
			if (this.needsRedraw || this.currentImage == null)
			{
				this.currentImage = new BufferedImage((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1, 
						BufferedImage.TYPE_INT_ARGB);
				
				BufferedImage sourceImage = ResourceLoader.getBufferedImage(imgPath);
				Graphics2D c2 = this.currentImage.createGraphics();
				
				//Draw source image scaled to size of background sprite
				c2.drawImage(sourceImage, 
						0, 0, this.currentImage.getWidth()-1, this.currentImage.getHeight()-1, 
						0, 0, sourceImage.getWidth()-1, sourceImage.getHeight()-1, null);
				
				this.needsRedraw = false;
			}
			
			super.drawStatic(g2);
		}
	}
}
