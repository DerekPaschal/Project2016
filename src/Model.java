/***************************
 * Purpose: Model class of Model-View-Controller paradigm,
 * Core game model starts in this class
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

class Model
{
	int tick = 0;
	public ModelVars mv;
	
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
		mv.playerShip = new PlayerShip(new Vector2D(Game.camera.pos.x, Game.camera.pos.y));
		synchronized(mv.gameSprites)
		{
			mv.gameSprites.add(mv.playerShip);
			Bullet adding;
			for (int i = 0; i < 100; i++)
			{
				adding = new Bullet(new Vector2D());
				adding.pos = new Vector2D(Math.random()*1920,Math.random()*1080);
				adding.vel = new Vector2D(Math.random(), Math.random());
				adding.size = 5+(Math.random() * 20);
				adding.color = new Color((int)(Math.random()*128)+127, (int)(Math.random()*128)+127, (int)(Math.random()*128)+127);
				mv.gameSprites.add((Sprite)adding);
			}
			
		}
		
		return;
	}
	
	public void gameUpdate()
	{		
		for(Sprite sprite : mv.gameSprites)
		{
			if (sprite instanceof PhysicsSprite)
				((PhysicsSprite) sprite).update();
		}
		Game.camera.pos = mv.playerShip.pos;
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
	public void update()
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
