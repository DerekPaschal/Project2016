/***************************
 * Purpose: Model class of Model-View-Controller paradigm,
 * Core game model starts in this class
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Rectangle;
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
		PhysicsVars.SpriteList = mv.gameSprites;
		PhysicsVars.timestep = 1.0;
		
		mv.playerShip = new PlayerShip(new Vector2D(50.0, 50.0));
		
		synchronized(mv.gameSprites)
		{
			mv.gameMap.mapBoundary.setBounds(new Rectangle(0,0,(int)ViewCamera.renderRes.x, (int)ViewCamera.renderRes.y));
			
			mv.gameSprites.add(mv.playerShip);
			Asteroid adding;
			for (int i = 0; i < 10; i++)
			{
				adding = new Asteroid(new Vector2D(Math.random()*ViewCamera.renderRes.x,Math.random()*ViewCamera.renderRes.y), 4+(Math.random() * 8));
				adding.vel =  new Vector2D(Math.random()-0.5, Math.random()-0.5);
				mv.gameSprites.add((Sprite)adding);
			}
			
		}
		
		return;
	}
	
	public void gameUpdate()
	{	
		synchronized(mv.gameSprites)
		{
			for(Sprite sprite : mv.gameSprites)
			{
				if (sprite instanceof PhysicsSprite)
					((PhysicsSprite) sprite).updateAcc();
			}
			
			for(Sprite sprite : mv.gameSprites)
			{
				if (sprite instanceof PhysicsSprite)
					((PhysicsSprite) sprite).updateVelPos();
			}
			
			Sprite sprite;
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
