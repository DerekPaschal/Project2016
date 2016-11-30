/***************************
 * Purpose: Model class of the Model-View-Controller
 * paradigm; core game model starts in this class
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
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
		//mv.mainMenu = new FullScreenMenu();
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
		if (this.mv.paused)
			return;
		
		int sprite_threads = 4;
		
		if (sprite_threads < 1)
			throw new Exception("Calculation threads cannot be less than 1.");
		
		//synchronized(mv.gameSpritesLock)
		synchronized(SpriteList.SpriteLock)
		{
			//Set all PhysicsSprite accelerations in gameMap to 0
			mv.gameMap.clearPhysicsSpritesAcc();
			
			//Update accelerations of each PhysicsSprite in the game
			CountDownLatch latch = new CountDownLatch(sprite_threads);
			double divided = SpriteList.size() / sprite_threads;
			
			int tempStart = 0, tempEnd = 0;
			//PhysicsSprite-PhysicsSprite collisions
			for (int i = 0; i<sprite_threads;i++)
			{
				/*if ((i + 1) == sprite_threads)
					executor.execute(new UpdateAccThread(divided * i, SpriteList.size(), latch));
				else
					executor.execute(new UpdateAccThread(divided * i, divided * (i+1), latch));
				 */
				tempStart = tempEnd;
				tempEnd = Math.max((int) (divided * (i+1)), SpriteList.size());
				
				executor.execute(new UpdateAccThread(tempStart, tempEnd, latch));
				
			}
			
			//Wait
			try {latch.await();}catch(InterruptedException e){e.printStackTrace();}
			
			//Update velocity and position of each PhysicsSprite
			mv.gameMap.updateVelPos();
			
			//Remove all Sprites that are marked for removal
			mv.gameMap.cleanPhysicsSpritesList();
		}
		
		//ViewCamera.pos = mv.playerShip.pos;
		if (SpriteList.getPlayerShip() != null)
			ViewCamera.pos = SpriteList.getPlayerShip().pos;
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
		//Determine if running PhysicsSprite-PhysicsSprite collisions or Boundary-PhysicsSprite collisions
		//if (this.sprites != null)
		//{
		
		Sprite s;
		for(int i = begin; i < end; i++)
		{
			s = SpriteList.get(i);
			if (s instanceof PhysicsSprite)
				((PhysicsSprite) s).updateAcc(i);
			else if (s instanceof MapBoundary)
				((MapBoundary)s).checkCollision();
		}
		
		/*else
		{
			for(MapBoundary currentBoundary : this.boundaries)
			{
				currentBoundary.checkCollision();
			}
			
		}*/
		//CountDown the latch so main thread can join
		latch.countDown();
	}
}
