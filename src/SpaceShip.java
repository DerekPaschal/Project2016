import java.awt.Graphics2D;
import java.util.ArrayList;

abstract class SpaceShip extends PhysicsSprite
{
	public boolean left, right, forward, backward, firing;
	private double turnRate = 5, thrustPower = 0.2;
	
	public SpaceShip(Vector2D position, double size)
	{
		super(position,size);
		
		left = false;
		right = false;
		forward = false;
		backward = false;
		firing = false;
	}
	
	@Override
	public void updateAcc(ArrayList<PhysicsSprite> physicsSprites)
	{		
		super.CollisionDetect(physicsSprites);
		
		//Add Acceleration from Thrusters
		if (forward)
			this.acc = this.acc.add(new Vector2D(Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower, Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
		if (backward)
			this.acc = this.acc.add(new Vector2D(-Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower,-Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
		
		//Apply Friction Force
		this.acc = this.acc.subtract(this.vel.multiply(new Vector2D(0.02,0.02)));
		
		//Add Rotation
		if (left)
			this.rotation.addAmount(-this.turnRate);
		if (right)
			this.rotation.addAmount(this.turnRate);
	}
	
	@Override
	public void collisionAlert(PhysicsSprite impactor)
	{

	}
}
