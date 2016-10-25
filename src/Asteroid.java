import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Asteroid extends PhysicsSprite
{
	public Color color;
	
	public Asteroid(Vector2D position, double size)
	{
		
		super(position, Math.round(size));
		this.vel = new Vector2D();
		this.color = Color.WHITE;
	}
	
	@Override
	public void draw(Graphics2D g2, ViewCamera camera)
	{
		this.currentImage = new BufferedImage((int)Math.round(this.size*2), (int)Math.round(this.size*2), BufferedImage.TYPE_INT_ARGB);
		Graphics2D c2 = (Graphics2D) currentImage.getGraphics();
		c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		c2.setColor(this.color);
		c2.fillOval(0, 0, (int)Math.round(this.size*2), (int)Math.round(this.size*2));
		
		super.draw(g2, camera);
	}
}
