/***************************
 * Purpose: GameMap class, containing all relevant data
 * to a single game map
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.util.ArrayList;

public class GameMap {
	public ArrayList<MapBoundary> fieldBoundaries;
	private ArrayList<PhysicsSprite> physicsSprites;
	public Object physicsSpritesLock = new Object();
	public MapBoundary mapBoundary;
	public MapType mapType;
	public Model model;
	
	public GameMap(Model m, MapType type)
	{
		this.mapType = type;
		this.model = m;
		initMap();
		loadMap(type);
	}
	public GameMap(Model m)
	{
		this.mapBoundary = new MapBoundary();
		this.model = m;
		initMap();
		loadMap(MapType.DEMO);
	}
	
	private void initMap()
	{
		//Set up resources
		this.fieldBoundaries = new ArrayList<MapBoundary>();
		this.physicsSprites = new ArrayList<PhysicsSprite>();
		this.mapBoundary = new MapBoundary();
		for (PhysicsSprite pSprite : physicsSprites)
			mapBoundary.addSprite(pSprite);
		this.fieldBoundaries.add(mapBoundary);
	}
	
	private void loadMap(MapType type)
	{
		switch (type)
		{
		case DEMO:
			loadDemo();
			break;
		case BLANK:
			break;
		case FILE:
			break;
		default:
			loadMap(MapType.DEMO);
			break;
		}
	}
	
	private void loadDemo()
	{
		
	}
	
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
	
	public void removePhysicsSprite(PhysicsSprite sprite)
	{
		synchronized (this.physicsSprites)
		{
			this.physicsSprites.remove(sprite);
			
			if (sprite instanceof Asteroid)
			{
				this.mapBoundary.removeSprite(sprite);
			}
		}
	}
	
	public void clearPhysicsSpritesAcc()
	{
		for (PhysicsSprite pSprite : this.physicsSprites)
		{
			pSprite.acc.x = 0;
			pSprite.acc.y = 0;
		}
	}
	
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
	
	public void cleanPhysicsSpritesList()
	{
		for (PhysicsSprite pSprite : this.physicsSprites)
		{
			if (pSprite.remove)
				this.physicsSprites.remove(pSprite);
		}
	}
}
