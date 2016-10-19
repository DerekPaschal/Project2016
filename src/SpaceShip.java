abstract class SpaceShip extends PhysicsSprite
{
	public boolean left, right, forward, backward, firing;
	private double turnRate = 5, thrustPower = 0.2;
	
	public SpaceShip(Vector2D position)
	{
		super(position);
		
		left = false;
		right = false;
		forward = false;
		backward = false;
		firing = false;
	}
	
	//public void forward() { this.forward = true; }
	//public void backward() { this.backward = true; }
	//public void left() { this.left = true; }
	//public void right() { this.right = true; }
	
	public void update()
	{
		this.acc = new Vector2D();
		
		//Add Acceleration from Thrusters
		if (forward)
			this.acc = this.acc.add(new Vector2D(Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower, Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
		if (backward)
			this.acc = this.acc.add(new Vector2D(-Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower,-Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
		
		//Apply Friction Force
		this.acc = this.acc.subtract(this.vel.multiply(new Vector2D(0.02,0.02)));
		
		//Integrate Acceleration
		this.vel = this.vel.add(this.acc);
		
		
		
		//Integrate Velocity
		this.pos = this.pos.add(this.vel);
		
		//Add Rotation
		if (left)
			this.rotation.addAmount(-this.turnRate);
		if (right)
			this.rotation.addAmount(this.turnRate);
		
	}
}
