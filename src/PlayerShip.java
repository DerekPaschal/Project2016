import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

public class PlayerShip extends SpaceShip
{

	public PlayerShip(Vector2D position) 
	{
		super(position,0.0);
		
		try
		{
			
			this.currentImage = GameFunction.loadBufferedImage("/resources/ships/player_test_ship.png");
			this.size = currentImage.getWidth()/2;
			Graphics2D c2 = (Graphics2D)this.currentImage.getGraphics();
			c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
			c2.setColor(Color.WHITE);
			c2.drawOval(0, 0, (int)Math.round(this.size*2), (int)Math.round(this.size*2));			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	