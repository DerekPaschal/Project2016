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
import java.util.ListIterator;

public class GameMap {
	
	enum MapType {DEMO, BLANK, FILE;};
	
	public ArrayList<MapBoundary> fieldBoundaries;
	//private ArrayList<PhysicsSprite> physicsSprites;
	//private ArrayList<BackgroundSprite> backgroundSprites;
	//public Object physicsSpritesLock = new Object();
	public MapBoundary mapBoundary;
	public MapType mapType;
	public Model model;
	

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
		//this.physicsSprites = new ArrayList<PhysicsSprite>();
		//this.backgroundSprites = new ArrayList<BackgroundSprite>();
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,100,100));
		this.fieldBoundaries.add(mapBoundary);
	}
	
	//Remove all sprites except gui
	public void removeAllSprites()
	{
		synchronized (SpriteList.SpriteLock)
		{
			for(int i = 0; i < SpriteList.size()-1; i++)
			{
				SpriteList.remove(0);
			}
			/*synchronized (this.physicsSprites)
			{
				for (PhysicsSprite pSprite : this.physicsSprites)
				{
					SpriteList.remove(pSprite);
				}
				this.physicsSprites.clear();
			}
			
			synchronized (this.fieldBoundaries)
			{
				//All sprites in fieldBoundaries already cleared
				//from master sprite list, so removing boundaries is
				//safe
				for (Sprite currBound : this.fieldBoundaries)
				{
					SpriteList.remove(currBound);
				}
				this.fieldBoundaries.clear();
			}
			
			synchronized (this.backgroundSprites)
			{
				for (BackgroundSprite currBackground : this.backgroundSprites)
				{
					SpriteList.remove(currBackground);
				}
				this.backgroundSprites.clear();
			}*/
		}
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
		//Remove any previous sprites
		this.removeAllSprites();
		
		PlayerShip playerShip = new PlayerShip(new Vector2D(500.0, 500.0));
		//addPhysicsSprite(playerShip);
		SpriteList.add(playerShip);
		
		//Draw Background sprites
		BackgroundSprite starfield = new BackgroundSprite(new Vector2D(0, 0), "backgrounds/back-stars.png");
		synchronized(SpriteList.SpriteLock)
		{
			this.addBackgroundSprite(starfield);
		}
		
		//Create inner asteroid field
		int numAsteroids = 100;
		MapBoundary asteroidField = new MapBoundary(new Rectangle(500,500,500,500));
		asteroidField.boundaryColor = Color.BLUE;
		asteroidField.setForce(0.0005);
		Asteroid adding;
		synchronized (SpriteList.SpriteLock)
		{
			for (int i = 0; i < numAsteroids; i++)
			{
				adding = new Asteroid(new Vector2D(asteroidField.getLeftBound() + Math.random()*asteroidField.getWidth(), 
						asteroidField.getUpperBound() + Math.random()*asteroidField.getHeight()), new Rotation(Math.random()*360),
						8+(Math.random() * 16), 0.8,100);
				adding.vel =  new Vector2D(Math.random()-0.5, Math.random()-0.5);
				adding.rot_vel = Math.random()*5-2.5;
				//this.addPhysicsSprite(adding);
				SpriteList.add(adding);
				asteroidField.addSprite(adding);
			}
		}
		//Add asteroid field to the GameMap
		addBoundary(asteroidField);
		
		//Create outer map bounds
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,2000,2000));
		synchronized(SpriteList.SpriteLock)
		{
			Sprite s;
			for (int i = 0; i < SpriteList.size(); i++)
			{
				s = SpriteList.get(i);
				if (s instanceof PhysicsSprite)
					mapBoundary.addSprite((PhysicsSprite)s);
			}
		}
		//Add mapBoundary to the GameMap
		addBoundary(mapBoundary);
	}
	
	/*
	 * Add a MapBoundary to the GameMap (and automatically
	 * add it to the master sprite list)
	 */
	public void addBoundary(MapBoundary mb)
	{
		synchronized(this.fieldBoundaries)
		{
			this.fieldBoundaries.add(mb);
		}
		SpriteList.add(mb); //Already synchronized
	}
	
	/*
	 * Add a PhysicsSprite to the GameMap (and automatically
	 * add it to the master sprite list). New PhysicsSprites
	 * will be affected by the mapBoundary.
	 */
	/*public void addPhysicsSprite(PhysicsSprite sprite)
	{
		synchronized(this.physicsSprites)
		{
			ListIterator<PhysicsSprite> i = this.physicsSprites.listIterator();
			i.add(sprite);
		}
		
		SpriteList.add(sprite);
	}*/
	
	/*
	 * Add a BackgroundSprite to the GameMap (and automatically
	 * add it master background sprite list).
	 */
	public void addBackgroundSprite(BackgroundSprite sprite)
	{
		/*synchronized (this.backgroundSprites)
		{
			this.backgroundSprites.add(sprite);
		}*/
		
		SpriteList.add(sprite); //Already synchronized
	}
	
	/*
	 * Completely remove a PhysicsSprite from GameMap (and
	 * master sprite list). The PhysicsSprite will also be
	 * removed from all MapBoundary list of affected sprites
	 */
	public void removePhysicsSprite(PhysicsSprite sprite)
	{
		sprite.remove = true;
	}
	
	/*
	 * Completely remove a BackgroundSprite from GameMap (and
	 * master background list).
	 */
	public void removeBackgroundSprite(BackgroundSprite sprite)
	{
		/*synchronized(this.backgroundSprites)
		{
			this.backgroundSprites.remove(sprite);
		}*/
		
		SpriteList.remove(sprite); //Already synchronized
	}
	
	/*
	 * Set the acceleration of all PhysicsSprites in GameMap
	 * to 0 (first step for physics calculations)
	 */
	public void clearPhysicsSpritesAcc()
	{
		synchronized (SpriteList.SpriteLock)
		{
			Sprite s;
			for (int i = 0; i < SpriteList.size(); i++)
			{
				s = SpriteList.get(i);
				if (s instanceof PhysicsSprite)
				{
					((PhysicsSprite)s).acc.x = 0;
					((PhysicsSprite)s).acc.y = 0;
					((PhysicsSprite)s).rot_acc = 0;
				}
			}
		}
	}
	
	/*
	 * Get size of the list of PhysicsSprites for use
	 * in physics calculations
	 */
	/*public int getPhysicsSpritesLength()
	{
		synchronized (this.physicsSprites)
		{
			return this.physicsSprites.size();
		}
	}*/
	
	//Use only for read-only and sprite update loops
	//NOT for use for modifying the list
	/*public ArrayList<PhysicsSprite> getPhysicsSprites()
	{
		return this.physicsSprites;
	}*/
	
	public void updateVelPos()
	{
		synchronized(SpriteList.SpriteLock)
		{
			Sprite s;
			for (int i = 0; i < SpriteList.size(); i++)
			{
				s = SpriteList.get(i);
				if (s instanceof PhysicsSprite)
					((PhysicsSprite)s).updateVelPos();
			}
		}
	}
	
	
	/*
	 * Clear all sprites from the list of PhysicsSprites
	 * that are marked for removal
	 */
	public void cleanPhysicsSpritesList()
	{
		synchronized (SpriteList.SpriteLock)
		{
			/*synchronized (this.physicsSprites)
			{
				for (ListIterator<PhysicsSprite> i = this.physicsSprites.listIterator(); i.hasNext(); )
				{
					PhysicsSprite pSprite = i.next();
					if (pSprite != null && pSprite.remove)
					{
						i.remove();
						SpriteList.removeSprite(pSprite);
					}
				}
			}*/
			
			Sprite s;
			for (int i = 0; i < SpriteList.size(); i++)
			{
				s = SpriteList.get(i);
				if (s.remove)
					SpriteList.remove(s);
			}
		}
	}
}
