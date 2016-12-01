/***************************
 * Purpose: PlayerShip class extending SpaceShip,
 * containing behaviors specific to the player's
 * ship and not shared between all spaceships.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerShip extends SpaceShip
{
	public PlayerShip(Vector2D position) 
	{
		super(position,new Rotation(0),0.0,10,20);
		
		this.size = 40; //Size of 'collision bubble' of ship
	}
	
	
}
