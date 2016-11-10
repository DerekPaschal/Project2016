/***************************
 * Purpose: Model class of Model-View-Controller paradigm,
 * Core game model starts in this class
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



class Model
{
	int tick = 0;
	public ModelVars mv;
	ExecutorService executor;
	
	/***************************
	 * Constructor
	 ***************************/
	Model() throws IOException
	{
		//Set up class containing model variables
		this.mv = new ModelVars(this);
		
		//Set up basic resources
		this.mv.rand = new Random(mv.seed);
		
		this.mv.setGameState(GameState.MAIN_MENU);
		
		//Setup Thread Executor for threaded portions
		executor = Executors.newCachedThreadPool();
	}

	/***************************
	 * Called when game state changes to main menu
	 ***************************/
	public void init_MainMenu()
	{
		mv.mainMenu = new FullScreenMenu();
	}
	 
	 /***************************
	 * Called when game state changes to the game
	 ***************************/
	public void init_Game()
	{
		PhysicsVars.SpriteList = mv.gameSprites;
		PhysicsVars.timestep = 1.0;
		
		mv.playerShip = new PlayerShip(new Vector2D(50.0, 50.0));
		
		synchronized(mv.gameSprites)
		{
			mv.gameMap.mapBoundary.setBounds(new Rectangle(0,0,2000,2000));
			mv.gameSprites.add(mv.gameMap.mapBoundary);
			mv.gameSprites.add(mv.playerShip);
			Asteroid adding;
			for (int i = 0; i < 1000; i++)
			{
				adding = new Asteroid(new Vector2D(Math.random()*mv.gameMap.mapBoundary.getRightBound(),Math.random()*mv.gameMap.mapBoundary.getLowerBound()), 4+(Math.random() * 8), 0.8);
				adding.vel =  new Vector2D(Math.random()-0.5, Math.random()-0.5);
				mv.gameSprites.add((Sprite)adding);
			}
			
		}
		
		return;
	}
	
	public void gameUpdate() throws Exception
	{	
		int calc_threads = 2;
		
		if (calc_threads < 1)
			throw new Exception("Calculation threads cannot be less than 1.");
		
		synchronized(mv.gameSprites)
		{			
			//Reset Physics Sprite Acceleration to none.
			for (Sprite sprite : mv.gameSprites)
			{
				if (sprite instanceof PhysicsSprite)
				{
					((PhysicsSprite) sprite).acc.x = 0;
					((PhysicsSprite) sprite).acc.y = 0;
				}
			}
			
			//Update accelerations of each PhysicsSprite in the game
			CountDownLatch latch = new CountDownLatch(calc_threads);
			double divided = mv.gameSprites.size()/calc_threads;
			for (int i = 0; i<calc_threads;i++)
			{
				executor.execute(new UpdateAccThread(mv.gameSprites, divided * i,divided * (i+1),latch));
			}
			//Wait
			try {latch.await();}catch(InterruptedException e){}
			
			
			//Update the Velocity and Position of each PhysicsSprite
			for(Sprite sprite : mv.gameSprites)
			{
				if (sprite instanceof PhysicsSprite)
					((PhysicsSprite) sprite).updateVelPos();
			}
			
			//Remove all Sprites that are marked for removal from the list
			int sprite_count = mv.gameSprites.size();
			for (int i = 0; i < sprite_count; i++)
			{
				if (mv.gameSprites.get(i).remove)
				{
					mv.gameSprites.remove(mv.gameSprites.get(i));
					i--;
					sprite_count--;
				}
			}
		}
		
		ViewCamera.pos = mv.playerShip.pos;
		
	}
	
	/***************************
	 * Called when game is changing states
	 * Allows model to gain control before the resources are disposed of
	 * Returns false to cancel the state change, true otherwise
	 ***************************/
	public boolean leaveCurrentState()
	{
		//No special behavior needed yet
		return true;
	}
	
	//Called by ModelVar class after variables initialized
	public void initGameState()
	{
		switch (mv.getGameState())
		{
			case MAIN_MENU:
				init_MainMenu();
				break;
			case GAME:
				init_Game();
				break;
		}
	}
	
	//Update function called on each tick
	public void update() throws Exception
	{
		switch (mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:
				gameUpdate();
				break;
			default:
				break;
		}
	}

	//Function called when left mouse button is clicked
	public void onLeftClick(Vector2D point){}
	
	//Function called when right mouse button is clicked
	public void onRightClick(Vector2D point){}
	
	//Function called when left mouse button is released
	public void onLeftClickRelease(Vector2D point){}
	
	//Function called when right mouse button is released
	public void onRightClickRelease(Vector2D point){}
	
	
}


class UpdateAccThread implements Runnable
{
	ArrayList<Sprite> sprites;
	int begin, end;
	CountDownLatch latch;
	
	UpdateAccThread(ArrayList<Sprite> spritelist, double begin, double end, CountDownLatch latch)
	{
		this.sprites = spritelist;
		this.begin = (int)begin;
		this.end = (int)end;
		this.latch = latch;
	}

	@Override
	public void run() 
	{
		Sprite currentSprite;
		for(int i = begin; i < end; i++)
		{
			currentSprite = sprites.get(i);
			if (currentSprite instanceof PhysicsSprite)
				((PhysicsSprite) currentSprite).updateAcc();
			
			else if (currentSprite instanceof MapBoundary)
			{
				((MapBoundary) currentSprite).checkCollision();
			}
		}
		
		//CountDown the latch so main thread can join
		latch.countDown();
	}
	
}
