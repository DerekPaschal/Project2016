
public class PhysicsSprite extends Sprite
{
	public Vector2D vel;
	public Vector2D acc;
	
	public PhysicsSprite(Vector2D position) 
	{
		super(position);
		this.vel = new Vector2D(0.0,0.0);
		this.acc = new Vector2D(0.0,0.0);
	}

}
