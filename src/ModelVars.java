/***************************
 * Purpose: ModelVars class to contain primary variables of
 * the model class. Note that functionality in this class is
 * oriented towards managing variables themselves; the model
 * handles behavior of the variables, and may add to and remove
 * from lists.
 *
 * Original Author: Zachary Johnson
 ***************************/

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
				this.gameSprites = new LinkedList<Sprite>();
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
	LinkedList<Sprite> gameSprites;
	PlayerShip playerShip;
	
	/***************************
	 * GAME Private Variables
	 ***************************/
}




















