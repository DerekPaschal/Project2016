/***************************
 * Purpose: ModelVars class to contain primary variables of
 * the model class. Note that functionality in this class is
 * oriented towards managing variables themselves; the model
 * handles behavior of the variables, and may add to and remove
 * from lists.
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ModelVars
{
	/***************************
	 * Constructor
	 ***************************/
	public ModelVars(Model model)
	{
		this.m = model;
		
		this.gameState = GameState.MAIN_MENU;
	}
	
	/***************************
	 * General Public Variables
	 ***************************/
	public Model m;
	Random rand;
	int seed = GameConstant.gameSeed; //Old value is 10
	
	
	/***************************
	 * MAIN MENU Public Variables
	 ***************************/
	public FullScreenMenu mainMenu;
	
	
	/***************************
	 * MAIN MENU Private Variables
	 ***************************/
	private GameState gameState;
	
	public GameState getGameState() { return gameState; }
	public void setGameState(GameState newState)
	{
		//Request state change from model
		//Model can cancel state change by returning false
		if (!m.leaveCurrentState())
			return;
		
		//Dispose of resources for current state
		switch (gameState)
		{
			case MAIN_MENU:
				this.mainMenu = null;
				break;
			case GAME:
				this.gameMap = null;
				this.gameSprites = null;
				break;
			default:
				break;
		}
		
		//Initialize resources for new state
		switch (newState)
		{
			case MAIN_MENU:
				this.mainMenu = new FullScreenMenu();
				break;
			case GAME:
				this.gameMap = new GameMap(Game.primaryModel);
				this.gameSprites = new ArrayList<Sprite>();
				break;
			default:
				break;
		}
		
		//Change the GameState
		this.gameState = newState;
		
		//Inform model to prepare any resources
		m.initGameState();
	}
	
	
	/***************************
	 * GAME Public Variables
	 ***************************/
	GameMap gameMap;
	PlayerShip playerShip;
	public Object gameSpritesLock = new Object(); 
	
	/***************************
	 * GAME Private Variables
	 ***************************/
	private ArrayList<Sprite> gameSprites;
	
	//For use in view ONLY. This method not to be used for
	//modifying the list
	public ArrayList<Sprite> getGameSprites()
	{
		return this.gameSprites;
	}
	
	//Synchronized adding to gameSprites and physicsSprites lists
	public void addGameSprite(Sprite s)
	{
		if (this.gameState != GameState.GAME)
			throw new IllegalArgumentException("ModelVars: Error, cannot add sprite when not in GameState GAME!");
		
		synchronized(this.gameSprites)
		{
			gameSprites.add(s);
		}
		
		if (s instanceof PhysicsSprite)
		{
			gameMap.addPhysicsSprite((PhysicsSprite) s);
		}
	}
	
		//Synchronized removing from gameSprites and physicsSprites lists
		public void removeGameSprite(Sprite s)
		{
			if (this.gameState != GameState.GAME)
				throw new IllegalArgumentException("ModelVars: Error, cannot remove sprite when not in GameState GAME!");
			
			synchronized(this.gameSprites)
			{
				gameSprites.remove(s);
			}
			
			if (s instanceof PhysicsSprite)
			{
				gameMap.removePhysicsSprite((PhysicsSprite) s);
			}
		}
		
		public void cleanGameSprites()
		{
			synchronized(gameSprites)
			{
				for (Sprite sprite : this.gameSprites)
				{
					if (sprite.remove)
						this.gameSprites.remove(sprite);
				}
			}
		}
}
