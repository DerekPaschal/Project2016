/***************************
 * Purpose: GameMap class, containing all relevant data
 * to a single game map
 *
 * Original Author: Zachary Johnson
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
		model.mv.playerShip = new PlayerShip(new Vector2D(50.0, 50.0));
		
		//Clear any existing resources on the map
		//this.mapBoundary = null;
		synchronized(this.fieldBoundaries)
		{
			this.fieldBoundaries.clear();
		}
		synchronized(this.physicsSpritesLock)
		{
			this.physicsSprites.clear();
		}
		
		synchronized(this.physicsSpritesLock)
		{
			model.mv.addGameSprite(model.mv.playerShip);
			
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
			fieldBoundaries.add(asteroidField);
			//System.out.println(asteroidField.getInfo());
			
			//Create outer map bounds
			this.mapBoundary = new MapBoundary(new Rectangle(0,0,2000,2000));
			for (PhysicsSprite pSprite : physicsSprites)
				mapBoundary.addSprite(pSprite);
			model.mv.addGameSprite(this.mapBoundary);
			fieldBoundaries.add(this.mapBoundary);
			//System.out.println(this.mapBoundary.getInfo());
			
		}
	}
	
	//Add a physics sprite to the map
	//The physics sprite will automatically be added to the
	//map boundary's list
	public void addPhysicsSprite(PhysicsSprite sprite)
	{
		synchronized (this.physicsSprites)
		{
			this.physicsSprites.add(sprite);
			
			if (sprite instanceof Asteroid || sprite instanceof PlayerShip)
			{
				this.mapBoundary.addSprite(sprite);
			}
		}
	}
	
	//Safely remove a physics sprite completely from the list
	public void removePhysicsSprite(PhysicsSprite sprite)
	{
		synchronized (this.physicsSprites)
		{
			//Remove from master physics sprites list
			this.physicsSprites.remove(sprite);
			
			//Remove sprite entry in all sprite boundaries
			for (MapBoundary currBoundary : this.fieldBoundaries)
				currBoundary.removeSprite(sprite);
		}
	}
	
	//Set acceleration of all physics sprites to 0
	public void clearPhysicsSpritesAcc()
	{
		for (PhysicsSprite pSprite : this.physicsSprites)
		{
			pSprite.acc.x = 0;
			pSprite.acc.y = 0;
			pSprite.rot_acc = 0;
		}
	}
	
	//Get the length of the map's physicsSprites list
	public int getPhysicsSpritesLength()
	{
		return this.physicsSprites.size();
	}
	
	//Use only for read-only and sprite update loops
	//NOT for use for modifying the list
	public ArrayList<PhysicsSprite> getPhysicsSprites()
	{
		return this.physicsSprites;
	}
	
	public void updateVelPos()
	{
		for (PhysicsSprite pSprite : this.physicsSprites)
		{
			pSprite.updateVelPos();
		}
	}
	
	//Iterate through physicsSprites list and remove all
	//sprites marked for removal
	public void cleanPhysicsSpritesList()
	{
		for (PhysicsSprite pSprite : this.physicsSprites)
		{
			if (pSprite.remove)
				this.physicsSprites.remove(pSprite);
		}
	}
}
