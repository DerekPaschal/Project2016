import java.io.IOException;

public class PlayerShip extends PhysicsSprite
{
	public boolean left, right, forward, backward, firing;
	private double turnRate = 5, thrustPower = 0.2;
	
	public PlayerShip(Vector2D position)
	{
		super(position);
		
		left = false;
		right = false;
		forward = false;
		backward = false;
		firing = false;
		
		try
		{
			this.currentImage = GameFunction.loadBufferedImage("/resources/ships/player_test_ship.png");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void forward() { this.forward = true; }
	public void backward() { this.backward = true; }
	public void left() { this.left = true; }
	public void right() { this.right = true; }
	
	public void update()
	{
		if (forward)
		{
			this.vel = this.vel.add(new Vector2D(Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower, Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
		}
		if (backward)
		{
			this.vel = this.vel.add(new Vector2D(-Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower,-Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
		}
		if (left)
		{
			this.rotation.addAmount(-this.turnRate);
		}
		if (right)
		{
			this.rotation.addAmount(this.turnRate);
		}
		
		this.pos = this.pos.add(this.vel);
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	