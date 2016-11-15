/***************************
 * Purpose: Model class of Model-View-Controller paradigm,
 * Core game model starts in this class
 *
 * Original Author: Zachary Johnson
 ***************************/

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
		mv.gameMap.loadMap(GameMap.MapType.DEMO);
		
		return;
	}
	
	public void gameUpdate() throws Exception
	{			
		int sprite_threads = 2;
		int boundary_threads = 1;
		
		if (sprite_threads < 1 || boundary_threads < 1)
			throw new Exception("Calculation threads cannot be less than 1.");
		
		synchronized(mv.gameSpritesLock)
		{
			synchronized(mv.gameMap.physicsSpritesLock)
			{
				//Set all PhysicsSprite accelerations in gameMap to 0
				mv.gameMap.clearPhysicsSpritesAcc();
				
				//Update accelerations of each PhysicsSprite in the game
				CountDownLatch latch = new CountDownLatch(sprite_threads+boundary_threads);
				double divided = mv.gameMap.getPhysicsSpritesLength() / sprite_threads;
				
				//PhysicsSprite-PhysicsSprite collisions
				for (int i = 0; i<sprite_threads;i++)
				{
					executor.execute(new UpdateAccThread(mv.gameMap.getPhysicsSprites(), divided * i, divided * (i+1), latch));
				}
				//PhysicsSprite-Boundary collisions
				executor.execute(new UpdateAccThread(mv.gameMap.fieldBoundaries, latch));
				//Wait
				try {latch.await();}catch(InterruptedException e){}
				
				
				//Update velocity and position of each PhysicsSprite
				mv.gameMap.updateVelPos();
				
				//Remove all Sprites that are marked for removal
				mv.cleanGameSprites();
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
	ArrayList<PhysicsSprite> sprites;
	ArrayList<MapBoundary> boundaries;
	int begin, end;
	CountDownLatch latch;
	
	UpdateAccThread(ArrayList<PhysicsSprite> spriteList, double begin, double end, CountDownLatch latch)
	{
		this.sprites = spriteList;
		this.begin = (int)begin;
		this.end = (int)end;
		this.latch = latch;
	}
	
	UpdateAccThread(ArrayList<MapBoundary> boundaryList, CountDownLatch latch)
	{
		this.boundaries = boundaryList;
		this.latch = latch;
	}

	@Override
	public void run() 
	{
		//Determine if running PhysicsSprite-PhysicsSprite collisions or Boundary-PhysicsSprite collisions
		if (this.sprites != null)
		{
			PhysicsSprite currentSprite;
			for(int i = begin; i < end; i++)
			{
				currentSprite = sprites.get(i);
				currentSprite.updateAcc(Game.primaryModel.mv.gameMap.getPhysicsSprites());
			}
		}
		else
		{
			for(MapBoundary currentBoundary : this.boundaries)
			{
				currentBoundary.checkCollision();
			}
			
		}
		//CountDown the latch so main thread can join
		latch.countDown();
	}
	
}
