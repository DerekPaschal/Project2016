
abstract class PhysicsSprite extends Sprite
{
	public Vector2D vel;
	public Vector2D acc;
	public double rot_vel;
	public double rot_acc;
	public double size;
	
	public PhysicsSprite(Vector2D position, double size) 
	{
		super(position);
		this.size = size;
		this.vel = new Vector2D(0.0,0.0);
		this.acc = new Vector2D(0.0,0.0);
		this.rot_vel = 0.0;
		this.rot_acc = 0.0;
	}
	
	public abstract void updateAcc();
	
	public abstract void collisionAlert(PhysicsSprite impactor);
	
	public void CollisionDetect()
	{
		//Physics Collision Detection
		double distance;
		Vector2D UnitVector;
		for(Sprite sprite : PhysicsVars.SpriteList)
		{
			if (sprite instanceof PhysicsSprite && this != sprite)
			{
				distance = this.pos.distance(sprite.pos);
				if (distance < (this.size + ((PhysicsSprite)sprite).size))
				{
					UnitVector  = this.pos.subtract(sprite.pos).divide(distance);
					this.acc = this.acc.add(UnitVector.multiply(3*(Math.min((this.size + ((PhysicsSprite)sprite).size) - distance,Math.min(this.size, ((PhysicsSprite)sprite).size))/this.size)));
					this.collisionAlert((PhysicsSprite)sprite);
				}
			}
		}
	}
	
	public void updateVelPos()
	{
		//Integrate Acceleration
		this.vel = this.vel.add(this.acc.multiply(PhysicsVars.timestep));
		
		//Integrate Rotational Acceleration
		this.rot_vel += this.rot_acc;
			
		//Integrate Velocity
		this.pos = this.pos.add(this.vel.multiply(PhysicsVars.timestep));
		
		//Integrate Rotaional Velocity
		this.rotation.addAmount(this.rot_vel);
	}

}
