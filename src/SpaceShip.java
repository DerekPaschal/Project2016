/***************************
 * Purpose: SpaceShip class containing common
 * functionality for spaceship behavior.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.util.ArrayList;

abstract class SpaceShip extends PhysicsSprite
{
	public boolean left, right, forward, backward, firing;
	private double turnRate = 2, thrustPower = 0.2;
	
	public SpaceShip(Vector2D position, Rotation rotation, double size, double health)
	{
		super(position,rotation,size, 1.0, health);
		
		left = false;
		right = false;
		forward = false;
		backward = false;
		firing = false;
	}
	
	@Override
	public void updateAcc(int index)
	{		
		super.CollisionDetect(index);
		
		synchronized (this.acc)
		{
			//Add Acceleration from Thrusters
			if (forward)
				this.acc = this.acc.add(new Vector2D(Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower, Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
			if (backward)
				this.acc = this.acc.add(new Vector2D(-Math.cos(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower,-Math.sin(this.rotation.getRadians() - Math.PI / 2.0) * this.thrustPower));
			
			//Apply Friction Force (In Space?)
			this.acc = this.acc.subtract(this.vel.multiply(new Vector2D(0.02,0.02)));
			
			//Add Rotation
			if (left)
				//this.rotation.addAmount(-this.turnRate);
				this.rot_acc =+ -this.turnRate;
			if (right)
				//this.rotation.addAmount(this.turnRate);
				this.rot_acc =+ this.turnRate;
			
			//Apply Rotation Friction (In Space?)
			this.rot_acc = this.rot_acc - (this.rot_vel * 0.2);
		}
	}
	
	@Override
	public void collisionAlert(PhysicsSprite impactor, double impact)
	{
		this.health = this.health - impact;
		if (this.health < 0)
			this.remove = true;
	}
}
