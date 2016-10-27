import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlayerShip extends SpaceShip
{

	public PlayerShip(Vector2D position) 
	{
		super(position,0.0);
		
		try
		{
			this.size = 25; //Size of 'collision bubble' of ship
			this.currentImage = new BufferedImage((int)this.size*2, (int)this.size*2, BufferedImage.TYPE_INT_ARGB); //create blank current image
			Graphics2D c2 = this.currentImage.createGraphics(); //Create graphics object for current Image
			//c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON); //Set Anti Aliasing
			
			BufferedImage shipImage = GameFunction.loadBufferedImage("/Resources/ships/debugship_blue.png"); //Load Player Ship Image
			Vector2D shipDims = new Vector2D(shipImage.getWidth(), shipImage.getHeight()); //Get Maximum dimensions of ship image
			double shipScale = (this.size*2) / Math.max(shipDims.x, shipDims.y); //Set scale size that will make ship image fit into size
			
			c2.scale(shipScale, shipScale); //Apply scaler for ship image drawing
			c2.drawImage(shipImage, (int)((this.size - (shipDims.x * 0.5 * shipScale))/shipScale), (int)((this.size - (shipDims.y * 0.5 * shipScale))/shipScale), null); //Draw ship image onto current image
			c2.scale(1/shipScale, 1/shipScale); //Reset scale to default
			
			//Draw 'shield bubble'
			c2.setColor(Color.WHITE); 
			c2.drawOval(0, 0, (int)Math.round(this.size*2)-1, (int)Math.round(this.size*2)-1);	
			c2.drawOval(1, 1, (int)Math.round(this.size*2)-3, (int)Math.round(this.size*2)-3);
			
			c2.dispose();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	