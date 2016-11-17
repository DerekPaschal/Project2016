/***************************
 * Purpose: GameMap class containing all relevant
 * data for a single loaded map in the game.
 * Methods for adding and removing from the GameMap
 * automatically update SpriteList for drawing.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class GameMap {
	
	enum MapType {DEMO, BLANK, FILE;};
	
	public ArrayList<MapBoundary> fieldBoundaries;
	private ArrayList<PhysicsSprite> physicsSprites;
	public Object physicsSpritesLock = new Object();
	public MapBoundary mapBoundary;
	public MapType mapType;
	public Model model;
	
//	public GameMap(Model m, MapType type)
//	{
//		this.mapType = type;
//		this.model = m;
//		initMap();
//	}
	public GameMap(Model m)
	{
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,100,100));
		this.model = m;
		initMap();
	}
	
	//Initialize resources for a map
	private void initMap()
	{
		//Set up resources
		this.fieldBoundaries = new ArrayList<MapBoundary>();
		this.physicsSprites = new ArrayList<PhysicsSprite>();
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,100,100));
		this.fieldBoundaries.add(mapBoundary);
	}
	
	//Load a map based on the given map type
	public void loadMap(MapType type)
	{
		switch (type)
		{
		case DEMO:
			loadDemo();
			break;
		case BLANK:
			//Unimplemented
			break;
		case FILE:
			//Unimplemented
			break;
		default:
			loadMap(MapType.DEMO);
			break;
		}
	}
	
	private void loadDemo()
	{
		synchronized(this.fieldBoundaries)
		{
			this.fieldBoundaries.clear();
		}
		synchronized(this.physicsSpritesLock)
		{
			this.physicsSprites.clear();
		}
		
		PlayerShip playerShip = new PlayerShip(new Vector2D(500.0, 500.0));
		addPhysicsSprite(playerShip);
		SpriteList.setPlayerShip(playerShip);
		
		//Create inner asteroid field
		int numAsteroids = 500;
		MapBoundary asteroidField = new MapBoundary(new Rectangle(500,500,1000,1000));
		asteroidField.boundaryColor = Color.BLUE;
		asteroidField.setForce(0.0005);
		Asteroid adding;
		for (int i = 0; i < numAsteroids; i++)
		{
			adding = new Asteroid(new Vector2D(asteroidField.getLeftBound() + Math.random()*asteroidField.getWidth(), 
					asteroidField.getUpperBound() + Math.random()*asteroidField.getHeight()), new Rotation(Math.random()*360),
					4+(Math.random() * 6), 0.8);
			//adding.vel =  new Vector2D(Math.random()-0.5, Math.random()-0.5);
			this.addPhysicsSprite(adding);
			asteroidField.addSprite(adding);
		}
		//fieldBoundaries.add(asteroidField);
		addBoundary(asteroidField);
		
		
		//Create outer map bounds
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,2000,2000));
		for (PhysicsSprite pSprite : physicsSprites)
			mapBoundary.addSprite(pSprite); //Already synchronized
//		model.mv.addGameSprite(this.mapBoundary);
//		fieldBoundaries.add(this.mapBoundary);
		addBoundary(mapBoundary);
			
		
	}
	
	/*
	 * Add a MapBoundary to the GameMap (and automatically
	 * add it to the master sprite list)
	 */
	public boolean addBoundary(MapBoundary mb)
	{
		this.fieldBoundaries.add(mb);
		
		synchronized(SpriteList.spriteListLock)
		{
			return SpriteList.addSprite(mb);
		}
	}
	
	/*
	 * Add a PhysicsSprite to the GameMap (and automatically
	 * add it to the master sprite list). New PhysicsSprites
	 * will be affected by the mapBoundary.
	 */
	public void addPhysicsSprite(PhysicsSprite sprite)
	{
		synchronized (this.physicsSprites)
		{
			this.physicsSprites.add(sprite);
		}
		
		this.mapBoundary.addSprite(sprite); //Already synchronized
		
		SpriteList.addSprite(sprite); //Already synchronized
	}
	
	/*
	 * Completely remove a PhysicsSprite from GameMap (and
	 * master sprite list). The PhysicsSprite will also be
	 * removed from all MapBoundary list of affected sprites
	 */
	public void removePhysicsSprite(PhysicsSprite sprite)
	{
		synchronized (this.physicsSprites)
		{
			this.physicsSprites.remove(sprite);
		}
		
		for (MapBoundary currBoundary : this.fieldBoundaries)
				currBoundary.removeSprite(sprite); //Already synchronized
		
		SpriteList.removeSprite(sprite); //Already synchronized
	}
	
	/*
	 * Set the acceleration of all PhysicsSprites in GameMap
	 * to 0 (first step for physics calculations)
	 */
	public void clearPhysicsSpritesAcc()
	{
		synchronized (this.physicsSprites)
		{
			for (PhysicsSprite pSprite : this.physicsSprites)
			{
				pSprite.acc.x = 0;
				pSprite.acc.y = 0;
				pSprite.rot_acc = 0;
			}
		}
	}
	
	/*
	 * Get size of the list of PhysicsSprites for use
	 * in physics calculations
	 */
	public int getPhysicsSpritesLength()
	{
		synchronized (this.physicsSprites)
		{
			return this.physicsSprites.size();
		}
	}
	
	//Use only for read-only and sprite update loops
	//NOT for use for modifying the list
	public ArrayList<PhysicsSprite> getPhysicsSprites()
	{
		return this.physicsSprites;
	}
	
	public void updateVelPos()
	{
		synchronized(SpriteList.spriteListLock)
		{
			for (PhysicsSprite pSprite : this.physicsSprites)
			{
				if (pSprite instanceof PlayerShip)
					System.out.println("FOUND ONE!");
				
				pSprite.updateVelPos();
			}
		}
	}
	
	/*
	 * Clear all sprites from the list of PhysicsSprites
	 * that are marked for removal
	 */
	public void cleanPhysicsSpritesList()
	{
		for (PhysicsSprite pSprite : this.physicsSprites)
		{
			if (pSprite != null && pSprite.remove)
				this.physicsSprites.remove(pSprite);
		}
	}
}
