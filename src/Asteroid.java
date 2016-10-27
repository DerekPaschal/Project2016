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
		int color_value = (int)(128+(Math.random()*64));
		this.color = new Color(color_value,color_value,color_value);
	}
	
	@Override
	public void updateAcc()
	{		
		this.acc = new Vector2D();
		
		super.CollisionDetect();
	}
	
	@Override
	public void updateVelPos()
	{
		/*if (this.pos.x < 0 && this.vel.x < 0)
			this.vel.x = -this.vel.x;
		else if (this.pos.x > ViewCamera.renderRes.x && this.vel.x > 0)
			this.vel.x = -this.vel.x;
		
		if (this.pos.y < 0 && this.vel.y < 0)
			this.vel.y = -this.vel.y;
		else if (this.pos.y > ViewCamera.renderRes.y && this.vel.y > 0)
			this.vel.y = -this.vel.y;*/
		
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
	
	}
}
