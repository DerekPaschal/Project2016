
abstract class PhysicsSprite extends Sprite
{
	public Vector2D vel;
	public Vector2D acc;
	public double size;
	
	public PhysicsSprite(Vector2D position, double size) 
	{
		super(position);
		this.size = size;
		this.vel = new Vector2D(0.0,0.0);
		this.acc = new Vector2D(0.0,0.0);
	}
	
	public void updateAcc()
	{
		if (this.remove)
			return;
		
		this.acc = new Vector2D();
		
		double distance;
		Vector2D UnitVector;
		for(Sprite sprite : PhysicsVars.SpriteList)
		{
			if (!sprite.remove && sprite instanceof PhysicsSprite && this != sprite)
			{
				distance = this.pos.distance(sprite.pos);
				if (distance < (this.size + ((PhysicsSprite)sprite).size))
				{
					UnitVector  = this.pos.subtract(sprite.pos).divide(distance);
					this.acc = this.acc.add(UnitVector.multiply(3*(Math.min((this.size + ((PhysicsSprite)sprite).size) - distance,Math.min(this.size, ((PhysicsSprite)sprite).size))/this.size)));
				}
			}
		}
	}
	
	public void updateVelPos()
	{
		//Integrate Acceleration
		this.vel = this.vel.add(this.acc.multiply(PhysicsVars.timestep));
			
		//Integrate Velocity
		this.pos = this.pos.add(this.vel.multiply(PhysicsVars.timestep));
	}

}
