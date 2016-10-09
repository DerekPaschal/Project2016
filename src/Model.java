/***************************
 * Purpose: Model class of Model-View-Controller paradigm,
 * Core game model starts in this class
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.io.IOException;
import javax.swing.ImageIcon;
import java.util.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;

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
		mv.playerShip = new PlayerShip(new Point2D(Game.CENTERX, Game.CENTERY));
		
		//Sprite testSprite = new Sprite(new Point2D(Game.CENTERX, Game.CENTERY));
		//mv.gameSprites.add(testSprite);
		return;
	}
	
	public void gameUpdate()
	{
		mv.playerShip.update();
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
	public void onLeftClick(Point2D point){}
	
	//Function called when right mouse button is clicked
	public void onRightClick(Point2D point){}
	
	//Function called when left mouse button is released
	public void onLeftClickRelease(Point2D point){}
	
	//Function called when right mouse button is released
	public void onRightClickRelease(Point2D point){}
	
	
}
