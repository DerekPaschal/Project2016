import java.io.IOException;

public class PlayerShip extends SpaceShip
{

	public PlayerShip(Vector2D position) 
	{
		super(position);
		
		try
		{
			this.currentImage = GameFunction.loadBufferedImage("/resources/ships/player_test_ship.png");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	