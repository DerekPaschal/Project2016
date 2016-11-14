import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Asteroid extends PhysicsSprite
{
	public Color color;
	
	public Asteroid(Vector2D position, double size, double restitution)
	{
		
		super(position, Math.round(size),restitution);
		this.vel = new Vector2D();
		int color_value = (int)(128+(Math.random()*64));
		this.color = new Color(color_value,color_value,color_value);
	}
	
	public void setColor(Color c)
	{
		this.color = c;
	}
	
	@Override
	public void updateAcc(ArrayList<PhysicsSprite> physicsSprites)
	{				
		super.CollisionDetect(physicsSprites);
	}
	
	@Override
	public void updateVelPos()
	{
		super.updateVelPos();
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		this.currentImage = new BufferedImage((int)Math.round(this.size*2), (int)Math.round(this.size*2), BufferedImage.TYPE_INT_ARGB);
		Graphics2D c2 = (Graphics2D) currentImage.getGraphics();
		//c2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		c2.setColor(this.color);
		c2.fillOval(0, 0, (int)Math.round(this.size*2), (int)Math.round(this.size*2));
		
		super.draw(g2);
	}

	@Override
	public void collisionAlert(PhysicsSprite impactor) 
	{
		//System.out.println(this.getClass().getName() + " collided with: " + impactor.getClass().getName());
	}
}
