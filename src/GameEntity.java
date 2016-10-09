/***************************
 * Purpose: GameEntity class containing core
 * characteristics of entities in the game,
 * such as position on the map
 *
 * Original Author: Zachary Johnson
 ***************************/

public class GameEntity
{
	public Point2D mapPos, velocity;
	
	public GameEntity()
	{
		this.mapPos = new Point2D(0, 0);
		this.velocity = new Point2D(0, 0);
	}
}
