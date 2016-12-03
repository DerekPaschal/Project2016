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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameMap {
	
	enum MapType {DEMO, BLANK, FILE;};
	
	public ArrayList<MapBoundary> fieldBoundaries;
	public MapBoundary mapBoundary;
	public MapType mapType;
	public Model model;
	private PlayerShip playerShip;
	ExecutorService executor;

	public GameMap(Model m)
	{
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,100,100));
		this.model = m;
		initMap();
		
		//Setup Thread Executor for threaded portions
		executor = Executors.newCachedThreadPool();
	}
	
	//Initialize resources for a map
	private void initMap()
	{
		//Set up resources
		this.fieldBoundaries = new ArrayList<MapBoundary>();
		this.mapBoundary = new MapBoundary(new Rectangle(0,0,100,100));
		this.fieldBoundaries.add(mapBoundary);
	}
	
	public void updateMap() throws Exception
	{
		int sprite_threads = 4;
		
		if (sprite_threads < 1)
			throw new Exception("Calculation threads cannot be less than 1.");
		
		//synchronized(mv.gameSpritesLock)
		synchronized(SpriteList.SpriteLock)
		{
			//Set all PhysicsSprite accelerations in gameMap to 0
			clearPhysicsSpritesAcc();
			
			//Update accelerations of each PhysicsSprite in the game
			CountDownLatch latch = new CountDownLatch(sprite_threads);
			double divided = SpriteList.size() / sprite_threads;
			
			int tempStart = 0, tempEnd = 0;
			//PhysicsSprite-PhysicsSprite collisions
			for (int i = 0; i<sprite_threads;i++)
			{
				tempStart = tempEnd;
				tempEnd = Math.max((int) (divided * (i+1)), SpriteList.size());
				
				executor.execute(new UpdateAccThread(tempStart, tempEnd, latch));
				
			}
			
			//Wait
			try {latch.await();}catch(InterruptedException e){e.printStackTrace();}
			
			//Remove all Sprites that are marked for removal
			cleanPhysicsSpritesList();
			
			//Add sprites to the list if needed
			addGameSprites();
			
			//Update velocity and position of each PhysicsSprite
			updateVelPos();
		}
		
		if (this.playerShip != null)
			ViewCamera.pos = this.playerShip.pos;
	}
	
	//Remove all sprites except gui
	public void removeAllSprites()
	{
		synchronized (SpriteList.SpriteLock)
		{
			int index = 0;
			for(int i = 0; i < SpriteList.size(); i++)
			{
				if (!(SpriteList.get(index) instanceof GameGUI))
					SpriteList.remove(index);
				else
				{
					index++;
					i++;
				}
			}
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
		
		this.playerShip = new PlayerShip(new Vector2D(200.0, 200.0));
		SpriteList.add(this.playerShip);
		
		//Draw Background sprites
		BackgroundSprite starfield = new BackgroundSprite(new Vector2D(0, 0), "backgrounds/back-stars.png");
		synchronized(SpriteList.SpriteLock)
		{
			SpriteList.add(starfield);
		}
		
		//Create inner asteroid field
		int numAsteroids = 500;
		MapBoundary asteroidField = new MapBoundary(new Rectangle(500,500,1000,1000));
		asteroidField.boundaryColor = Color.BLUE;
		asteroidField.setForce(0.0005);
		Asteroid adding;
		synchronized (SpriteList.SpriteLock)
		{
			for (int i = 0; i < numAsteroids; i++)
			{
				adding = new Asteroid(new Vector2D(asteroidField.getLeftBound() + Math.random()*asteroidField.getWidth(), 
						asteroidField.getUpperBound() + Math.random()*asteroidField.getHeight()), new Rotation(Math.random()*360),
						10+(Math.random() * 10), 0.99);
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
		
		if (this.playerShip != null)
			ViewCamera.pos = this.playerShip.pos;
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
	
	/**
	 * General purpose method that adds sprites to the GameMap depending on
	 * the current status of the Game.
	 */
	public void addGameSprites()
	{		
		if (this.playerShip.firing && this.playerShip.timeSinceFiring>this.playerShip.bulletCooldown)
		{
			SpriteList.add(this.playerShip.fireBullet());
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



class UpdateAccThread implements Runnable
{
	int begin, end;
	CountDownLatch latch;
	
	UpdateAccThread(double begin, double end, CountDownLatch latch)
	{
		this.begin = (int)begin;
		this.end = (int)end;
		this.latch = latch;
	}

	@Override
	public void run() 
	{		
		Sprite s;
		for(int i = begin; i < end; i++)
		{
			s = SpriteList.get(i);
			if (s instanceof PhysicsSprite)
				((PhysicsSprite) s).updateAcc(i);
			else if (s instanceof MapBoundary)
				((MapBoundary)s).checkCollision();
		}
		
		//CountDown the latch so main thread can join
		latch.countDown();
	}
}
