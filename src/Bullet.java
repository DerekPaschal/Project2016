import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Bullet extends PhysicsSprite
{
	public double size;
	public Color color;
	
	public Bullet(Vector2D position)
	{
		super(position);
	}

	@Override
	public void update() 
	{
		super.update();
	}
	
	@Override
	public void draw(Graphics2D g2, ViewCamera camera)
	{
		this.currentImage = new BufferedImage((int)((this.size*2)+1), (int)((this.size*2)+1), BufferedImage.TYPE_INT_ARGB);
		Graphics2D c2 = (Graphics2D) currentImage.getGraphics();
		c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		c2.setColor(this.color);
		c2.fillOval(0, 0, (int)(this.size * 2), (int)(this.size * 2));
		
		super.draw(g2, camera);
	}
}
