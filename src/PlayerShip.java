import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
	
	
	public void draw(Graphics2D g2, ViewCamera camera)
	{
		AffineTransform at = new AffineTransform();
		at.scale(camera.Zoom, camera.Zoom);
		double transX = (this.pos.x - ((double)currentImage.getWidth() * at.getScaleX() * camera.Zoom))/at.getScaleX();
		double transY = (this.pos.y - ((double)currentImage.getHeight() * at.getScaleY() * camera.Zoom))/at.getScaleY();
		at.translate(transX, transY);
		
		double rotateX = currentImage.getWidth() / 2;
		double rotateY = currentImage.getHeight() / 2;
		at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		
		
		g2.drawImage(currentImage, at, null);
	}
	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	