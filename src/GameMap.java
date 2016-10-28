/***************************
 * Purpose: GameMap class, containing all relevant data
 * to a single game map
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Rectangle;

public class GameMap {
	public MapBoundary mapBoundary;
	public Model model;
	
	public GameMap(Model m)
	{
		this.mapBoundary = new MapBoundary();
		this.model = m;
	}
}
